import React, {useEffect, useState} from "react";
import "./GameBoard.css";
import type {GameState} from "../../models/gameState";
import CardComponent from "../cardComponent/cardComponent";

interface GameBoardProps {
    gameState: GameState;
    onFlipCard: (position: number) => Promise<GameState>;
}

const GameBoard: React.FC<GameBoardProps> = ({gameState, onFlipCard}) => {
    const [flippedPositions, setFlippedPositions] = useState<number[]>([]);
    const [isLocked, setIsLocked] = useState(false);
    const [temporarilyVisibleIds, setTemporarilyVisibleIds] = useState<Set<string>>(new Set());
    const [removedCardIds, setRemovedCardIds] = useState<Set<string>>(new Set());

    const handleFlip = async (position: number) => {
        if (isLocked || flippedPositions.includes(position)) {
            return;
        }

        const updatedFlippedPositions = [...flippedPositions, position];
        setFlippedPositions(updatedFlippedPositions);

        await onFlipCard(position);

        if (updatedFlippedPositions.length === 2) {
            setIsLocked(true);
            setTimeout(() => {
                setFlippedPositions([]);
                setIsLocked(false);
            }, 2000);
        }
    };

    useEffect(() => {
        const newlyMatched = Object.values(gameState.cardsPositions)
            .filter(card => card.matched && !removedCardIds.has(card.id));

        if (newlyMatched.length > 0) {
            const matchedIds = newlyMatched.map(c => c.id);
            setTemporarilyVisibleIds(temporarilyVisible => new Set([...temporarilyVisible, ...matchedIds]));

            const timer = setTimeout(() => {
                setRemovedCardIds(alreadyRemoved => new Set([...alreadyRemoved, ...matchedIds]));
                setTemporarilyVisibleIds(temporarilyVisible => new Set([...temporarilyVisible].filter(id => !matchedIds.includes(id))));
            }, 2000);
            return () => clearTimeout(timer);
        }
    }, [gameState.cardsPositions]);

    return (
        <div className={`game-board ${isLocked ? "locked" : ""}`}>
            {Object.entries(gameState.cardsPositions).map(([position, card]) => {
                const positionNumber = Number(position);

                const isRemoved = removedCardIds.has(card.id);
                const isFlipped = flippedPositions.includes(positionNumber) || temporarilyVisibleIds.has(card.id);

                return isRemoved ? (
                    <div key={card.id}/>
                ) : (
                    <CardComponent
                        key={card.id}
                        card={card}
                        isFlipped={isFlipped}
                        onClick={() => handleFlip(positionNumber)}
                    />
                );
            })}
        </div>
    );
};

export default GameBoard;
