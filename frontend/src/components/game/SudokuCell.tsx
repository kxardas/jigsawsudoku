import { Dot } from "lucide-react";
import type { CSSProperties } from "react";

type SudokuCellProps = {
  value: number;
  size: number;
  isFixed: boolean;
  isSelected: boolean;
  className?: string;
  notes?: number[];
  onClick: () => void;
  style?: CSSProperties;
};

export function SudokuCell({
  value,
  size,
  isFixed,
  isSelected,
  notes = [],
  className = "",
  onClick,
  style = {},
}: SudokuCellProps) {
  const hasNotes = value === 0 && notes.length > 0;
  const notesColumns = Math.ceil(Math.sqrt(size));

  return (
    <button
      onClick={onClick}
      disabled={isFixed}
      className={`
        group relative flex h-15.5 w-15.5
        items-center justify-center
        overflow-hidden
        border-0
        bg-transparent
        text-xl font-normal
        outline-none
        transition-all duration-200
        ${
          isFixed
            ? "cursor-default text-[var(--text-color)]/50"
            : "cursor-pointer text-[var(--text-color)]"
        }
        ${className}
      `}
    >
      <span
        className={`
          flex h-full w-full items-center justify-center
          ${
            isSelected
              ? "bg-white/[0.055] text-white shadow-[0_0_30px_rgba(255,255,255,0.16),inset_0_0_18px_rgba(255,255,255,0.07)]"
              : "group-hover:bg-white/[0.035]"
          }
          ${
            !isSelected && !isFixed
              ? "group-hover:shadow-[inset_0_0_14px_rgba(255,255,255,0.045)]"
              : ""
          }
        `}
        style={style}
      >
        {value !== 0 ? (
          <span className='drop-shadow-[0_0_10px_rgba(255,255,255,0.18)]'>{value}</span>
        ) : hasNotes ? (
          <div
            className='grid h-full w-full content-start justify-start gap-[2px] p-1 text-[9px] font-black leading-none text-[var(--sub-color)]'
            style={{
              gridTemplateColumns: `repeat(${notesColumns}, minmax(0, auto))`,
            }}
          >
            {notes
              .slice()
              .sort((a, b) => a - b)
              .map((noteValue) => (
                <span
                  key={noteValue}
                  className='flex h-3.5 w-3.5 items-center justify-center rounded-sm text-white/80'
                >
                  {noteValue}
                </span>
              ))}
          </div>
        ) : (
          <Dot className='h-5 w-5 opacity-20 transition-opacity group-hover:opacity-50' />
        )}
      </span>
    </button>
  );
}
