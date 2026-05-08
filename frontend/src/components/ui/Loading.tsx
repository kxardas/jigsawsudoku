type LoadingProps = {
  text?: string;
};

export function Loading({ text = "Loading..." }: LoadingProps) {
  return (
    <div className='flex items-center gap-2 text-[var(--sub-color)]'>
      <div className='h-4 w-4 animate-spin rounded-full border-2 border-[var(--sub-color)] border-t-[var(--accent-color)]' />
      <span>{text}</span>
    </div>
  );
}
