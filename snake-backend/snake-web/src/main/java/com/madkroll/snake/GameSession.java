package com.madkroll.snake;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GameSession {

    private final Grid grid;
    private final List<SnakePart> snake;
}
