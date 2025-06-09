package com.madkroll.snake.ws.events;

public record StartGameRequested(
        int width,
        int height,
        int turnRate
) {
}
