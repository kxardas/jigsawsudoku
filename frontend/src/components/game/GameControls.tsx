import { Lightbulb, RotateCcw } from "lucide-react";
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
  hintsLeft,
  onOpenNewGameModal,
  onHint,
  onClearBoard,
}: GameControlsProps) {
  return (
    <div className='flex flex-wrap gap-2'>
      <Button type='button' disabled={loading} onClick={onOpenNewGameModal} className='h-11'>
        New Game
      </Button>

      {/* <Button type='button' variant='success' disabled={loading} onClick={onSolve} className='h-11'>
        Solve
      </Button> */}

      <Button
        type='button'
        variant='success'
        disabled={loading || hintsLeft <= 0}
        onClick={onHint}
        className='h-11'
      >
        <Lightbulb className='h-4 w-4' />
      </Button>

      <Button type='button' variant='ghost' disabled={loading} onClick={onClearBoard} className='h-11'>
        <RotateCcw className='h-4 w-4' />
      </Button>

    </div>
  );
}
