import type { TextareaHTMLAttributes } from "react";

type TextareaProps = TextareaHTMLAttributes<HTMLTextAreaElement> & {
  label: string;
};

export function Textarea({ label, className = "", value, ...props }: TextareaProps) {
  const hasValue = value !== undefined && String(value).length > 0;

  return (
    <div className='relative w-full'>
      <textarea
        value={value}
        placeholder=' '
        className={`custom-scrollbar peer min-h-16 w-full resize-none rounded-lg border bg-[var(--bg-color)] px-3 pb-2 pt-2 text-[var(--text-color)] outline-none transition placeholder-transparent focus:border-[var(--accent-color)] disabled:cursor-not-allowed disabled:opacity-50 ${className}`}
        {...props}
      />

      <label
        className={`pointer-events-none absolute left-2 bg-[var(--bg-color)] px-1 text-[var(--sub-color)] transition-all
          ${
            hasValue
              ? "-top-2 text-xs text-[var(--accent-color)]"
              : "top-2 text-base peer-focus:-top-2 peer-focus:text-xs peer-focus:text-[var(--accent-color)]"
          }`}
      >
        {label}
      </label>
    </div>
  );
}
