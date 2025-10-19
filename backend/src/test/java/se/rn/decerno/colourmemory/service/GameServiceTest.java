package se.rn.decerno.colourmemory.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.rn.decerno.colourmemory.model.Card;
import se.rn.decerno.colourmemory.model.Color;
import se.rn.decerno.colourmemory.model.GameState;
import se.rn.decerno.colourmemory.model.GameStatus;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void startGame_should_initialize_game_state_with_16_cards() {
        GameState state = gameService.startGame();

        assertNotNull(state, "GameState should not be null");
        assertEquals(GameStatus.IN_PROGRESS, state.getGameStatus());
        assertEquals(16, state.getCardsPositions().size(), "Should create 16 cards");
        assertEquals(0, state.getScore(), "Initial score should be 0");
    }

    @Test
    void flipCard_should_add_card_to_flipped_cards() {
        gameService.startGame();
        GameState state = gameService.flipCard(0);

        assertEquals(1, state.getFlippedCards().size(), "Should have one flipped card");
    }

    @Test
    void flipCard_same_color_should_increase_score_and_mark_matched_and_reset_flipped_cards() {
        gameService.startGame();
        GameState state = gameService.getGameState();
        Map<Integer, Card> cardsPositions = state.getCardsPositions();

        List<Card> matchingBlackCards = cardsPositions.values().stream()
                .filter(card -> card.getColor() == Color.BLACK)
                .limit(2)
                .toList();

        int pos1 = findPositionOfCard(cardsPositions, matchingBlackCards.get(0));
        int pos2 = findPositionOfCard(cardsPositions, matchingBlackCards.get(1));

        gameService.flipCard(pos1);
        gameService.flipCard(pos2);

        assertTrue(cardsPositions.get(pos1).isMatched());
        assertTrue(cardsPositions.get(pos2).isMatched());
        assertEquals(GameStatus.IN_PROGRESS, state.getGameStatus());
        assertEquals(1, state.getScore(), "Score should increase after matching pair");
        assertEquals(0, state.getFlippedCards().size(), "Flipped cards should be cleared after matching pair");
    }


    @Test
    void flipCard_different_colors_should_reduce_score_and_reset_flipped_cards() {
        gameService.startGame();
        GameState state = gameService.getGameState();
        state.incrementScore();

        Map<Integer, Card> cards = state.getCardsPositions();
        int first = 0;
        int second = findDifferentColor(cards, cards.get(first).getColor());

        gameService.flipCard(first);
        gameService.flipCard(second);

        assertEquals(0, state.getScore(), "Score should decrease on mismatch");
        assertEquals(0, state.getFlippedCards().size(), "Flipped cards should be cleared after mismatch");
    }

    @Test
    void game_should_finish_when_all_cards_are_matched() {
        gameService.startGame();
        GameState state = gameService.getGameState();
        Map<Integer, Card> cardsPositions = state.getCardsPositions();

        List<Card> matchingBlackCards = cardsPositions.values().stream()
                .filter(card -> card.getColor() == Color.BLACK)
                .limit(2)
                .toList();

        cardsPositions.values().forEach(card -> card.setMatched(true));
        matchingBlackCards.forEach(card -> card.setMatched(false));

        int pos1 = findPositionOfCard(cardsPositions, matchingBlackCards.get(0));
        int pos2 = findPositionOfCard(cardsPositions, matchingBlackCards.get(1));

        gameService.flipCard(pos1);
        gameService.flipCard(pos2);

        assertEquals(GameStatus.FINISHED, state.getGameStatus());
    }

    private int findPositionOfCard(Map<Integer, Card> cardPositions, Card card) {
        return cardPositions.entrySet().stream().filter(e -> e.getValue().equals(card))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    private int findDifferentColor(Map<Integer, Card> cardPositions, Color colorToExclude) {
        return cardPositions.entrySet().stream().filter(e -> !e.getValue().getColor().equals(colorToExclude))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }
}