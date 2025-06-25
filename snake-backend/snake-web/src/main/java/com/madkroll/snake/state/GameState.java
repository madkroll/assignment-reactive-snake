package com.madkroll.snake.state;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public record GameState(
        long createdAt,
        AtomicLong turn,
        int turnRate,
        Grid grid,
        List<Player> players
) {
}
