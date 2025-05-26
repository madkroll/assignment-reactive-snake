package com.madkroll.snake.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madkroll.snake.state.Player;
import com.madkroll.snake.ws.events.PlayerConnected;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Player> connectedPlayersRegistry;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New connection established");
        Player player = new Player(
                UUID.randomUUID().toString(),
                session
        );

        connectedPlayersRegistry.put(session.getId(), player);

        // notify that user is registered
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new PlayerConnected(player.getId(), session.getId()))));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received text message: {}", message.getPayload());

        JsonNode json = objectMapper.readTree(message.getPayload());
        String type = json.get("type").asText();

        switch (type) {
            case "move":
                String direction = json.get("direction").asText();
                log.info("Move: {}", direction);
                break;
            default:
                log.info("Unknown type: {}", type);
        }

        session.sendMessage(new TextMessage("{\"type\":\"ack\", \"message\":\"Command received\"}"));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket error: {}", exception.getMessage(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection closed: {}", session.getId());
        Player player = connectedPlayersRegistry.remove(session.getId());
        log.info("Player unregistered: {}", player.getId());
    }
}
