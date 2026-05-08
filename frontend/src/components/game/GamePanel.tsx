import type { GameState, SelectedCell } from "../../types/game";
import { Card } from "../ui/Card";
import { GameControls } from "./GameControls";
import { GameHud } from "./GameHud";
import { NumberPad } from "./NumberPad";
import { SudokuBoard } from "./SudokuBoard";

type GamePanelProps = {
  game: GameState;
  selectedCell: SelectedCell | null;
  loading: boolean;
  displayElapsedSeconds: number;
  onOpenNewGameModal: () => void;
  onSolve: () => void;
  onClearBoard: () => void;
  onHint: () => void;
  onSelectCell: (row: number, col: number) => void;
  onNumberClick: (value: number) => void;
  onClear: () => void;
  notes: number[][][];
  notesMode: boolean;
  onNotesModeChange: (value: boolean) => void;
};

export function GamePanel({
  game,
  selectedCell,
  loading,
  displayElapsedSeconds,
  notes,
  notesMode,
  onOpenNewGameModal,
  onSolve,
  onClearBoard,
  onHint,
  onSelectCell,
  onNumberClick,
  onClear,
  onNotesModeChange,
}: GamePanelProps) {
  return (
    <Card className='space-y-6'>
      <div className='flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between'>
        <div>
          <h2 className='text-2xl font-black text-[var(--text-color)]'>Game</h2>
          <p className='mt-1 text-sm text-[var(--sub-color)]'>Select a cell and place a number.</p>
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

      <GameHud game={game} displayElapsedSeconds={displayElapsedSeconds} />

      <div className='flex justify-center pb-2'>
        <div className='flex items-center gap-4 flex-col sm:flex-row'>
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
        </div>
      </div>
    </Card>
  );
}
