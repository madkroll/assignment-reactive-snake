package com.madkroll.snake.ws;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GameWebSocketHandler implements WebSocketHandler {

    private final ActiveGameSessionsBroadcaster activeGameSessionsBroadcaster;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> send =
                session.send(
                        activeGameSessionsBroadcaster
                                .subscribe()
                                .map(session::textMessage)
                );

        Mono<Void> receive = session.receive().doOnNext(
                webSocketMessage -> {
                }
        ).then();

        return Mono.zip(send, receive).then();
    }
}
