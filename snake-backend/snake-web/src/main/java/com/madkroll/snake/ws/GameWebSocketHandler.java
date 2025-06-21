package com.madkroll.snake.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.state.Player;
import com.madkroll.snake.ws.feed.lobby.LobbyFeed;
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
                Sinks.many().unicast().onBackpressureBuffer()
        );
        connectedPlayersRegistry.put(player.getId(), player);

        Mono<Void> input =
                session
                        .receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map(this::parseMessage)
                        .doOnNext(messageData -> applyProcessor(messageData, player))
                        .then();

        Flux<WebSocketMessage> lobbySignals =
                lobbyFeed
                        .provide()
                        .map(session::textMessage);

        Flux<WebSocketMessage> playerSignals =
                player.getFeed().asFlux().map(session::textMessage);

        return session
                .send(Flux.merge(lobbySignals, playerSignals))
                .and(input)
                .doOnTerminate(() -> {
                    player.getFeed().tryEmitComplete();
                    connectedPlayersRegistry.remove(player.getId());
                });
    }

    private void applyProcessor(MessageData messageData, Player player) {
        messageProcessors
                .getOrDefault(messageData.type(), unknownTypeProcessor)
                .process(messageData)
                .map(this::writeResponseMessage)
                .ifPresent(responseMessage -> {
                    player.getFeed().tryEmitNext(responseMessage);
                });
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
