import { Clock3 } from "lucide-react";
import { formatDurationClock } from "../../utils/time";

type GameTimerProps = {
  seconds: number;
};

export function GameTimer({ seconds }: GameTimerProps) {
  return (
    <div className='flex items-center gap-2 rounded-2xl border border-white/10 bg-[var(--bg-color)] px-4 py-2'>
      <Clock3 className='h-4 w-4 text-[var(--sub-color)]' />

      <div>
        <p className='text-[10px] font-bold uppercase tracking-[0.16em] text-[var(--sub-color)]'>
          Time
        </p>

        <p className='font-mono text-lg font-black leading-none text-[var(--text-color)]'>
          {formatDurationClock(seconds)}
        </p>
      </div>
    </div>
  );
}
