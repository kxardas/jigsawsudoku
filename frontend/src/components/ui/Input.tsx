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
          peer w-full rounded-2xl border bg-[var(--bg-color)]
          px-4 pb-3 pt-5 text-[var(--text-color)]
          shadow-[inset_0_1px_0_rgba(255,255,255,0.04),0_10px_24px_rgba(0,0,0,0.14)]
          outline-none transition-all duration-200 placeholder-transparent
          hover:border-white/20
          focus:shadow-[inset_0_1px_0_rgba(255,255,255,0.05),0_0_0_4px_rgba(255,255,255,0.06),0_14px_30px_rgba(0,0,0,0.18)]
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
          pointer-events-none absolute left-4 bg-[var(--bg-color)] px-1.5
          font-medium text-[var(--sub-color)] transition-all duration-200
          ${hasValue ? "-top-2 text-xs" : "top-4 text-sm peer-focus:-top-2 peer-focus:text-xs"}
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
