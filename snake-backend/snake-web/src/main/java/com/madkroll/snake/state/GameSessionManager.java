package com.madkroll.snake.state;

import com.madkroll.snake.rest.data.StartGameRequest;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameSessionManager {

    private final Map<String, GameSession> activeSessions = new ConcurrentHashMap<>();

    public final GameSession startNewSession(final StartGameRequest startGameRequest) {
        Grid grid = new Grid(startGameRequest.width(), startGameRequest.height());

        GameSession gameSession = new GameSession(
                UUID.randomUUID().toString(),
                0L,
                grid
        );
        activeSessions.put(gameSession.getId(), gameSession);

        return gameSession;
    }
}
