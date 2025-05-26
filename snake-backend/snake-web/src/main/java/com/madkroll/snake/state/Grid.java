package com.madkroll.snake.state;

import lombok.Getter;

import java.util.Random;

@Getter
public class Grid {

    private final int width;
    private final int height;
    private final Renderable[][] renderables;

    public Grid(final int width, final int height) {
        this.width = width;
        this.height = height;
        renderables = new Renderable[height][width];
    }
}
