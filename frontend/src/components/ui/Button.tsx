import type { ButtonHTMLAttributes, ReactNode } from "react";

type ButtonVariant =
  | "primary"
  | "secondary"
  | "danger"
  | "ghost"
  | "success"
  | "warning"
  | "ghost_on";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  children: ReactNode;
  variant?: ButtonVariant;
};

const variantClasses: Record<ButtonVariant, string> = {
  primary:
    "bg-blue-700 hover:bg-blue-600 active:bg-blue-800 text-white shadow-lg shadow-blue-700/25",

  secondary:
    "bg-violet-600 hover:bg-violet-500 active:bg-violet-700 text-white shadow-lg shadow-violet-600/25",

  danger:
    "bg-pink-600 hover:bg-pink-500 active:bg-pink-700 text-white shadow-lg shadow-pink-600/25",

  success: 
    "bg-[#1DB954] hover:bg-[#1db954]/80 text-slate-950 shadow-lg shadow-[#1DB954]/25",

  warning:
    "bg-lime-400 hover:bg-lime-300 active:bg-lime-500 text-slate-950 shadow-lg shadow-lime-400/25",

  ghost:
    "border border-fuchsia-400/30 bg-fuchsia-500/10 text-[var(--text-color)] hover:bg-fuchsia-500/20 hover:text-gray-600 active:bg-fuchsia-500/30",

  ghost_on:
    "border border-fuchsia-300/50 bg-fuchsia-500/35 text-fuchsia-100 shadow-lg shadow-fuchsia-500/15 hover:bg-fuchsia-500/35 hover:text-white active:bg-fuchsia-500/45",
};

export function Button({ children, variant = "primary", className = "", ...props }: ButtonProps) {
  return (
    <button
      className={`flex justify-center items-center gap-1 rounded-xl font-semibold px-4 py-2 text-[var(--text-color)] transition cursor-pointer disabled:cursor-not-allowed disabled:opacity-50 ${variantClasses[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
