export const API_BASE_URL = import.meta.env.VITE_API_URL ?? "/api";
export const GAME_NAME = "jigsaw sudoku";
export const DEFAULT_PLAYER_NAME = "guest";
export const BOARD_SIZES = [5, 7, 9] as const;
export const DIFFICULTIES = ["EASY", "NORMAL", "HARD"] as const;
export const ACTIVE_GAME_ID_STORAGE_KEY = "jigsaw-sudoku-active-game-id";
