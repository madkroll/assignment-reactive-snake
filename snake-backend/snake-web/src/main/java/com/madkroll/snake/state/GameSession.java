package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GameSession {

    private String id;
    private final long turn;
    private final Grid grid;
}
