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
        gameState = new GameState();
        gameState.setGameStatus(GameStatus.IN_PROGRESS);

        createCardsAndShuffle();

        return gameState;
    }

    /**
     * Flips a card at the given position. If two cards are flipped, they are compared.
     *
     * @param position the position of the card to flip (0-15)
     * @return the updated game state
     */
    public GameState flipCard(int position) {
        Card card = gameState.getCardsPositions().get(position);

        // Ignore if the card doesn't exist, is already matched, or is already flipped
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

        // Create two cards for each color (pairs)
        for (Color color : Color.values()) {
            cards.add(new Card(UUID.randomUUID().toString(), color));
            cards.add(new Card(UUID.randomUUID().toString(), color));
        }

        Collections.shuffle(cards);

        // Assign shuffled cards to grid positions
        Map<Integer, Card> cardPositions = new HashMap<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            cardPositions.put(i, cards.get(i));
        }

        gameState.setCardsPositions(cardPositions);
    }

    /**
     * Compares two flipped cards. If they match, marks them as matched and increments score.
     * If they don't match, decrements score. Updates accuracy and clears flipped cards.
     */
    private void compareCardsAndUpdateGameState(Card card1, Card card2) {
        if (card1.getColor() == card2.getColor()) {
            gameState.incrementScore();
            card1.setMatched(true);
            card2.setMatched(true);

            gameState.incrementNumberOfMatches();

            // Game finishes if all cards are matched
            boolean allMatched = gameState.getCardsPositions().values().stream()
                    .allMatch(Card::isMatched);

            if (allMatched) {
                gameState.setGameStatus(GameStatus.FINISHED);
            }
        } else {
            gameState.reduceScore();
        }

        // Accuracy = successful matches / total attempts (2 moves = 1 attempt)
        gameState.setAccuracy((double) gameState.getNumberOfMatches() / ((double) gameState.getMoveCount() / 2));
        gameState.getFlippedCards().clear();
    }
}
