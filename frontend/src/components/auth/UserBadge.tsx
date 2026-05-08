import { LogOut, UserRound } from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import { DEFAULT_PLAYER_NAME } from "../../utils/constants";
import { useState } from "react";
import { useToast } from "../../context/ToastContext";
import { Button } from "../ui/Button";
import { useTranslation } from "react-i18next";

type UserBadgeProps = {
  onOpenAuthModal: () => void;
};

function getInitials(username: string) {
  const trimmed = username.trim();

  if (!trimmed) {
    return "?";
  }

  return trimmed.slice(0, 1).toUpperCase();
}

export function UserBadge({ onOpenAuthModal }: UserBadgeProps) {
  const [loggingOut, setLoggingOut] = useState(false);
  
  const { showToast } = useToast();
  const { user, isAuthenticated, logout } = useAuth();
  const { t } = useTranslation();
  const username = user?.username ?? t("user.guest", { defaultValue: DEFAULT_PLAYER_NAME });

  async function handleLogout() {
    try {
      setLoggingOut(true);

      await logout();

      showToast({
        type: "success",
        message: t("toast.logoutSuccess"),
      });
    } catch {
      showToast({
        type: "error",
        message: t("toast.logoutError"),
      });
    } finally {
      setLoggingOut(false);
    }
  }

  return (
    <div className="flex w-fit items-center gap-3 rounded-2xl border border-[var(--border-color)] bg-[var(--bg-color)] px-3 py-2 shadow-lg shadow-black/10">
      <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-[var(--accent-color)] text-sm font-black text-[var(--bg-color)] shadow-[0_0_18px_rgba(124,110,230,0.25)]">
        {isAuthenticated ? getInitials(username) : <UserRound className="h-5 w-5" />}
      </div>

      <div className="min-w-0">
        <p className="max-w-32 truncate text-sm font-bold text-[var(--text-color)]">
          {username}
        </p>

        <div className="mt-0.5 flex items-center gap-1.5">
          <span
            className={`h-1.5 w-1.5 rounded-full ${
              isAuthenticated ? "bg-green-400" : "bg-yellow-300"
            }`}
          />

          <p className="text-xs text-[var(--sub-color)]">
            {isAuthenticated ? t("user.loggedIn") : t("user.guestMode")}
          </p>
        </div>
      </div>

      <div className="ml-1 h-8 w-px bg-[var(--border-color)]" />

      {isAuthenticated ? (
        <button
          type="button"
          onClick={handleLogout}
          disabled={loggingOut}
          className="cursor-pointer inline-flex h-9 items-center gap-2 rounded-xl px-3 text-sm font-bold text-[var(--sub-color)] transition hover:bg-[var(--bg-color)] hover:text-[var(--text-color)]"
        >
          <LogOut className="h-4 w-4" />
          {loggingOut ? t("user.loggingOut") : t("user.logout")}
        </button>
      ) : (
        <Button
          type="button"
          onClick={onOpenAuthModal}
          className="h-9 rounded-xl px-3 text-sm font-bold text-[var(--bg-color)] transition hover:brightness-110"
        >
          {t("user.signIn")}
        </Button>
      )}
    </div>
  );
}
