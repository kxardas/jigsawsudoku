import { useEffect } from "react";
import { X } from "lucide-react";
import { BOARD_SIZES, DIFFICULTIES } from "../../utils/constants";
import type { BoardSize, GameDifficulty } from "../../types/game";
import { AnimatePresence, motion } from "framer-motion";
import { Button } from "../ui/Button";

type NewGameModalProps = {
  isOpen: boolean;
  boardSize: BoardSize;
  difficulty: GameDifficulty;
  loading: boolean;
  onBoardSizeChange: (size: BoardSize) => void;
  onDifficultyChange: (difficulty: GameDifficulty) => void;
  onClose: () => void;
  onStart: () => void;
};

function getOptionClass(isActive: boolean) {
  return `border
    cursor-pointer rounded-xl px-4 py-2 text-sm font-bold transition
    disabled:cursor-not-allowed disabled:opacity-50
    ${
      isActive
        ? "border-solid border-fuchsia-400/30 bg-fuchsia-500/10 text-fuchsia-200 hover:bg-fuchsia-500/20 text-[var(--text-color)]"
        : "bg-[var(--bg-color)] text-[var(--sub-color)] hover:bg-[#222637] hover:text-[var(--text-color)] border-dashed border-white/10"
    }
  `;
}

function getDifficultyClass(difficulty: GameDifficulty, isActive: boolean) {
  const colorClasses: Record<GameDifficulty, string> = {
    EASY: isActive
      ? "bg-green-500 text-[#121520] shadow-[0_0_18px_rgba(34,197,94,0.25)] border-transparent"
      : "bg-green-500/10 text-green-300 hover:bg-green-500/20 border-green-900",

    NORMAL: isActive
      ? "bg-yellow-400 text-[#121520] shadow-[0_0_18px_rgba(250,204,21,0.25)] border-transparent"
      : "bg-yellow-400/10 text-yellow-300 hover:bg-yellow-400/20 border-yellow-900",

    HARD: isActive
      ? "bg-red-500 text-white shadow-[0_0_18px_rgba(239,68,68,0.25)] border-transparent"
      : "bg-red-500/10 text-red-300 hover:bg-red-500/20 border-red-900",
  };

  return `
    cursor-pointer rounded-xl px-4 py-2 text-sm font-bold transition
    disabled:cursor-not-allowed disabled:opacity-50
    ${colorClasses[difficulty]}
  `;
}

export function NewGameModal({
  isOpen,
  boardSize,
  difficulty,
  loading,
  onBoardSizeChange,
  onDifficultyChange,
  onClose,
  onStart,
}: NewGameModalProps) {
  useEffect(() => {
    if (!isOpen) return;

    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === "Escape" && !loading) {
        onClose();
      }
    }

    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [isOpen, loading, onClose]);

  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          className='fixed w-full h-full inset-0 z-50 flex items-center justify-center bg-black/70 px-4 backdrop-blur-sm'
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.18 }}
          onMouseDown={() => {
            if (!loading) onClose();
          }}
        >
          <motion.div
            className='w-full max-w-md rounded-2xl border border-white/10 bg-[var(--bg-color)] p-6 shadow-2xl'
            initial={{ opacity: 0, scale: 0.95, y: 12 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 1.1, y: -24 }}
            transition={{ duration: 0.2, ease: "easeOut" }}
            onMouseDown={(event) => event.stopPropagation()}
            role='dialog'
            aria-modal='true'
            aria-labelledby='new-game-title'
          >
            <div className='mb-6 flex items-start justify-between gap-4'>
              <div>
                <h2 id='new-game-title' className='text-2xl font-bold text-[var(--text-color)]'>
                  New Game
                </h2>

                <p className='mt-1 text-sm text-[var(--sub-color)]'>
                  Choose board size and difficulty before starting.
                </p>
              </div>

              <button
                type='button'
                disabled={loading}
                onClick={onClose}
                className='rounded-lg p-2 text-[var(--sub-color)] transition hover:bg-[var(--bg-color)] hover:text-[var(--text-color)] disabled:cursor-not-allowed disabled:opacity-50'
                aria-label='Close modal'
              >
                <X size={20} />
              </button>
            </div>

            <div className='space-y-5'>
              <section className='space-y-2'>
                <p className='text-sm font-semibold text-[var(--sub-color)]'>Board size</p>

                <div className='flex flex-wrap gap-2'>
                  {BOARD_SIZES.map((size) => {
                    const isActive = boardSize === size;

                    return (
                      <button
                        key={size}
                        type='button'
                        disabled={loading}
                        onClick={() => onBoardSizeChange(size)}
                        className={getOptionClass(isActive)}
                      >
                        {size}×{size}
                      </button>
                    );
                  })}
                </div>
              </section>

              <section className='space-y-2'>
                <p className='text-sm font-semibold text-[var(--sub-color)]'>Difficulty</p>

                <div className='flex flex-wrap gap-2'>
                  {DIFFICULTIES.map((item) => {
                    const isActive = difficulty === item;

                    return (
                      <button
                        key={item}
                        type='button'
                        disabled={loading}
                        onClick={() => onDifficultyChange(item)}
                        className={getDifficultyClass(item, isActive) + " border"}
                      >
                        {item}
                      </button>
                    );
                  })}
                </div>
              </section>
            </div>

            <div className='mt-7 flex flex-col-reverse gap-2 sm:flex-row sm:justify-end'>
              <Button type='button' variant='ghost' disabled={loading} onClick={onClose}>
                Cancel
              </Button>

              <Button type='button' disabled={loading} onClick={onStart}>
                {loading ? "Starting..." : "Start Game"}
              </Button>
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
