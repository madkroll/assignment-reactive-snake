package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Sinks;

@Getter
@AllArgsConstructor
public class Player {

    String id;
    Sinks.Many<String> feed;
}
