package com.madkroll.snake.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.context.GameSessionManager;
import com.madkroll.snake.state.GameSession;
import com.madkroll.snake.ws.events.MessageData;
import com.madkroll.snake.ws.events.StartGameRequested;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class GameWebSocketHandler implements WebSocketHandler {

    private final ActiveGameSessionsBroadcaster activeGameSessionsBroadcaster;
    private final GameSessionManager gameSessionManager;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> input =
                session
                        .receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map(this::parseMessage)
                        .doOnNext(this::processMessage)
                        .then();

        Flux<WebSocketMessage> output =
                activeGameSessionsBroadcaster
                        .subscribe()
                        .map(session::textMessage);

        return session.send(output).and(input);
    }

    private void processMessage(MessageData messageData) {
        if (messageData.type().equals("start-game-request")) {
            GameSession gameSession = gameSessionManager.startNewSession(objectMapper.convertValue(messageData.payload(), StartGameRequested.class));
            log.info("Game session started: {}", gameSession);
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
