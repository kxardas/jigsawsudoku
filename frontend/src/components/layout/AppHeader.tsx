import { UserBadge } from "../auth/UserBadge";

type AppHeaderProps = {
  onOpenModal: () => void;
}

export function AppHeader({ onOpenModal }: AppHeaderProps) {
  return (
    <header className='flex flex-col gap-4 sm:flex-row sm:justify-between'>
      <div>
        <h1 className='text-4xl font-black text-[var(--text-color)]'>Jigsaw Sudoku</h1>
        <p className='mt-2 text-[var(--sub-color)]'>Play and have fun!</p>
      </div>

      <UserBadge onOpenAuthModal={onOpenModal}/>
    </header>
  );
}
