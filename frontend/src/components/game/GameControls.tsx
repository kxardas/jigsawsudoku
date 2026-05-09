import { Lightbulb, RotateCcw, Sparkles } from "lucide-react";
import { Button } from "../ui/Button";

type GameControlsProps = {
  loading: boolean;
  hintsLeft: number;
  onOpenNewGameModal: () => void;
  onHint: () => void;
  onClearBoard: () => void;
  onSolve: () => void;
};

export function GameControls({
  loading,
  onOpenNewGameModal,
  onHint,
  onSolve,
  onClearBoard,
}: GameControlsProps) {
  return (
    <div className='flex flex-wrap gap-2'>
      <Button type='button' disabled={loading} onClick={onOpenNewGameModal} className='h-11'>
        New Game
      </Button>

      <Button
        type='button'
        variant='success'
        disabled={loading}
        onClick={onHint}
        className='h-11'
      >
        <Lightbulb className='h-4 w-4' />
      </Button>

      <Button type='button' variant='ghost' disabled={loading} onClick={onClearBoard} className='h-11'>
        <RotateCcw className='h-4 w-4' />
      </Button>

      <Button type='button' variant='success' disabled={loading} onClick={onSolve} className='h-11'>
        <Sparkles className="h-4 w-4" />
      </Button>
    </div>
  );
}
