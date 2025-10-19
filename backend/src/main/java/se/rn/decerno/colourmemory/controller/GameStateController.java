package se.rn.decerno.colourmemory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.rn.decerno.colourmemory.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import se.rn.decerno.colourmemory.model.GameStatus;
import se.rn.decerno.colourmemory.service.GameService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/game")
public class GameStateController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public GameState startGame() {
        return gameService.startGame();
    }

    @PostMapping("/flip/{position}")
    public GameState flipCard(@PathVariable int position) {
        GameState gameState = gameService.getGameState();

        if (gameState == null || gameState.getGameStatus() != GameStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game has not been started yet");
        }
        if (position < 0 || position >= gameState.getCardsPositions().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid position");
        }

        return gameService.flipCard(position);
    }
}
