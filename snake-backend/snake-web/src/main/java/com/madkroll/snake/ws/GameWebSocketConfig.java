package com.madkroll.snake.ws;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
@AllArgsConstructor
public class GameWebSocketConfig {

    public static final int ORDER_BEFORE_MVC_ROUTES = -1;

    private final GameWebSocketHandler gameWebSocketHandler;

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public HandlerMapping webSocketMapping() {
        return new SimpleUrlHandlerMapping(
                Map.of("/ws", gameWebSocketHandler),
                ORDER_BEFORE_MVC_ROUTES
        );
    }


}
