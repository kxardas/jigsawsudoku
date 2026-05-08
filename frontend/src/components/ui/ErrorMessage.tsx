type ErrorMessageProps = {
  message: string;
  className?: string;
};

export function ErrorMessage({ message, className = "" }: ErrorMessageProps) {
  if (!message) return null;

  return (
    <div
      className={`text-center rounded-lg border border-red-500/40 bg-red-500/10 px-4 py-3 text-sm text-red-300 ${className}`}
    >
      {message}
    </div>
  );
}
