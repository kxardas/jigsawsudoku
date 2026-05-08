import type { InputHTMLAttributes } from "react";
import { AnimatePresence, motion } from "framer-motion";

type InputProps = InputHTMLAttributes<HTMLInputElement> & {
  label: string;
  error?: string;
  onErrorDismiss?: () => void;
};

export function Input({
  label,
  error,
  onErrorDismiss,
  className = "",
  value,
  onFocus,
  ...props
}: InputProps) {
  const hasValue = value !== undefined && String(value).length > 0;

  return (
    <div className='relative w-full'>
      <input
        value={value}
        placeholder=' '
        onFocus={(event) => {
          onErrorDismiss?.();
          onFocus?.(event);
        }}
        className={`
          peer w-full rounded-lg border bg-[var(--bg-color)]
          px-3 pb-2 pt-4 text-[var(--text-color)]
          outline-none transition placeholder-transparent
          disabled:cursor-not-allowed disabled:opacity-50
          ${
            error
              ? "border-red-400 focus:border-red-400"
              : "border-white/10 focus:border-[var(--accent-color)]"
          }
          ${className}
        `}
        {...props}
      />

      <label
        className={`
          pointer-events-none absolute left-3 bg-[var(--bg-color)] px-1
          text-[var(--sub-color)] transition-all
          ${hasValue ? "-top-2 text-xs" : "top-3 text-base peer-focus:-top-2 peer-focus:text-xs"}
          ${
            error ? "text-red-300 peer-focus:text-red-300" : "peer-focus:text-[var(--accent-color)]"
          }
        `}
      >
        {label}
      </label>

      <AnimatePresence>
        {error && (
          <motion.div
            initial={{ opacity: 0, y: -6, scale: 0.96 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: -6, scale: 0.96 }}
            transition={{ duration: 0.18, ease: "easeOut" }}
            className='absolute top-full z-30 mt-2 max-w-[calc(100%-1.5rem)] rounded-xl border border-red-500/30 bg-red-500/10 px-3 py-2 text-xs font-medium text-red-300 shadow-xl backdrop-blur'
          >
            {error}
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
