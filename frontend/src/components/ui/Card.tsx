import type { ReactNode } from "react";

type CardProps = {
  children: ReactNode;
  className?: string;
};

export function Card({ children, className = "" }: CardProps) {
  return (
    <section
      className={`rounded-2xl border border-white/10 bg-[var(--sub-alt-color)] p-5 shadow-lg ${className}`}
    >
      {children}
    </section>
  );
}
