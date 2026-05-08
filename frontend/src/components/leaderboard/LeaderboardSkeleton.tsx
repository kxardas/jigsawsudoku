import { Skeleton } from "../ui/Skeleton";

export function LeaderboardSkeleton() {
  return (
    <div className='overflow-hidden rounded-xl border border-[var(--border-color)]'>
      <div className='grid grid-cols-[28px_1fr_80px_100px] gap-3 bg-[var(--bg-color)] px-4 py-3'>
        <Skeleton className='h-4 w-4' />
        <Skeleton className='h-4 w-14' />
        <Skeleton className='h-4 w-14' />
        <Skeleton className='h-4 w-11' />
      </div>

      <div className='divide-y divide-[var(--border-color)]'>
        {Array.from({ length: 10 }).map((_, index) => (
          <div key={index} className='grid grid-cols-[28px_1fr_70px_100px] gap-4 px-4 py-4'>
            <Skeleton className='h-4 w-4' />
            <Skeleton className='h-4 w-18' />
            <Skeleton className='h-4 w-12' />
            <Skeleton className='h-4 w-20' />
          </div>
        ))}
      </div>
    </div>
  );
}
