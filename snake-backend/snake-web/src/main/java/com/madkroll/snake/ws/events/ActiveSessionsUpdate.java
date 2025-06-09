package com.madkroll.snake.ws.events;

import java.util.List;

public record ActiveSessionsUpdate(
        String type,
        List<String> activeSessions
) {

    public ActiveSessionsUpdate(List<String> activeSessions) {
        this("active-sessions-response", activeSessions);
    }
}
