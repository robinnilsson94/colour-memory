export const GameStatus = {
    NOT_STARTED: "NOT_STARTED",
    IN_PROGRESS: "IN_PROGRESS",
    FINISHED: "FINISHED"
} as const;

export type GameStatus = typeof GameStatus[keyof typeof GameStatus];