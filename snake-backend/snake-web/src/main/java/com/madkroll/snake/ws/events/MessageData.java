package com.madkroll.snake.ws.events;

public record MessageData(
        String type,
        Object payload
) {
}
