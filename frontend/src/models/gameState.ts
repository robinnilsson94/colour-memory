import type {Card} from "./card";
import type {GameStatus} from "./gameStatus.ts";

export interface GameState {
    score: number;
    gameStatus: GameStatus;
    cardsPositions: { [key: number]: Card };
    flippedCards: Card[];
}