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

    if (!started) {
        return (
            <div className="center-align">
                <div className="center-align">
                    <img src="/resources/colourMemory.png" alt="Colour Memory" className="small-image"/>
                </div>
                <button onClick={handleStartGame}>Start</button>
            </div>
        );
    }

    if (!gameState){
        return <div>Loading</div>;
    }

    return (
        <div className="center-align">
            <div className="center-align">
                <img src="/resources/colourMemory.png" alt="Colour Memory" className="small-image"/>
            </div>
            <button onClick={handleStartGame}>Restart</button>
            <h3>Score: {gameState.score}</h3>

            <GameBoard gameState={gameState} onFlipCard={handleFlipCard}/>

            {gameState.gameStatus === GameStatus.FINISHED && (
                <>
                    <div className="greyed-out-background"/>
                    <div className="game-finished-modal">
                        <h2>Game finished!</h2>
                        <p>Your score: {gameState.score}</p>
                        <button onClick={handleStartGame}>Start a new game</button>
                    </div>
                </>
            )}
        </div>
    );
};

export default App;
