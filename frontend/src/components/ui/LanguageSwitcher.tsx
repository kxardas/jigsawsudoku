import { motion } from "framer-motion";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import {
  LANGUAGE_STORAGE_KEY,
  SUPPORTED_LANGUAGES,
  type SupportedLanguage,
} from "../../utils/i18n";

const languageLabels: Record<SupportedLanguage, string> = {
  en: "EN",
  sk: "SK",
};

export function LanguageSwitcher() {
  const { i18n, t } = useTranslation();

  const activeLanguage = SUPPORTED_LANGUAGES.includes(i18n.language as SupportedLanguage)
    ? (i18n.language as SupportedLanguage)
    : "en";

  useEffect(() => {
    document.documentElement.lang = activeLanguage;
    window.localStorage.setItem(LANGUAGE_STORAGE_KEY, activeLanguage);
  }, [activeLanguage]);

  async function handleLanguageChange(language: SupportedLanguage) {
    if (language === activeLanguage) return;
    await i18n.changeLanguage(language);
  }

  return (
    <div
      className='inline-flex h-10 items-center gap-1 rounded-full border border-[var(--border-color)] bg-[var(--sub-alt-color)] p-1 shadow-lg shadow-black/10'
      role='radiogroup'
      aria-label={t("language.label")}
    >
      {SUPPORTED_LANGUAGES.map((language) => {
        const isActive = activeLanguage === language;

        return (
          <button
            key={language}
            type='button'
            role='radio'
            aria-checked={isActive}
            onClick={() => handleLanguageChange(language)}
            className={`relative h-8 min-w-9 cursor-pointer rounded-full px-2 text-xs font-black transition-colors ${
              isActive ? "text-white" : "text-[var(--sub-color)] hover:text-[var(--text-color)]"
            }`}
          >
            {isActive && (
              <motion.span
                layoutId='active-language-pill'
                className='absolute inset-0 rounded-full bg-[var(--accent-color)] shadow-md shadow-black/10'
                transition={{
                  type: "spring",
                  stiffness: 450,
                  damping: 35,
                }}
              />
            )}

            <span className='relative z-10'>{languageLabels[language]}</span>
          </button>
        );
      })}
    </div>
  );
}
