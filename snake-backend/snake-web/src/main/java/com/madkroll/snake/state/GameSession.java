package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameSession {

    private final String id;
    private final long turn;
    private final Grid grid;
}
