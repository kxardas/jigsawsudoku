import { Button } from "../ui/Button";

type AppErrorStateProps = {
  error: string;
  onStartNewGame: () => void;
};

export function AppErrorState({ error, onStartNewGame }: AppErrorStateProps) {
  return (
    <div className='rounded-2xl border border-red-500/30 bg-red-500/10 p-5'>
      <p className='font-semibold text-red-300'>Could not load the game.</p>
      <p className='mt-1 text-sm text-[var(--sub-color)]'>{error}</p>

      <Button type='button' className='mt-4' onClick={onStartNewGame}>
        Start New Game
      </Button>
    </div>
  );
}
