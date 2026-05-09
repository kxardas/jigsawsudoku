import type { ReactNode } from "react";

type PageLayoutProps = {
  children: ReactNode;
};

export function PageLayout({ children }: PageLayoutProps) {
  return (
    <div className='min-h-screen bg-gsbg text-gs-text bg-[var(--bg-color)] px-4 py-8 text-[var(--text-color)]'>
      <main className='mx-auto max-w-7xl space-y-6'>{children}</main>
    </div>
  );
}
