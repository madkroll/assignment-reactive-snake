package com.madkroll.snake.ws.feed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.context.GameSessionManager;
import com.madkroll.snake.ws.MessageData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Component
@AllArgsConstructor
public class LobbyFeed {

    private final GameSessionManager gameSessionManager;
    private final ObjectMapper objectMapper;

    private final Flux<String> activeSessionsFeed = Flux.interval(Duration.ofSeconds(1))
            .publish()
            .refCount(1)
            .map(tick -> activeSessionsAsJson());

    public Flux<String> provide() {
        return activeSessionsFeed;
    }

    private String activeSessionsAsJson() {
        try {
            return objectMapper.writeValueAsString(
                    new MessageData("active-sessions-response", gameSessionManager.listActiveSessionsIds())
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
