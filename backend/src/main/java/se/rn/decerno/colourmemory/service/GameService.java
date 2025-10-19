package se.rn.decerno.colourmemory.service;

import org.springframework.stereotype.Service;
import se.rn.decerno.colourmemory.model.Card;
import se.rn.decerno.colourmemory.model.Color;
import se.rn.decerno.colourmemory.model.GameState;
import se.rn.decerno.colourmemory.model.GameStatus;

import java.util.*;

@Service
public class GameService {

    public static final int GRID_SIZE = 16;

    private GameState gameState;

    public GameState startGame() {
        gameState = new GameState(0, GameStatus.IN_PROGRESS, new HashMap<>());

        createCardsAndShuffle();

        return gameState;
    }

    public GameState flipCard(int position) {
        Card card = gameState.getCardsPositions().get(position);

        if (card == null || card.isMatched() || gameState.getFlippedCards().contains(card)) {
            return gameState;
        }

        gameState.incrementMoveCount();

        List<Card> flippedCards = gameState.getFlippedCards();
        flippedCards.add(card);

        if (flippedCards.size() == 2) {
            compareCardsAndUpdateGameState(flippedCards.get(0), flippedCards.get(1));
        }

        return gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    private void createCardsAndShuffle() {
        List<Card> cards = new ArrayList<>();

        for (Color color : Color.values()) {
            cards.add(new Card(UUID.randomUUID().toString(), color));
            cards.add(new Card(UUID.randomUUID().toString(), color));
        }

        Collections.shuffle(cards);

        Map<Integer, Card> cardPositions = new HashMap<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            cardPositions.put(i, cards.get(i));
        }

        gameState.setCardsPositions(cardPositions);
    }

    private void compareCardsAndUpdateGameState(Card card1, Card card2) {
        if (card1.getColor() == card2.getColor()) {
            gameState.incrementScore();
            card1.setMatched(true);
            card2.setMatched(true);

            gameState.incrementNumberOfMatches();

            boolean allMatched = gameState.getCardsPositions().values().stream()
                    .allMatch(Card::isMatched);

            if (allMatched) {
                gameState.setGameStatus(GameStatus.FINISHED);
            }
        } else {
            gameState.reduceScore();
        }

        // Accuracy is calculated by dividing the number of matches with the number of comparisons made (2 moves means 1 comparison)
        gameState.setAccuracy((double) gameState.getNumberOfMatches() / ((double) gameState.getMoveCount() / 2));
        gameState.getFlippedCards().clear();
    }
}
