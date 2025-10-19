package se.rn.decerno.colourmemory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameState {

    private int score;
    private GameStatus gameStatus;
    private Map<Integer, Card> cardsPositions;
    private final List<Card> flippedCards = new ArrayList<>();

    public GameState(int score, GameStatus gameStatus, Map<Integer, Card> cardPositions) {
        this.score = score;
        this.gameStatus = gameStatus;
        this.cardsPositions = cardPositions;
    }

    public int getScore() {
        return score;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Map<Integer, Card> getCardsPositions() {
        return cardsPositions;
    }

    public void setCardsPositions(Map<Integer, Card> cardsPositions) {
        this.cardsPositions = cardsPositions;
    }

    public List<Card> getFlippedCards() {
        return flippedCards;
    }

    public void incrementScore() {
        score++;
    }

    public void reduceScore() {
        if (score > 0) {
            score--;
        }
    }
}
