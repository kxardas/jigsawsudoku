import { Button } from "../ui/Button";
import { Eraser, Pen } from "lucide-react";

type NumberPadProps = {
  size: number;
  disabled?: boolean;
  notesMode: boolean;
  onNumberClick: (value: number) => void;
  onClear: () => void;
  onNotesModeChange: (value: boolean) => void;
};

export function NumberPad({
  size,
  disabled = false,
  onNumberClick,
  onClear,
  notesMode,
  onNotesModeChange,
}: NumberPadProps) {
  const numbers = Array.from({ length: size }, (_, index) => index + 1);

  return (
    <div className='bg-[var(--sub-alt-color)] border border-white/10 rounded-2xl w-full flex flex-wrap justify-between p-2 sm:flex-col sm:flex-nowrap sm:h-full'>
      {numbers.map((number) => (
        <Button
          key={number}
          type='button'
          variant='number_pad'
          disabled={disabled}
          onClick={() => onNumberClick(number)}
          className='flex h-9 w-9 items-center justify-center px-0 text-base font-medium sm:h-9.5 sm:w-9.5 sm:text-lg'
          aria-label={`Place number ${number}`}
        >
          {number}
        </Button>
      ))}

      <div className="bg-white/10 w-[1px] h-full sm:w-full sm:h-[1px] sm:my-2" />

      <Button
        type='button'
        variant='ghost'
        disabled={disabled}
        onClick={onClear}
        className='flex h-9 w-9 sm:h-9.5 sm:w-9.5 justify-center items-center px-0 font-semibold'
        aria-label='Clear selected cell'
        title='Clear'
      >
        <Eraser className='absolute h-4 w-4' />
      </Button>

      <Button
        type='button'
        variant={notesMode ? "ghost_on" : "ghost"}
        disabled={disabled}
        onClick={() => onNotesModeChange(!notesMode)}
        className='flex h-9 w-9 sm:h-9.5 sm:w-9.5 justify-center items-center px-0 font-semibold'
        aria-label='Turn note mode on'
        title='Note'
      >
        <Pen className='absolute h-4 w-4' />
      </Button>
    </div>
  );
}
