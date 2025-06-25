package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class GameSession {

    private final String id;
    private final GameState gameState;
    private final Flux<String> feed;

}
