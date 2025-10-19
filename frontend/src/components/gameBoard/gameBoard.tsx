import React, {useEffect, useState} from "react";
import "./GameBoard.css";
import type {GameState} from "../../models/gameState";
import CardComponent from "../cardComponent/cardComponent";

interface GameBoardProps {
    gameState: GameState;
    onFlipCard: (position: number) => Promise<GameState>;
}

const GameBoard: React.FC<GameBoardProps> = ({gameState, onFlipCard}) => {
    const [flipped, setFlipped] = useState<number[]>([]);
    const [isLocked, setIsLocked] = useState(false);
    const [temporarilyVisible, setTemporarilyVisible] = useState<Set<string>>(new Set());
    const [removedCards, setRemovedCards] = useState<Set<string>>(new Set());

    const handleFlip = async (position: number) => {
        if (isLocked || flipped.includes(position)){
            return;
        }

        await onFlipCard(position);
        setFlipped(prev => [...prev, position]);

        if (flipped.length === 1) {
            setIsLocked(true);
            setTimeout(() => {
                setFlipped([]);
                setIsLocked(false);
            }, 2000);
        }
    };

    useEffect(() => {
        const newlyMatched = Object.values(gameState.cardsPositions)
            .filter(card => card.matched && !removedCards.has(card.id));

        if (newlyMatched.length > 0) {
            const newTemp = new Set(temporarilyVisible);
            newlyMatched.forEach(c => newTemp.add(c.id));
            setTemporarilyVisible(newTemp);

            setTimeout(() => {
                setRemovedCards(prev => {
                    const next = new Set(prev);
                    newlyMatched.forEach(c => next.add(c.id));
                    return next;
                });
                setTemporarilyVisible(prev => {
                    const next = new Set(prev);
                    newlyMatched.forEach(c => next.delete(c.id));
                    return next;
                });
            }, 2000);
        }
    }, [gameState]);

    return (
        <div className={`game-board ${isLocked ? "locked" : ""}`}>
            {Object.entries(gameState.cardsPositions).map(([position, card]) => {
                const pos = Number(position);

                const isRemoved = removedCards.has(card.id);
                const isFlipped = flipped.includes(pos) || temporarilyVisible.has(card.id);

                return isRemoved ? (
                    <div key={card.id}/>
                ) : (
                    <CardComponent
                        key={card.id}
                        card={card}
                        isFlipped={isFlipped}
                        onClick={() => handleFlip(pos)}
                    />
                );
            })}
        </div>
    );
};

export default GameBoard;
