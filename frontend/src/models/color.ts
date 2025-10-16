export const Color = {
    RED: "RED",
    GREEN: "GREEN",
    BLUE: "BLUE",
    YELLOW: "YELLOW",
    BROWN: "BROWN",
    PURPLE: "PURPLE",
    WHITE: "WHITE",
    BLACK: "BLACK"
} as const;

export type Color = typeof Color[keyof typeof Color];
