import { BOARD_SIZES, DIFFICULTIES } from "../utils/constants"

export type BoardSize = (typeof BOARD_SIZES)[number];

export type GameDifficulty = (typeof DIFFICULTIES)[number];

export type GameStateValue = "PLAYING" | "SOLVED" | "FAILED";

export type GameState = {
  gameId: string;
  size: number;
  board: number[][];
  fixedCells: boolean[][];
  regions: number[][];
  difficulty: GameDifficulty;
  state: GameStateValue;
  solvedAutomatically: boolean;
  lastMoveValid: boolean;
  message: string;
  hintsLeft: number;
  currentScore: number;
  elapsedSeconds: number;
  scoreSubmitted: boolean;
};

export type SelectedCell = {
  row: number;
  col: number;
}

export type CreateGameRequest = {
  size: BoardSize;
  difficulty: GameDifficulty;
}

export type MoveRequest = {
  row: number;
  col: number;
  value: number;
};