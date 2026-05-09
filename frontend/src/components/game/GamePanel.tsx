import type { GameState, SelectedCell } from "../../types/game";
import { Card } from "../ui/Card";
import { GameBoardSkeleton } from "../ui/GameBoardSkeleton";
import { Reveal } from "../ui/Reveal";
import { GameControls } from "./GameControls";
import { GameStatus } from "./GameStatus";
import { NumberPad } from "./NumberPad";
import { SudokuBoard } from "./SudokuBoard";

type GamePanelProps = {
  game: GameState;
  selectedCell: SelectedCell | null;
  loading: boolean;
  displayElapsedSeconds: number;
  notes: number[][][];
  notesMode: boolean;

  onNotesModeChange: (value: boolean) => void;
  onOpenNewGameModal: () => void;
  onSolve: () => void;
  onClearBoard: () => void;
  onHint: () => void;
  onSelectCell: (row: number, col: number) => void;
  onNumberClick: (value: number) => void;
  onClear: () => void;
};

export function GamePanel({
  game,
  selectedCell,
  loading,
  displayElapsedSeconds,
  notes,
  notesMode,
  onNotesModeChange,
  onOpenNewGameModal,
  onSolve,
  onClearBoard,
  onHint,
  onSelectCell,
  onNumberClick,
  onClear,
}: GamePanelProps) {
  const boardSize = game?.size;

  return (
    <Card className='space-y-6'>
      <div className='flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between'>
        <div>
          <h2 className='text-2xl font-bold text-[var(--text-color)]'>Game</h2>

          <p className='text-sm text-[var(--sub-color)]'>Select a cell and place a number.</p>
        </div>

        <GameControls
          loading={loading}
          hintsLeft={game.hintsLeft}
          onOpenNewGameModal={onOpenNewGameModal}
          onHint={onHint}
          onClearBoard={onClearBoard}
          onSolve={onSolve}
        />
      </div>

      <GameStatus game={game} displayElapsedSeconds={displayElapsedSeconds} />

      <div className='flex justify-center pb-2'>
        <Reveal className='flex items-center gap-2 flex-col sm:flex-row'>
          {loading && !game && <GameBoardSkeleton size={boardSize} />}

          <SudokuBoard
            game={game}
            selectedCell={selectedCell}
            onSelectCell={onSelectCell}
            notes={notes}
          />

          <NumberPad
            size={game.size}
            disabled={loading || game.state !== "PLAYING"}
            onNumberClick={onNumberClick}
            onClear={onClear}
            notesMode={notesMode}
            onNotesModeChange={onNotesModeChange}
          />
        </Reveal>
      </div>
    </Card>
  );
}
