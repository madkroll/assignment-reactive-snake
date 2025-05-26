package com.madkroll.snake.rest;

import com.madkroll.snake.rest.data.StartGameRequest;
import com.madkroll.snake.state.GameSession;
import com.madkroll.snake.state.GameSessionManager;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
@RequestMapping("/game")
@AllArgsConstructor
public class GameController {

    private final GameSessionManager gameSessionManager;

    @PostMapping(
            path = "/start",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GameSession> startNewGame(@RequestBody final StartGameRequest startGameRequest) {
        // TODO: validate input
        return ResponseEntity.ok(gameSessionManager.startNewSession(startGameRequest));
    }
}
