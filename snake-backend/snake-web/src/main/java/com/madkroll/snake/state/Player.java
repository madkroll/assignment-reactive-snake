package com.madkroll.snake.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.reactive.socket.WebSocketSession;

@Getter
@AllArgsConstructor
public class Player {

    String id;
    WebSocketSession session;
}
