package com.madkroll.snake.ws.feed.lobby;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.context.GameSessionManager;
import com.madkroll.snake.ws.MessageData;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class LobbyFeed {

    private final GameSessionManager gameSessionManager;
    private final ObjectMapper objectMapper;

    private final Sinks.Many<List<String>> sink = Sinks.many().replay().latest();

    @PostConstruct
    public void enableBroadcasting() {
        Flux.interval(Duration.ofSeconds(1))
                .filter(tick -> {
                    int numberOfSubscribers = sink.currentSubscriberCount();
                    log.info("Subscribers: {}", numberOfSubscribers);
                    return numberOfSubscribers > 0;
                })
                .doOnNext(tick -> log.info("Broadcasting active sessions"))
                .map(tick -> gameSessionManager.listActiveSessionsIds())
                .subscribe(sink::tryEmitNext);
    }

    public Flux<String> provide() {
        log.info("Subscribed");
        return sink.asFlux()
                .map(activeSessions -> new MessageData("active-sessions-response", activeSessions))
                .map(this::toJson);
    }

    private String toJson(MessageData message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
