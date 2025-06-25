package com.madkroll.snake.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.state.Player;
import com.madkroll.snake.ws.feed.LobbyFeed;
import com.madkroll.snake.ws.processor.MessageProcessor;
import com.madkroll.snake.ws.processor.UnknownTypeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
public class GameWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final LobbyFeed lobbyFeed;
    private final Map<String, Player> connectedPlayersRegistry;
    private final UnknownTypeProcessor unknownTypeProcessor;
    private final Map<String, MessageProcessor> messageProcessors;

    public GameWebSocketHandler(
            ObjectMapper objectMapper,
            LobbyFeed lobbyFeed,
            Map<String, Player> connectedPlayersRegistry,
            UnknownTypeProcessor unknownTypeProcessor,
            List<MessageProcessor> messageProcessors
    ) {
        this.objectMapper = objectMapper;
        this.lobbyFeed = lobbyFeed;
        this.connectedPlayersRegistry = connectedPlayersRegistry;
        this.unknownTypeProcessor = unknownTypeProcessor;
        this.messageProcessors =
                messageProcessors
                        .stream()
                        .collect(toMap(MessageProcessor::getType, messageProcessor -> messageProcessor));
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final Player player = new Player(
                UUID.randomUUID().toString(),
                Sinks.many().unicast().onBackpressureBuffer(),
                Sinks.many().multicast().onBackpressureBuffer()
        );
        connectedPlayersRegistry.put(player.id(), player);

        Mono<Void> input =
                session
                        .receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map(this::parseMessage)
                        .doOnNext(messageData -> applyProcessor(messageData, player))
                        .then();

        Flux<WebSocketMessage> allPlayerUpdatesFeed = Flux.merge(
                        player.feed().asFlux(),
                        Flux.switchOnNext(player.subscription().asFlux())
                )
                .map(session::textMessage)
                .doOnSubscribe(subscription -> player.subscription().tryEmitNext(lobbyFeed.provide()));

        return session
                .send(allPlayerUpdatesFeed)
                .and(input)
                .doOnTerminate(() -> {
                    player.feed().tryEmitComplete();
                    player.subscription().tryEmitComplete();
                    connectedPlayersRegistry.remove(player.id());
                });
    }

    private void applyProcessor(MessageData messageData, Player player) {
        messageProcessors
                .getOrDefault(messageData.type(), unknownTypeProcessor)
                .process(messageData, player)
                .map(this::writeResponseMessage)
                .ifPresent(responseMessage -> player.feed().tryEmitNext(responseMessage));
    }

    private String writeResponseMessage(MessageData messageData) {
        try {
            return objectMapper.writeValueAsString(messageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageData parseMessage(String messageAsJson) {
        try {
            return objectMapper.readValue(messageAsJson, MessageData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
