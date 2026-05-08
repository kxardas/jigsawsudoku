import { UserBadge } from "../auth/UserBadge";
import { LanguageSwitcher } from "../ui/LanguageSwitcher";
import { ThemeSwitcher } from "../ui/ThemeSwitcher";
import { useTranslation } from "react-i18next";

type AppHeaderProps = {
  onOpenModal: () => void;
}

export function AppHeader({ onOpenModal }: AppHeaderProps) {
  const { t } = useTranslation();

  return (
    <header className='flex flex-col gap-4 rounded-2xl border border-[var(--border-color)] bg-[var(--sub-alt-color)] p-4 shadow-lg shadow-black/10 sm:flex-row sm:items-center sm:justify-between'>
      <div>
        <h1 className='text-4xl font-black text-[var(--text-color)]'>{t("app.title")}</h1>
        <p className='mt-2 text-[var(--sub-color)]'>{t("app.subtitle")}</p>
      </div>

      <div className='flex flex-col gap-3 sm:flex-row sm:items-center'>
        <div className='flex items-center gap-2'>
          <LanguageSwitcher />
          <ThemeSwitcher />
        </div>
        <UserBadge onOpenAuthModal={onOpenModal}/>
      </div>
    </header>
  );
}
