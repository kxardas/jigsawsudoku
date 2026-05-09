import type { ButtonHTMLAttributes, ReactNode } from "react";

type ButtonVariant = "primary" | "secondary" | "danger" | "ghost" | "success" | "warning" | "ghost_on" | "number_pad";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  children: ReactNode;
  variant?: ButtonVariant;
};

const variantClasses: Record<ButtonVariant, string> = {
  primary:
    "rounded-xl bg-blue-700 hover:bg-blue-600 active:bg-blue-800 text-white shadow-lg shadow-blue-700/25",

  secondary:
    "rounded-xl bg-violet-600 hover:bg-violet-500 active:bg-violet-700 text-white shadow-lg shadow-violet-600/25",

  danger:
    "rounded-xl bg-pink-600 hover:bg-pink-500 active:bg-pink-700 text-white shadow-lg shadow-pink-600/25",

  success:
    "rounded-xl bg-[#1DB954] hover:bg-[#1db954]/80 text-slate-950 shadow-lg shadow-[#1DB954]/25",

  warning:
    "rounded-xl bg-lime-400 hover:bg-lime-300 active:bg-lime-500 text-slate-950 shadow-lg shadow-lime-400/25",

  ghost:
    "rounded-xl border border-fuchsia-400/30 bg-fuchsia-500/10 text-fuchsia-200 hover:bg-fuchsia-500/20 hover:text-white active:bg-fuchsia-500/30",

  ghost_on:
    "rounded-xl border border-fuchsia-400/30 bg-fuchsia-500/70 text-fuchsia/20 hover:bg-fuchsia-500/50 hover:text-fuchsia active:bg-fuchsia-500/30 shadow-lg shadow-[#D946EF]/25",
  
  number_pad:
    "rounded-lg bg-blue-700 hover:bg-blue-600 active:bg-blue-800 text-white shadow-lg shadow-blue-700/25",
};

export function Button({ children, variant = "primary", className = "", ...props }: ButtonProps) {
  return (
    <button
      className={`flex justify-center items-center gap-1 font-semibold px-4 py-2 text-[var(--text-color)] transition cursor-pointer disabled:cursor-not-allowed disabled:opacity-50 ${variantClasses[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
