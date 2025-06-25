package com.madkroll.snake.ws.processor;

import com.madkroll.snake.state.Player;
import com.madkroll.snake.ws.MessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UnknownTypeProcessor implements MessageProcessor {

    public static final String UNKNOWN_TYPE = "unknown-type";

    @Override
    public String getType() {
        return UNKNOWN_TYPE;
    }

    @Override
    public Optional<MessageData> process(MessageData messageData, Player player) {
        log.info("Message of unknown type '{}' received from player {}", messageData, player.id());
        return Optional.of(
                new MessageData(
                        "system-message",
                        "Unknown message type: " + messageData.type()
                )
        );
    }
}
