import { AnimatePresence, motion } from "framer-motion";
import { LockKeyhole, Send, Trophy, X } from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import type { GameDifficulty, GameState } from "../../types/game";
import { formatDurationString } from "../../utils/time";
import { Button } from "../ui/Button";
import CountUp from "../ui/CountUp";

type VictoryModalProps = {
  isOpen: boolean;
  game: GameState | null;
  submitting?: boolean;
  submitted?: boolean;
  onClose: () => void;
  onSubmitScore: () => void;
  onOpenAuthModal: () => void;
};

const MAX_HINTS = 3;

const difficultyClasses: Record<GameDifficulty, string> = {
  EASY: "text-green-500",
  NORMAL: "text-yellow-400",
  HARD: "text-red-500",
};

export function VictoryModal({
  isOpen,
  game,
  submitting = false,
  submitted = false,
  onClose,
  onSubmitScore,
  onOpenAuthModal,
}: VictoryModalProps) {
  const { isAuthenticated } = useAuth();

  if (!game) {
    return null;
  }

  const hintsUsed = Math.max(0, MAX_HINTS - game.hintsLeft);
  const isScoreAlreadySubmitted = submitted || game.scoreSubmitted;
  const canSubmitScore = isAuthenticated && !game.solvedAutomatically && !isScoreAlreadySubmitted;

  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          className='fixed inset-0 z-50 flex items-center justify-center bg-black/70 px-4 backdrop-blur-md'
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.18 }}
          onMouseDown={onClose}
        >
          <motion.div
            className='relative w-full max-w-md'
            initial={{ opacity: 0, y: 18, scale: 0.96 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 18, scale: 0.96 }}
            transition={{ duration: 0.22, ease: "easeOut" }}
            onMouseDown={(event) => event.stopPropagation()}
          >
            <div className='relative overflow-hidden rounded-2xl border border-white/10 bg-[var(--sub-alt-color)] p-5 shadow-2xl'>
              <button
                type='button'
                onClick={onClose}
                className='absolute right-4 top-4 z-10 rounded-xl p-2 text-[var(--sub-color)] transition hover:bg-[var(--sub-alt-color)] hover:text-[var(--text-color)]'
                aria-label='Close victory modal'
              >
                <X className='h-5 w-5' />
              </button>

              <div className='text-center'>
                <div className='mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-[var(--accent-color)]/15 text-[var(--accent-color)]'>
                  <Trophy className='h-8 w-8' />
                </div>

                <p className='mt-4 text-xs font-bold uppercase tracking-[0.24em] text-[var(--sub-color)]'>
                  Puzzle completed
                </p>

                <h2 className='mt-2 text-3xl font-black text-[var(--text-color)]'>Nice solve!</h2>

                <p className='mt-2 text-sm text-[var(--sub-color)]'>
                  {game.size}×{game.size}{" "}
                  <span className={difficultyClasses[game.difficulty]}>
                    {game.difficulty.toLowerCase()}
                  </span>{" "}
                  puzzle completed.
                </p>
              </div>

              <div className='mt-5 rounded-2xl border border-white/10 bg-[var(--sub-alt-color)] px-4 py-3 text-center'>
                <p className='text-xs font-bold uppercase tracking-[0.18em] text-[var(--sub-color)]'>
                  Score
                </p>

                <CountUp
                  className='mt-1 block text-4xl font-black text-[var(--accent-color)]'
                  from={0}
                  to={game.currentScore}
                  duration={0.55}
                />
              </div>

              <div className='mt-3 grid grid-cols-3 gap-2'>
                <div className='rounded-xl border border-white/10 bg-[var(--sub-alt-color)] px-3 py-2 text-center'>
                  <p className='text-[10px] font-bold uppercase tracking-[0.12em] text-[var(--sub-color)]'>
                    Time
                  </p>
                  <p className='mt-1 text-sm font-black text-[var(--text-color)]'>
                    {formatDurationString(game.elapsedSeconds)}
                  </p>
                </div>

                <div className='rounded-xl border border-white/10 bg-[var(--sub-alt-color)] px-3 py-2 text-center'>
                  <p className='text-[10px] font-bold uppercase tracking-[0.12em] text-[var(--sub-color)]'>
                    Hints
                  </p>
                  <p className='mt-1 text-sm font-black text-[var(--text-color)]'>
                    {hintsUsed}/{MAX_HINTS}
                  </p>
                </div>

                <div className='rounded-xl border border-white/10 bg-[var(--sub-alt-color)] px-3 py-2 text-center'>
                  <p className='text-[10px] font-bold uppercase tracking-[0.12em] text-[var(--sub-color)]'>
                    Mode
                  </p>
                  <p className={`mt-1 text-sm font-black ${difficultyClasses[game.difficulty]}`}>
                    {game.difficulty}
                  </p>
                </div>
              </div>

              {game.solvedAutomatically && (
                <p className='mt-3 rounded-xl border border-amber-400/20 bg-amber-400/10 px-3 py-2 text-center text-xs text-amber-200'>
                  Auto-solved puzzles cannot be submitted.
                </p>
              )}

              <div className='mt-5 space-y-2'>
                {isAuthenticated ? (
                  <Button
                    type='button'
                    variant='success'
                    disabled={submitting || !canSubmitScore}
                    onClick={onSubmitScore}
                    className='w-full'
                  >
                    {isScoreAlreadySubmitted ? (
                      "Score submitted"
                    ) : submitting ? (
                      "Submitting..."
                    ) : (
                      <>
                        <Send className='h-4 w-4' />
                        Submit score
                      </>
                    )}
                  </Button>
                ) : (
                  <Button
                    type='button'
                    onClick={onOpenAuthModal}
                    className='w-full'
                  >
                    <LockKeyhole className='h-4 w-4' />
                    Sign in to submit score
                  </Button>
                )}

                <Button type='button' variant='ghost' onClick={onClose} className='w-full'>
                  Back to board
                </Button>
              </div>
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
