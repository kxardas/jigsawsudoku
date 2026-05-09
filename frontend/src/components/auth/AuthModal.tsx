import { useEffect, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { X } from "lucide-react";
import { LoginForm } from "./LoginForm";
import { RegisterForm } from "./RegisterForm";

type AuthMode = "login" | "register";

type AuthModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

export function AuthModal({ isOpen, onClose }: AuthModalProps) {
  const [mode, setMode] = useState<AuthMode>("login");

  const isLogin = mode === "login";

  useEffect(() => {
    if (!isOpen) return;

    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === "Escape") {
        onClose();
      }
    }

    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [isOpen, onClose]);

  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          className="fixed inset-0 z-[60] flex items-center justify-center bg-black/70 px-4"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          onMouseDown={onClose}
        >
          <motion.div
            className="w-full max-w-md rounded-2xl border border-white/10 bg-[var(--sub-alt-color)] p-5 shadow-xl"
            initial={{ opacity: 0, y: 20, scale: 0.96 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: -20, scale: 1.1 }}
            transition={{ duration: 0.2 }}
            onMouseDown={(event) => event.stopPropagation()}
          >
            <div className="mb-5 flex items-center justify-between">
              <h2 className="text-2xl font-bold text-[var(--text-color)]">
                {isLogin ? "Login" : "Register"}
              </h2>

              <button
                type="button"
                onClick={onClose}
                className="rounded-lg p-2 text-[var(--sub-color)] transition hover:bg-white/10 hover:text-[var(--text-color)]"
                aria-label="Close auth modal"
              >
                <X className="h-5 w-5" />
              </button>
            </div>

            <div className="mb-5 grid grid-cols-2 gap-2">
              <button
                type="button"
                onClick={() => setMode("login")}
                className={`rounded-lg px-4 py-2 font-bold transition ${
                  isLogin
                    ? "bg-[var(--accent-color)] text-[var(--bg-color)]"
                    : "bg-white/5 text-[var(--sub-color)] hover:text-[var(--text-color)]"
                }`}
              >
                Login
              </button>

              <button
                type="button"
                onClick={() => setMode("register")}
                className={`rounded-lg px-4 py-2 font-bold transition ${
                  !isLogin
                    ? "bg-[var(--accent-color)] text-[var(--bg-color)]"
                    : "bg-white/5 text-[var(--sub-color)] hover:text-[var(--text-color)]"
                }`}
              >
                Register
              </button>
            </div>

            <AnimatePresence mode="wait">
              {isLogin ? (
                <motion.div
                  key="login"
                  initial={{ opacity: 0, x: -12 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: 12 }}
                  transition={{ duration: 0.15 }}
                >
                  <LoginForm
                    onSuccess={onClose}
                    onSwitchToRegister={() => setMode("register")}
                  />
                </motion.div>
              ) : (
                <motion.div
                  key="register"
                  initial={{ opacity: 0, x: 12 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: -12 }}
                  transition={{ duration: 0.15 }}
                >
                  <RegisterForm
                    onSuccess={onClose}
                    onSwitchToLogin={() => setMode("login")}
                  />
                </motion.div>
              )}
            </AnimatePresence>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}