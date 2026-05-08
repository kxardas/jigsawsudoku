import type { ReactNode } from "react";

type AppGridProps = {
  children: ReactNode;
  sidebar: ReactNode;
};

export function AppGrid({ children, sidebar }: AppGridProps) {
  return (
    <div className="grid items-start gap-6 xl:grid-cols-[minmax(0,1fr)_410px] 2xl:grid-cols-[minmax(0,1fr)_440px]">
      <section className="min-w-0 space-y-6 xl:space-y-8">{children}</section>

      <aside className="min-w-0 space-y-6 lg:sticky lg:top-6 lg:self-start xl:space-y-8">
        {sidebar}
      </aside>
    </div>
  );
}