package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Player {

    String id;
    Sinks.Many<String> feed;
    Sinks.Many<Flux<String>> subscription;
}
