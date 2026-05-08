import type { Score } from "../../types/score";
import { formatDate } from "../../utils/date";
import { EmptyState } from "../ui/EmptyState";

type LeaderboardTableProps = {
  scores: Score[];
};

export function LeaderboardTable({ scores }: LeaderboardTableProps) {
  if (scores.length === 0) {
    return (
      <EmptyState
        title="No scores yet."
        description="Leaderboard will appear here after players submit scores."
      />
    )
  }

  return (
    <div className='overflow-hidden rounded-xl border border-white/10'>
      <table className='w-full text-left text-sm'>
        <thead className='bg-[var(--bg-color)] text-[var(--sub-color)]'>
          <tr>
            <th className='px-4 py-3'>#</th>
            <th className='px-4 py-3'>Player</th>
            <th className='px-4 py-3'>Points</th>
            <th className='px-4 py-3'>Date</th>
          </tr>
        </thead>

        <tbody>
          {scores.map((score, index) => (
            <tr
              key={score.ident ?? `${score.player}-${score.playedOn}-${index}`}
              className='border-t border-white/10 text-[var(--text-color)]'
            >
              <td className='px-4 py-3 text-[var(--sub-color)]'>{index + 1}</td>
              <td className='px-4 py-3 font-semibold'>{score.player}</td>
              <td className='px-4 py-3 text-[var(--accent-color)]'>{score.points}</td>
              <td className='px-4 py-3 text-[var(--sub-color)]'>{formatDate(score.playedOn)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
