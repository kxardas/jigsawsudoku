import type { ReactNode } from "react";

type CardProps = {
  children: ReactNode;
  className?: string;
};

export function Card({ children, className = "" }: CardProps) {
  return (
    <section
      className={`rounded-2xl border border-white/10 bg-[var(--bg-color)] p-5 shadow-lg ${className}`}
    >
      {children}
    </section>
  );
}
