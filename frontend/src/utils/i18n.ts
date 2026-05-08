import i18n from "i18next";
import { initReactI18next } from "react-i18next";

const LANGUAGE_STORAGE_KEY = "jigsaw-sudoku-language";

export const SUPPORTED_LANGUAGES = ["en", "sk"] as const;

export type SupportedLanguage = (typeof SUPPORTED_LANGUAGES)[number];

function getInitialLanguage(): SupportedLanguage {
  if (typeof window === "undefined") {
    return "en";
  }

  const storedLanguage = window.localStorage.getItem(LANGUAGE_STORAGE_KEY);
  return SUPPORTED_LANGUAGES.includes(storedLanguage as SupportedLanguage)
    ? (storedLanguage as SupportedLanguage)
    : "en";
}

const resources = {
  en: {
    translation: {
        "app.title": "Jigsaw Sudoku",
        "app.subtitle": "Play and have fun!",
        "language.label": "Language",
        "theme.label": "Theme",
        "theme.dark": "Dark",
        "theme.light": "Light",
        "user.guest": "guest",
        "user.loggedIn": "Logged in",
        "user.guestMode": "Guest mode",
        "user.logout": "Logout",
        "user.loggingOut": "Logging out...",
        "user.signIn": "Sign in",
        "toast.logoutSuccess": "Logged out successfully",
        "toast.logoutError": "Could not log out",
    },
  },
  sk: {
    translation: {
        "app.title": "Jigsaw Sudoku",
        "app.subtitle": "Hraj a zabav sa!",
        "language.label": "Jazyk",
        "theme.label": "Téma",
        "theme.dark": "Tmavá",
        "theme.light": "Svetlá",
        "user.guest": "hosť",
        "user.loggedIn": "Prihlásený",
        "user.guestMode": "Režim hosťa",
        "user.logout": "Odhlásiť",
        "user.loggingOut": "Odhlasuje sa...",
        "user.signIn": "Prihlásiť",
        "toast.logoutSuccess": "Odhlásenie prebehlo úspešne",
        "toast.logoutError": "Odhlásenie sa nepodarilo",
    },
  },
};

void i18n.use(initReactI18next).init({
  resources,
  lng: getInitialLanguage(),
  fallbackLng: "en",
  interpolation: {
    escapeValue: false,
  },
});

if (typeof document !== "undefined") {
  document.documentElement.lang = getInitialLanguage();
}

export { LANGUAGE_STORAGE_KEY };

export default i18n;
