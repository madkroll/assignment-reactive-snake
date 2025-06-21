package com.madkroll.snake.ws;

public record MessageData(
        String type,
        Object payload
) {
}
