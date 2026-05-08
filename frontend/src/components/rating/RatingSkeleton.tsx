import { Skeleton } from "../ui/Skeleton";

export function RatingSkeleton() {
  return (
    <div className='space-y-4'>
      <div className='rounded-2xl bg-[var(--bg-color)] p-5'>
        <div className='flex items-center gap-4'>
          <Skeleton className='h-16 w-16 shrink-0 rounded-2xl' />

          <div className='min-w-0 flex-1'>
            <div className='mb-2 flex gap-1'>
              {Array.from({ length: 5 }).map((_, index) => (
                <Skeleton key={index} className='h-8 w-8 rounded-md' />
              ))}
            </div>

            <Skeleton className='h-4 w-44' />
          </div>
        </div>
      </div>

      <div className='rounded-2xl border border-[var(--border-color)] bg-[var(--bg-color)] p-5'>
        <div className='mb-3 space-y-2'>
          <Skeleton className='h-4 w-24' />
          <Skeleton className='h-4 w-52' />
        </div>

        <div className='flex gap-1'>
          {Array.from({ length: 5 }).map((_, index) => (
            <Skeleton key={index} className='h-8 w-8 rounded-md' />
          ))}
        </div>
      </div>
    </div>
  );
}
