import { useEffect } from "react";
import { useLeaderboard } from "../../hooks/useLeaderboard";
import { Card } from "../ui/Card";
import { ErrorMessage } from "../ui/ErrorMessage";
import { LeaderboardSkeleton } from "./LeaderboardSkeleton";
import { LeaderboardTable } from "./LeaderboardTable";

type LeaderboardPanelProps = {
  reloadKey?: number;
}

export function LeaderboardPanel({ reloadKey = 0 }: LeaderboardPanelProps) {
  const { scores, loading, error, reload } = useLeaderboard();

  useEffect(() => {
    reload();
  }, [reloadKey]);
  
  return (
    <Card className='space-y-4'>
      <div>
        <h2 className='text-2xl font-bold text-[var(--text-color)]'>Leaderboard</h2>

        <p className='mt-1 text-sm text-[var(--sub-color)]'>Best players by score.</p>
      </div>

      {error && <ErrorMessage message={error} />}

      {loading ? (
        <LeaderboardSkeleton />
      ) : (
        <div className='max-h-[471px] overflow-y-auto pr-1 custom-scrollbar'>
          <LeaderboardTable scores={scores} />
        </div>
      )}
    </Card>
  );
}
