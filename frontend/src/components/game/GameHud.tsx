import { Clock3, Grid3X3, Lightbulb, Gauge, Activity } from "lucide-react";
import type { GameDifficulty, GameState } from "../../types/game";
import { formatDurationClock } from "../../utils/time";

type GameHudProps = {
  game: GameState;
  displayElapsedSeconds: number;
};

const difficultyClasses: Record<GameDifficulty, string> = {
  EASY: "text-green-500",
  NORMAL: "text-yellow-400",
  HARD: "text-rose-500",
};

function getStatusClass(status: string) {
  if (status === "SOLVED") {
    return "text-green-500";
  }

  if (status === "PLAYING") {
    return "text-sky-400";
  }

  return "text-[var(--text-color)]";
}

function HudItem({
  label,
  value,
  icon,
  valueClassName = "text-[var(--text-color)]",
}: {
  label: string;
  value: string | number;
  icon: React.ReactNode;
  valueClassName?: string;
}) {
  return (
    <div className='flex min-w-0 items-center gap-2'>
      <div className='flex h-8 w-8 shrink-0 items-center justify-center rounded-xl bg-white/[0.03] text-[var(--sub-color)]'>
        {icon}
      </div>

      <div className='min-w-0'>
        <p className='text-[10px] font-bold uppercase tracking-[0.05em] text-[var(--sub-color)]'>
          {label}
        </p>

        <p className={`truncate text-base font-bold text-sm ${valueClassName}`}>{value}</p>
      </div>
    </div>
  );
}

export function GameHud({ game, displayElapsedSeconds }: GameHudProps) {
  return (
    <div className='rounded-2xl border border-white/10 bg-[var(--bg-color)] px-4 py-3'>
      <div className='grid gap-3 sm:grid-cols-2 lg:grid-cols-5'>
        <HudItem
          label='Board'
          value={`${game.size}×${game.size}`}
          icon={<Grid3X3 className='h-4 w-4' />}
        />

        <HudItem
          label='Difficulty'
          value={game.difficulty}
          valueClassName={difficultyClasses[game.difficulty]}
          icon={<Gauge className='h-4 w-4' />}
        />

        <HudItem
          label='Status'
          value={game.state}
          valueClassName={getStatusClass(game.state)}
          icon={<Activity className='h-4 w-4' />}
        />

        <HudItem
          label='Time'
          value={formatDurationClock(displayElapsedSeconds)}
          icon={<Clock3 className='h-4 w-4' />}
        />

        <HudItem
          label='Hints'
          value={game.hintsLeft}
          valueClassName='text-[var(--accent-color)]'
          icon={<Lightbulb className='h-4 w-4' />}
        />
      </div>
    </div>
  );
}
