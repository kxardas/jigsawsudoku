import { Skeleton } from "../ui/Skeleton";

export function CommentsSkeleton() {
  return (
    <div className='space-y-3'>
      {Array.from({ length: 3 }).map((_, index) => (
        <article
          key={index}
          className='rounded-2xl border border-white/10 bg-[var(--bg-color)] p-4'
        >
          <div className='flex gap-3'>
            <Skeleton className='h-10 w-10 shrink-0 rounded-xl' />

            <div className='min-w-0 flex-1'>
              <div className='mb-3 flex items-center justify-between gap-4'>
                <Skeleton className='h-4 w-28' />
                <Skeleton className='h-3 w-20' />
              </div>

              <div className='space-y-2'>
                <Skeleton className='h-4 w-full' />
                <Skeleton className='h-4 w-5/6' />
                {index === 0 && <Skeleton className='h-4 w-2/3' />}
              </div>
            </div>
          </div>
        </article>
      ))}
    </div>
  );
}
