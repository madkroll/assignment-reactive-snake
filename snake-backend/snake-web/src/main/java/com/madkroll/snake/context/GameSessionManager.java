package com.madkroll.snake.context;

import com.madkroll.snake.state.GameSession;
import com.madkroll.snake.state.Grid;
import com.madkroll.snake.ws.processor.StartGameRequestProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class GameSessionManager {

    private final Map<String, GameSession> activeSessionsRegistry;

    public final GameSession startNewSession(final StartGameRequestProcessor.StartGameRequestPayload startGameRequestPayload) {
        Grid grid = new Grid(startGameRequestPayload.width(), startGameRequestPayload.height());

        GameSession gameSession = new GameSession(
                UUID.randomUUID().toString(),
                0L,
                grid
        );
        activeSessionsRegistry.put(gameSession.getId(), gameSession);

        return gameSession;
    }

    public List<String> listActiveSessionsIds() {
        return List.copyOf(activeSessionsRegistry.keySet());
    }
}
