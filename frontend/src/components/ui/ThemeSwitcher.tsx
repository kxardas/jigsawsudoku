import { Moon, Sun } from "lucide-react";
import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

const THEME_STORAGE_KEY = "jigsaw-sudoku-theme";

type ThemeName = "dark" | "light";

function getInitialTheme(): ThemeName {
  if (typeof window === "undefined") {
    return "dark";
  }

  return window.localStorage.getItem(THEME_STORAGE_KEY) === "light" ? "light" : "dark";
}

export function ThemeSwitcher() {
  const [theme, setTheme] = useState<ThemeName>(getInitialTheme);
  const { t } = useTranslation();

  const isLight = theme === "light";

  useEffect(() => {
    document.documentElement.dataset.theme = theme;
    window.localStorage.setItem(THEME_STORAGE_KEY, theme);
  }, [theme]);

  function toggleTheme() {
    setTheme((currentTheme) => (currentTheme === "dark" ? "light" : "dark"));
  }

  return (
    <button
      type="button"
      role="switch"
      aria-checked={isLight}
      aria-label={t("theme.label")}
      title={isLight ? t("theme.light") : t("theme.dark")}
      onClick={toggleTheme}
      className="relative inline-flex h-10 w-[76px] cursor-pointer items-center rounded-full border border-[var(--border-color)] bg-[var(--sub-alt-color)] p-1 shadow-lg shadow-black/10 transition hover:border-[var(--accent-color)]"
    >
      <span className="sr-only">{t("theme.label")}</span>

      <motion.span
        className="absolute top-0.8 h-8 w-8 rounded-full bg-[var(--accent-color)] shadow-md shadow-black/15"
        animate={{ x: isLight ? 34 : 0 }}
        transition={{
          type: "spring",
          stiffness: 450,
          damping: 35,
        }}
      />

      <span className="relative z-10 flex w-full items-center justify-between px-2">
        <Moon
          className={`h-4 w-4 transition-colors ${
            isLight ? "text-[var(--sub-color)]" : "text-white"
          }`}
        />
        <Sun
          className={`h-4 w-4 transition-colors ${
            isLight ? "text-white" : "text-[var(--sub-color)]"
          }`}
        />
      </span>
    </button>
  );
}