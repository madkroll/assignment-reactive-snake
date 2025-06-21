package com.madkroll.snake.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.state.GameSession;
import com.madkroll.snake.state.Player;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class GameConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Map<String, GameSession> activeSessionsRegistry() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Player> connectedPlayersRegistry() {
        return new ConcurrentHashMap<>();
    }
}
