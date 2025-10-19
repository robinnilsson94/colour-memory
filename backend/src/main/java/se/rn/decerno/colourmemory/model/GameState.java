package se.rn.decerno.colourmemory.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {

    private int score;
    private int moveCount;
    private int numberOfMatches;
    private double accuracy;
    private GameStatus gameStatus;
    private Map<Integer, Card> cardsPositions = new HashMap<>();
    private final List<Card> flippedCards = new ArrayList<>();

    public int getScore() {
        return score;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
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

    public void incrementMoveCount() {
        moveCount++;
    }

    public void incrementNumberOfMatches() {
        numberOfMatches++;
    }
}
