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
        className={`custom-scrollbar peer min-h-28 w-full resize-none rounded-2xl border bg-[var(--bg-color)] px-4 pb-4 pt-7 text-sm leading-6 text-[var(--text-color)] shadow-[inset_0_1px_0_rgba(255,255,255,0.04),0_10px_24px_rgba(0,0,0,0.14)] outline-none transition-all duration-200 placeholder-transparent hover:border-white/20 focus:border-[var(--accent-color)] focus:shadow-[inset_0_1px_0_rgba(255,255,255,0.05),0_0_0_4px_rgba(255,255,255,0.06),0_14px_30px_rgba(0,0,0,0.18)] disabled:cursor-not-allowed disabled:opacity-50 ${className}`}
        {...props}
      />

      <label
        className={`pointer-events-none absolute left-4 bg-[var(--bg-color)] px-1.5 font-medium text-[var(--sub-color)] transition-all duration-200
          ${
            hasValue
              ? "-top-2 text-xs text-[var(--accent-color)]"
              : "top-4 text-sm peer-focus:-top-2 peer-focus:text-xs peer-focus:text-[var(--accent-color)]"
          }`}
      >
        {label}
      </label>
    </div>
  );
}
