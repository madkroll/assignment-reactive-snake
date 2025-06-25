package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class GameState {

    private final long createdAt;
    private final AtomicLong turn;
    private final int turnRate;
    private final Grid grid;
    private final List<Player> players;
}
