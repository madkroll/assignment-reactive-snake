package com.madkroll.snake.context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.state.GameSession;
import com.madkroll.snake.state.Grid;
import com.madkroll.snake.ws.MessageData;
import com.madkroll.snake.ws.processor.StartGameRequestProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class GameSessionManager {

    private final ObjectMapper objectMapper;
    private final Map<String, GameSession> activeSessionsRegistry;

    public final GameSession startNewSession(
            final StartGameRequestProcessor.StartGameRequestPayload startGameRequestPayload
    ) {
        Grid grid = new Grid(startGameRequestPayload.width(), startGameRequestPayload.height());

        GameSession gameSession = new GameSession(
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                0L,
                startGameRequestPayload.turnRate(),
                grid,
                new ArrayList<>(),
                Flux.interval(Duration.ofSeconds(1))
                        .publish()
                        .refCount(1)
                        .map(tick -> gameTurnAsJson())
        );

        activeSessionsRegistry.put(gameSession.id(), gameSession);

        return gameSession;
    }

    public List<String> listActiveSessionsIds() {
        return List.copyOf(activeSessionsRegistry.keySet());
    }

    private String gameTurnAsJson() {
        try {
            log.info("next turn");
            return objectMapper.writeValueAsString(
                    new MessageData("game-turn", "game-turn")
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
