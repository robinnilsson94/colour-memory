import type {GameState} from "../models/gameState.ts";
import axios from "axios"

export async function startGame(): Promise<GameState> {
    const response = await axios.post("/game/start");

    return response.data;
}

export async function flipCard(position: number): Promise<GameState> {
    const response = await axios.post(`/game/flip/${position}`);

    return response.data;
}