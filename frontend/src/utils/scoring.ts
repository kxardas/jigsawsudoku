import type { GameDifficulty } from "../types/game.ts"

export const SCORE_BY_DIFFICULTY: Record<GameDifficulty, number> = {
  EASY: 100,
  NORMAL: 200,
  HARD: 300,
};

export function calculateScore(difficulty: GameDifficulty): number {
  return SCORE_BY_DIFFICULTY[difficulty];
}