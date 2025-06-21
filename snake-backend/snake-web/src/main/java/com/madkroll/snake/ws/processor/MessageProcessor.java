package com.madkroll.snake.ws.processor;

import com.madkroll.snake.ws.MessageData;

import java.util.Optional;

public interface MessageProcessor {

    String getType();

    Optional<MessageData> process(MessageData messageData);
}
