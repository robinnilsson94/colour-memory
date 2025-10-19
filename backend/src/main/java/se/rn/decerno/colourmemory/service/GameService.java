package se.rn.decerno.colourmemory.service;

import org.springframework.stereotype.Service;
import se.rn.decerno.colourmemory.model.Card;
import se.rn.decerno.colourmemory.model.Color;
import se.rn.decerno.colourmemory.model.GameState;
import se.rn.decerno.colourmemory.model.GameStatus;

import java.util.*;

@Service
public class GameService {

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

        List<Card> flippedCards = gameState.getFlippedCards();
        flippedCards.add(card);

        if (flippedCards.size() == 2) {
            compareCards(flippedCards);
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
        for (int i = 0; i < 16; i++) {
            cardPositions.put(i, cards.get(i));
        }

        gameState.setCardsPositions(cardPositions);
    }

    private void compareCards(List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);

        if (card1.getColor() == card2.getColor()) {
            gameState.incrementScore();
            card1.setMatched(true);
            card2.setMatched(true);

            boolean allMatched = gameState.getCardsPositions().values().stream()
                    .allMatch(Card::isMatched);

            if (allMatched) {
                gameState.setGameStatus(GameStatus.FINISHED);
            }

        } else {
            gameState.reduceScore();
        }

        gameState.getFlippedCards().clear();
    }
}
