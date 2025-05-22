package com.madkroll.snake;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.LinkedList;
import java.util.List;

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
        // game session initialization
        List<SnakePart> snake = new LinkedList<>();
        snake.add(new SnakePart(5, 4));
        snake.add(new SnakePart(5, 5));
        snake.add(new SnakePart(5, 6));
        Grid grid = new Grid(10, 10);
        Fruit fruit = new Fruit(1, 1);
        GameSession gameSession = new GameSession(
                0L,
                grid,
                snake,
                fruit
        );

        return ResponseEntity.ok(gameSession);
    }
}
