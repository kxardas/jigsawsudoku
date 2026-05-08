type EmptyStateProps = {
  title: string;
  description?: string;
  className?: string;
};

export function EmptyState({ title, description, className = "" }: EmptyStateProps) {
  return (
    <div
      className={`rounded-2xl border border-dashed border-[var(--border-color)] bg-[var(--bg-color)] p-6 text-center ${className}`}
    >
      <p className='font-semibold text-[var(--text-color)]'>{title}</p>

      {description && <p className='mt-1 text-sm text-[var(--sub-color)]'>{description}</p>}
    </div>
  );
}
