import type {Color} from "./color.ts";

export interface Card {
    id: string;
    color: Color;
    matched: boolean;
}