import type { GameState } from "../../types/game";
import { formatDurationClock } from "../../utils/time";

type GameStatusProps = {
  game: GameState;
  displayElapsedSeconds: number;
};

function getDifficultyClass(difficulty: GameState["difficulty"]) {
  switch (difficulty) {
    case "EASY":
      return "text-green-500 dark:text-green-500";
    case "NORMAL":
      return "text-amber-500 dark:text-yellow-500";
    case "HARD":
      return "text-red-700 dark:text-red-500";
    default:
      return "text-[var(--text-color)]";
  }
}

function getStateClass(state: GameState["state"]) {
  switch (state) {
    case "SOLVED":
      return "text-teal-600 dark:text-teal-400";
    case "FAILED":
      return "text-red-700 dark:text-red-400";
    case "PLAYING":
      return "text-blue-600 dark:text-blue-400";
    default:
      return "text-[var(--text-color)]";
  }
}

const CARD =
  "flex flex-col gap-1.5 rounded-xl border border-white/10 bg-[var(--bg-color)] px-4 py-3";
const LABEL =
  "flex items-center gap-1.5 text-[10px] font-semibold uppercase tracking-widest text-[var(--sub-color)]";
const VALUE = "text-2xl font-bold text-[var(--text-color)]";

export function GameStatus({ game, displayElapsedSeconds }: GameStatusProps) {
  return (
    <div className='space-y-2.5'>
      <div className='grid grid-cols-2 gap-2.5 sm:grid-cols-3 xl:grid-cols-5'>
        {/* Board */}
        <div className={CARD}>
          <p className={LABEL}>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              width='14'
              height='14'
              viewBox='0 0 24 24'
              fill='none'
              stroke='currentColor'
              strokeWidth='2'
              strokeLinecap='round'
              strokeLinejoin='round'
            >
              <rect x='3' y='3' width='7' height='7' />
              <rect x='14' y='3' width='7' height='7' />
              <rect x='3' y='14' width='7' height='7' />
              <rect x='14' y='14' width='7' height='7' />
            </svg>
            Board
          </p>
          <p className={VALUE}>
            {game.size}×{game.size}
          </p>
        </div>

        {/* Difficulty */}
        <div className={CARD}>
          <p className={LABEL}>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              width='14'
              height='14'
              viewBox='0 0 24 24'
              fill='none'
              stroke='currentColor'
              strokeWidth='2'
              strokeLinecap='round'
              strokeLinejoin='round'
            >
              <path d='M8.5 14.5A2.5 2.5 0 0 0 11 12c0-1.38-.5-2-1-3-1.072-2.143-.224-4.054 2-6 .5 2.5 2 4.9 4 6.5 2 1.6 3 3.5 3 5.5a7 7 0 1 1-14 0c0-1.153.433-2.294 1-3a2.5 2.5 0 0 0 2.5 2.5z' />
            </svg>
            Difficulty
          </p>
          <p className={`${VALUE} ${getDifficultyClass(game.difficulty)}`}>{game.difficulty}</p>
        </div>

        {/* Status */}
        <div className={CARD}>
          <p className={LABEL}>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              width='14'
              height='14'
              viewBox='0 0 24 24'
              fill='none'
              stroke='currentColor'
              strokeWidth='2'
              strokeLinecap='round'
              strokeLinejoin='round'
            >
              <polyline points='22 12 18 12 15 21 9 3 6 12 2 12' />
            </svg>
            Status
          </p>
          <p className={`${VALUE} ${getStateClass(game.state)}`}>{game.state}</p>
        </div>

        {/* Time */}
        <div className={CARD}>
          <p className={LABEL}>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              width='14'
              height='14'
              viewBox='0 0 24 24'
              fill='none'
              stroke='currentColor'
              strokeWidth='2'
              strokeLinecap='round'
              strokeLinejoin='round'
            >
              <circle cx='12' cy='12' r='10' />
              <polyline points='12 6 12 12 16 14' />
            </svg>
            Time
          </p>
          <p className={VALUE}>{formatDurationClock(displayElapsedSeconds)}</p>
        </div>

        {/* Hints */}
        <div className={CARD}>
          <p className={LABEL}>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              width='14'
              height='14'
              viewBox='0 0 24 24'
              fill='none'
              stroke='currentColor'
              strokeWidth='2'
              strokeLinecap='round'
              strokeLinejoin='round'
            >
              <path d='M15 14c.2-1 .7-1.7 1.5-2.5 1-.9 1.5-2.2 1.5-3.5A6 6 0 0 0 6 8c0 1 .2 2.2 1.5 3.5.7.7 1.3 1.5 1.5 2.5' />
              <path d='M9 18h6' />
              <path d='M10 22h4' />
            </svg>
            Hints
          </p>
          <p className={`${VALUE} text-[var(--accent-color)]`}>{game.hintsLeft}</p>
        </div>
      </div>

      {game.solvedAutomatically && (
        <div className='flex items-center gap-2 rounded-xl border border-amber-400/20 bg-amber-400/10 px-4 py-2.5 text-sm text-amber-300'>
          <span className='h-2 w-2 rounded-full bg-amber-400' />
          Solved automatically
        </div>
      )}
    </div>
  );
}
