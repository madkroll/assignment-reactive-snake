package com.madkroll.snake.rest.data;

public record StartGameRequest(
        int width,
        int height,
        int turnRate
) {
}
