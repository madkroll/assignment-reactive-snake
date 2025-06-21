package com.madkroll.snake.ws.processor;

import com.madkroll.snake.ws.MessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UnknownTypeProcessor implements MessageProcessor {

    public static final String UNKNOWN_TYPE = "unknown-type";

    @Override
    public String getType() {
        return UNKNOWN_TYPE;
    }

    @Override
    public void process(MessageData messageData) {
        log.info("Message of unknown type received: {}", messageData);
    }
}
