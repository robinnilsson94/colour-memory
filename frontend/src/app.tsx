import React, {useState} from "react";
import GameBoard from "./components/gameBoard/gameBoard";
import type {GameState} from "./models/gameState";
import {flipCard, startGame} from "./api/gameApi";
import {GameStatus} from "./models/gameStatus.ts";
import "./style.css";

const App: React.FC = () => {
    const [gameState, setGameState] = useState<GameState | null>(null);
    const [started, setStarted] = useState(false);

    const handleStartGame = async () => {
        const state = await startGame();
        setGameState(state);
        setStarted(true);
    };

    const handleFlipCard = async (position: number): Promise<GameState> => {
        if (!gameState) {
            throw new Error("Game not started");
        }
        const updated = await flipCard(position);
        setGameState(updated);
        return updated;
    };

    const formatAccuracy = (accuracy: number | null): string => {
        return accuracy != null ? (accuracy * 100).toFixed(1) : '0';
    };

    if (!started) {
        return (
            <div className="center-align">
                <div className="center-align">
                    <img src="/colourMemory.png" alt="Colour Memory" className="small-image"/>
                </div>
                <button onClick={handleStartGame}>Start</button>
            </div>
        );
    }

    if (!gameState) {
        return <div>Loading</div>;
    }

    return (
        <div className="center-align">
            <div className="center-align">
                <img src="/colourMemory.png" alt="Colour Memory" className="small-image"/>
            </div>
            <button onClick={handleStartGame}>Restart</button>

            <div className="game-stats">
                <h3>Score: {gameState.score}</h3>
                <h4>Flips: {gameState.moveCount}</h4>
                <h4>Accuracy: {formatAccuracy(gameState.accuracy)}%</h4>
            </div>

            <GameBoard gameState={gameState} onFlipCard={handleFlipCard}/>

            {gameState.gameStatus === GameStatus.FINISHED && (
                <>
                    <div className="greyed-out-background"/>
                    <div className="game-finished-modal">
                        <h2>Game finished!</h2>
                        <p>Your score: {gameState.score}</p>
                        <p>Flips: {gameState.moveCount}</p>
                        <p>Accuracy: {formatAccuracy(gameState.accuracy)}%</p>
                        <button onClick={handleStartGame}>Start a new game</button>
                    </div>
                </>
            )}
        </div>
    );
};

export default App;
