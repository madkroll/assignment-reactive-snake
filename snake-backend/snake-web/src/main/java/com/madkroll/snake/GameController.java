package com.madkroll.snake;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
@RequestMapping("/game")
public class GameController {

    @GetMapping(
            path = "/start"
    )
    public ResponseEntity<GameSession> startNewGame() {
        return ResponseEntity.ok(new GameSession(new Grid(10, 10)));
    }
}
