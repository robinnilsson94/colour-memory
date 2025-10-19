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

    public static final int NO_OF_CARDS = 16;

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
        assertEquals(NO_OF_CARDS, state.getCardsPositions().size(), "Should create " + NO_OF_CARDS + " cards");
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

        CardPair blackCardPositions = findMatchingBlackCardsAndMatchThem(cardsPositions);

        assertTrue(cardsPositions.get(blackCardPositions.first).isMatched());
        assertTrue(cardsPositions.get(blackCardPositions.second).isMatched());
        assertEquals(GameStatus.IN_PROGRESS, state.getGameStatus());
        assertEquals(1, state.getScore(), "Score should increase after matching pair");
        assertEquals(0, state.getFlippedCards().size(), "Flipped cards should be cleared after matching pair");
    }


    @Test
    void flipCard_different_colors_should_reduce_score_and_reset_flipped_cards() {
        gameService.startGame();
        GameState state = gameService.getGameState();

        findMatchingBlackCardsAndMatchThem(state.getCardsPositions());

        assertEquals(1, state.getScore(), "Score should increase after matching a pair");

        Map<Integer, Card> cards = state.getCardsPositions();
        int unmatchedCardPosition = findUnmatchedCardPosition(state.getCardsPositions());
        int nonMatchingPosition = findDifferentColorUnmatched(cards, cards.get(unmatchedCardPosition).getColor());

        gameService.flipCard(unmatchedCardPosition);
        gameService.flipCard(nonMatchingPosition);

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

    private int findUnmatchedCardPosition(Map<Integer, Card> cards) {
        return cards.entrySet().stream().filter(e -> !e.getValue().isMatched())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    private int findDifferentColorUnmatched(Map<Integer, Card> cardPositions, Color colorToExclude) {
        return cardPositions.entrySet().stream().filter(e -> !e.getValue().getColor().equals(colorToExclude)
                        && !e.getValue().isMatched())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    private record CardPair(int first, int second) {
    }

    private CardPair findMatchingBlackCardsAndMatchThem(Map<Integer, Card> cardPositions) {
        List<Card> matchingBlackCards = cardPositions.values().stream()
                .filter(card -> card.getColor() == Color.BLACK)
                .limit(2)
                .toList();

        int pos1 = findPositionOfCard(cardPositions, matchingBlackCards.get(0));
        int pos2 = findPositionOfCard(cardPositions, matchingBlackCards.get(1));

        gameService.flipCard(pos1);
        gameService.flipCard(pos2);

        return new CardPair(pos1, pos2);
    }
}