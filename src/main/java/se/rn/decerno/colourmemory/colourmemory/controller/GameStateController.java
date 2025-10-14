package se.rn.decerno.colourmemory.colourmemory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.rn.decerno.colourmemory.colourmemory.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import se.rn.decerno.colourmemory.colourmemory.model.GameStatus;
import se.rn.decerno.colourmemory.colourmemory.service.GameService;

@RestController
@RequestMapping("/game")
public class GameStateController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public GameState startGame() {
        return gameService.startGame();
    }

    @PostMapping("/end")
    public GameState endGame() {
        return gameService.endGame();
    }

    @PostMapping("/flip/{position}")
    public GameState flipCard(@PathVariable int position) {
        GameState gameState = gameService.getGameState();

        if (gameState == null || gameState.getGameStatus() != GameStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game has not been started yet");
        }

        if (position < 0 || position >= 16) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid position");
        }

        return gameService.flipCard(position);
    }

    @GetMapping("/state")
    public GameState getGameState() {
        return gameService.getGameState();
    }
}
