package com.madkroll.snake.context;

import com.madkroll.snake.rest.data.StartGameRequest;
import com.madkroll.snake.state.GameSession;
import com.madkroll.snake.state.Grid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class GameSessionManager {

    private final Map<String, GameSession> activeSessionsRegistry;

    public final GameSession startNewSession(final StartGameRequest startGameRequest) {
        Grid grid = new Grid(startGameRequest.width(), startGameRequest.height());

        GameSession gameSession = new GameSession(
                UUID.randomUUID().toString(),
                0L,
                grid
        );
        activeSessionsRegistry.put(gameSession.getId(), gameSession);

        return gameSession;
    }
}
