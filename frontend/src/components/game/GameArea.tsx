import type { GameState, SelectedCell } from "../../types/game";
import { NumberPad } from "./NumberPad";
import { SudokuBoard } from "./SudokuBoard";

type GameAreaProps = {
  game: GameState;
  selectedCell: SelectedCell | null;
  onSelectCell: (row: number, col: number) => void;
  onNumberClick: (value: number) => void;
  onClear: () => void;
};

export function GameArea({
  game,
  selectedCell,
  onSelectCell,
  onNumberClick,
  onClear,
}: GameAreaProps) {
  return (
    <div className="flex flex-col items-start gap-7 sm:flex-row">
      <SudokuBoard 
        game={game}
        selectedCell={selectedCell}
        onSelectCell={onSelectCell}
      />

      <NumberPad 
        size={game.size}
        disabled={!selectedCell || game.state !== "PLAYING"}
        onNumberClick={onNumberClick}
        onClear={onClear}
      />
    </div>
  );
}
