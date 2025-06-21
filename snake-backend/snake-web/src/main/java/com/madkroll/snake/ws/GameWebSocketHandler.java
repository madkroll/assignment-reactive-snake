package com.madkroll.snake.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
public class GameWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final LobbyFeed lobbyFeed;
    private final UnknownTypeProcessor unknownTypeProcessor;
    private final Map<String, MessageProcessor> messageProcessors;

    public GameWebSocketHandler(
            ObjectMapper objectMapper,
            LobbyFeed lobbyFeed,
            UnknownTypeProcessor unknownTypeProcessor,
            List<MessageProcessor> messageProcessors
    ) {
        this.objectMapper = objectMapper;
        this.lobbyFeed = lobbyFeed;
        this.unknownTypeProcessor = unknownTypeProcessor;
        this.messageProcessors =
                messageProcessors
                        .stream()
                        .collect(toMap(MessageProcessor::getType, messageProcessor -> messageProcessor));
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> input =
                session
                        .receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map(this::parseMessage)
                        .doOnNext(this::applyProcessor)
                        .then();

        Flux<WebSocketMessage> output =
                lobbyFeed
                        .provide()
                        .map(session::textMessage);

        return session.send(output).and(input);
    }

    private void applyProcessor(MessageData messageData) {
        messageProcessors
                .getOrDefault(messageData.type(), unknownTypeProcessor)
                .process(messageData);
    }

    private MessageData parseMessage(String messageAsJson) {
        try {
            return objectMapper.readValue(messageAsJson, MessageData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
