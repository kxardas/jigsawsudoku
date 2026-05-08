import { useId } from "react";
import type { GameState, SelectedCell } from "../../types/game";
import { SudokuCell } from "./SudokuCell";
import {
  CELL_SIZE,
  REGION_STROKE_WIDTH,
  getRegionCellPresentation,
  getRegionOutlines,
} from "../../utils/sudokuBoardGeometry";

type SudokuBoardProps = {
  game: GameState;
  selectedCell: SelectedCell | null;
  notes: number[][][];
  onSelectCell: (row: number, col: number) => void;
};

export function SudokuBoard({ game, selectedCell, notes, onSelectCell }: SudokuBoardProps) {
  const boardSize = game.size * CELL_SIZE;
  const regionOutlines = getRegionOutlines(game);
  const clipPathId = useId().replace(/:/g, "");

  return (
    <div className='relative w-fit scale-90 origin-bottom sm:scale-100'>
      <div className='relative rounded-[1.75rem] border border-[var(--border-color)] p-3 shadow-[inset_0_0_28px_rgba(255,255,255,0.025),0_22px_60px_rgba(0,0,0,0.24)]'>
        <svg
          className='block overflow-visible stroke-gray-700/75'
          height={boardSize}
          viewBox={`0 0 ${boardSize} ${boardSize}`}
          width={boardSize}
        >
          <defs>
            <clipPath id={clipPathId} clipPathUnits='userSpaceOnUse'>
              {regionOutlines.map((outline) => (
                <path
                  key={`clip-${outline.region}-${outline.path}`}
                  d={outline.path}
                  fill='white'
                />
              ))}
            </clipPath>
          </defs>

          <foreignObject
            clipPath={`url(#${clipPathId})`}
            height={boardSize}
            width={boardSize}
            x={0}
            y={0}
          >
            <div
              className='grid w-fit gap-0'
              style={{
                gridTemplateColumns: `repeat(${game.size}, ${CELL_SIZE}px)`,
              }}
            >
              {game.board.map((row, rowIndex) =>
                row.map((value, colIndex) => {
                  const isFixed = game.fixedCells[rowIndex][colIndex];
                  const isSelected =
                    selectedCell?.row === rowIndex && selectedCell?.col === colIndex;
                  const regionCell = getRegionCellPresentation(game, rowIndex, colIndex);

                  return (
                    <SudokuCell
                      key={`${rowIndex}-${colIndex}`}
                      value={value}
                      isFixed={isFixed}
                      isSelected={isSelected}
                      style={regionCell.style}
                      onClick={() => onSelectCell(rowIndex, colIndex)}
                      notes={notes[rowIndex]?.[colIndex] ?? []}
                      size={game.size}
                    />
                  );
                }),
              )}
            </div>
          </foreignObject>

          {regionOutlines.map((outline) => (
            <path
              key={`${outline.region}-${outline.path}`}
              d={outline.path}
              fill='none'
              strokeLinecap='round'
              strokeLinejoin='round'
              strokeWidth={REGION_STROKE_WIDTH}
            />
          ))}
        </svg>
      </div>
    </div>
  );
}
