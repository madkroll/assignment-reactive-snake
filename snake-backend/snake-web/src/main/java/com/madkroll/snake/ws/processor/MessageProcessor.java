package com.madkroll.snake.ws.processor;

import com.madkroll.snake.ws.MessageData;

public interface MessageProcessor {

    String getType();

    void process(MessageData messageData);
}
