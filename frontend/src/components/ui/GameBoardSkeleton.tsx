import { Skeleton } from "./Skeleton";

type GameBoardSkeletonProps = {
  size?: number;
};

export function GameBoardSkeleton({ size = 5 }: GameBoardSkeletonProps) {
  return (
    <div
      className='grid w-fit gap-2 rounded-2xl bg-[var(--sub-alt-color)] p-4'
      style={{
        gridTemplateColumns: `repeat(${size}, 48px)`,
      }}
    >
      {Array.from({ length: size * size }).map((_, index) => (
        <Skeleton key={index} className='h-12 w-12' />
      ))}
    </div>
  );
}
