package com.madkroll.snake.ws.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.context.GameSessionManager;
import com.madkroll.snake.state.GameSession;
import com.madkroll.snake.state.Player;
import com.madkroll.snake.ws.MessageData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class StartGameRequestProcessor implements MessageProcessor {

    private static final String TYPE_START_GAME_REQUEST = "start-game-request";
    private static final String TYPE_START_GAME_ACKNOWLEDGEMENT = "start-game-acknowledgement";

    private final ObjectMapper objectMapper;
    private final GameSessionManager gameSessionManager;

    @Override
    public String getType() {
        return TYPE_START_GAME_REQUEST;
    }

    public Optional<MessageData> process(MessageData messageData, Player player) {
        GameSession gameSession = gameSessionManager.startNewSession(
                objectMapper.convertValue(messageData.payload(), StartGameRequestPayload.class)
        );
        log.info("Game session started: {}", gameSession);

        gameSession.players().add(player);
        player.subscription().tryEmitNext(gameSession.feed());

        return Optional.of(
                new MessageData(
                        TYPE_START_GAME_ACKNOWLEDGEMENT,
                        new StartGameAcknowledgementPayload(gameSession.id())
                )
        );
    }

    public record StartGameRequestPayload(
            int width,
            int height,
            int turnRate
    ) {
    }

    public record StartGameAcknowledgementPayload(
            String gameSessionId
    ) {
    }
}
