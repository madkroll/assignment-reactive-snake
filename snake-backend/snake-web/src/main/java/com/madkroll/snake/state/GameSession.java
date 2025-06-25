package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class GameSession {

    private final String id;
    private final long createdAt;
    private final long turn;
    private final long turnRate;
    private final Grid grid;
    private final List<Player> players;
    private final Flux<String> feed;
}
