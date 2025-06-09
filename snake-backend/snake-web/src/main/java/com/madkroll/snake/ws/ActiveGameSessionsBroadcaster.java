package com.madkroll.snake.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.context.GameSessionManager;
import com.madkroll.snake.ws.events.MessageData;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@AllArgsConstructor
public class ActiveGameSessionsBroadcaster {

    private final GameSessionManager gameSessionManager;
    private final ObjectMapper objectMapper;

    private final AtomicInteger subscriberCount = new AtomicInteger(0);
    private final Sinks.Many<List<String>> sink = Sinks.many().replay().latest();

    @PostConstruct
    public void enableBroadcasting() {
        Flux.interval(Duration.ofSeconds(1))
                .filter(tick -> {
                    log.info("Subscribers: {}", subscriberCount.get());
                    return subscriberCount.get() > 0;
                })
                .doOnNext(tick -> log.info("Broadcasting active sessions"))
                .map(tick -> gameSessionManager.listActiveSessionsIds())
                .subscribe(sink::tryEmitNext);
    }

    public Flux<String> subscribe() {
        log.info("Subscribed");
        subscriberCount.incrementAndGet();
        return sink.asFlux()
                .map(activeSessions -> new MessageData("active-sessions-response", activeSessions))
                .map(this::toJson)
                .doFinally(signalType -> {
                    log.info("Unsubscribing from broadcast");
                    subscriberCount.decrementAndGet();
                });
    }

    private String toJson(MessageData message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
