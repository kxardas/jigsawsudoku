import type { ReactNode } from "react";

type AppGridProps = {
  children: ReactNode;
  sidebar: ReactNode;
};

export function AppGrid({ children, sidebar }: AppGridProps) {
  return (
    <div className='grid items-start gap-6 xl:grid-cols-[minmax(0,1fr)_380px]'>
        <section className='space-y-6'>{children}</section>
        <aside className='space-y-6 lg:sticky lg:top-6 lg:self-start min-w-0'>{sidebar}</aside>
    </div>
  );
}
