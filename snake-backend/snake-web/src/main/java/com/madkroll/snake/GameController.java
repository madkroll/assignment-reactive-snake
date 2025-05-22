package com.madkroll.snake;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// TODO: fix it by putting reversed proxy in front of client and server
@CrossOrigin(origins = "http://localhost:63342")
@RestController
@EnableWebMvc
@RequestMapping("/game")
public class GameController {

    @GetMapping(
            path = "/start",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GameSession> startNewGame() {
        return ResponseEntity.ok(new GameSession(new Grid(10, 10)));
    }
}
