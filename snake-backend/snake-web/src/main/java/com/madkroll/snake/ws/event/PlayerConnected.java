package com.madkroll.snake.ws.event;

public record PlayerConnected(
        String type,
        String playerId,
        String sessionId
) {

    public PlayerConnected(String playerId, String sessionId) {
        this("player-connected", playerId, sessionId);
    }
}
