import { useEffect, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { Gamepad2, LogIn, ShieldCheck, UserPlus, X } from "lucide-react";
import { LoginForm } from "./LoginForm";
import { RegisterForm } from "./RegisterForm";

type AuthMode = "login" | "register";

type AuthModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

export function AuthModal({ isOpen, onClose }: AuthModalProps) {
  const [mode, setMode] = useState<AuthMode>("login");

  useEffect(() => {
    if (!isOpen) {
      return;
    }

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

  function handleSuccess() {
    onClose();
  }

  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          className='fixed inset-0 z-[60] flex items-center justify-center bg-black/75 px-4 backdrop-blur-xl'
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.18 }}
          onMouseDown={onClose}
        >
          <motion.div
            className='relative w-full max-w-[760px]'
            initial={{ opacity: 0, y: 22, scale: 0.96 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: -33, scale: 1.1 }}
            transition={{ duration: 0.22, ease: "easeOut" }}
            onMouseDown={(event) => event.stopPropagation()}
          >
            <div className='relative grid overflow-hidden rounded-2xl border border-white/10 bg-[var(--bg-color)] shadow-2xl md:grid-cols-[0.9fr_1.1fr]'>
              <section className='relative hidden min-h-[560px] overflow-hidden border-r border-white/10 bg-[var(--bg-color)]/55 p-6 md:block'>
                <div className='relative flex h-full flex-col justify-between'>
                  <div>
                    <div className='flex h-14 w-14 items-center justify-center rounded-2xl bg-[var(--accent-color)] text-[var(--bg-color)] shadow-[0_0_34px_rgba(124,110,230,0.35)]'>
                      <Gamepad2 className='h-8 w-8' />
                    </div>

                    <h2 className='mt-6 text-4xl font-black leading-tight text-[var(--text-color)]'>
                      Play smarter.
                      <br />
                      Score higher.
                    </h2>

                    <p className='mt-3 text-sm leading-relaxed text-[var(--sub-color)]'>
                      Save your progress, rate puzzles, post comments and keep your name on the
                      leaderboard.
                    </p>
                  </div>

                  <div className='space-y-3'>
                    <div className='rounded-2xl border border-white/10 bg-green-600/9 p-4 shadow-[0_0_34px_rgba(83,188,78,0.2)]'>
                      <div className='flex items-start gap-3'>
                        <ShieldCheck className='mt-0.5 h-5 w-5 shrink-0 text-green-300' />

                        <div>
                          <p className='text-sm font-bold text-[var(--text-color)]'>
                            HttpOnly session
                          </p>

                          <p className='mt-1 text-xs leading-relaxed text-[var(--sub-color)]'>
                            Tokens stay outside JavaScript. Cleaner and safer.
                          </p>
                        </div>
                      </div>
                    </div>

                    <div className='grid grid-cols-3 gap-2 text-center'>
                      <div className='rounded-2xl bg-[var(--sub-alt-color)]/60 px-3 py-3'>
                        <p className='text-lg font-black text-[var(--text-color)]'>5×5</p>
                        <p className='text-xs text-[var(--sub-color)]'>easy</p>
                      </div>

                      <div className='rounded-2xl bg-[var(--sub-alt-color)]/60 px-3 py-3'>
                        <p className='text-lg font-black text-[var(--text-color)]'>7×7</p>
                        <p className='text-xs text-[var(--sub-color)]'>normal</p>
                      </div>

                      <div className='rounded-2xl bg-[var(--sub-alt-color)]/60 px-3 py-3'>
                        <p className='text-lg font-black text-[var(--text-color)]'>9×9</p>
                        <p className='text-xs text-[var(--sub-color)]'>hard</p>
                      </div>
                    </div>
                  </div>
                </div>
              </section>

              <section className='relative flex min-h-[560px] flex-col p-4 sm:p-5'>
                <div className='mb-4 flex items-start justify-between gap-4'>
                  <div>
                    <p className='text-xs font-bold uppercase tracking-[0.1em] text-[var(--sub-color)]'>
                      Account
                    </p>

                    <h2 className='mt-1 text-2xl font-black text-[var(--text-color)]'>
                      {mode === "login" ? "Welcome back" : "Create account"}
                    </h2>
                  </div>

                  <button
                    type='button'
                    onClick={onClose}
                    className='rounded-xl p-2 text-[var(--sub-color)] transition hover:bg-[var(--bg-color)] hover:text-[var(--text-color)]'
                    aria-label='Close auth modal'
                  >
                    <X className='h-5 w-5' />
                  </button>
                </div>

                <div className='mb-4 grid grid-cols-2 rounded-2xl bg-[var(--bg-color)] p-1'>
                  <button
                    type='button'
                    onClick={() => setMode("login")}
                    className={`
                      cursor-pointer relative flex items-center justify-center gap-2 rounded-xl px-3 py-2 text-sm font-bold transition
                      ${
                        mode === "login"
                          ? "text-[var(--bg-color)]"
                          : "text-[var(--sub-color)] hover:text-[var(--text-color)]"
                      }
                    `}
                  >
                    {mode === "login" && (
                      <motion.span
                        layoutId='auth-active-tab'
                        className='absolute inset-0 rounded-xl bg-[var(--accent-color)] shadow-[0_0_24px_rgba(124,110,230,0.35)]'
                        transition={{
                          type: "spring",
                          stiffness: 420,
                          damping: 32,
                        }}
                      />
                    )}

                    <span className='relative z-10 flex items-center gap-2'>
                      <LogIn className='h-4 w-4' />
                      Login
                    </span>
                  </button>

                  <button
                    type='button'
                    onClick={() => setMode("register")}
                    className={`
                      cursor-pointer relative flex items-center justify-center gap-2 rounded-xl px-3 py-2 text-sm font-bold transition
                      ${
                        mode === "register"
                          ? "text-[var(--bg-color)]"
                          : "text-[var(--sub-color)] hover:text-[var(--text-color)]"
                      }
                    `}
                  >
                    {mode === "register" && (
                      <motion.span
                        layoutId='auth-active-tab'
                        className='absolute inset-0 rounded-xl bg-[var(--accent-color)] shadow-[0_0_24px_rgba(124,110,230,0.35)]'
                        transition={{
                          type: "spring",
                          stiffness: 420,
                          damping: 32,
                        }}
                      />
                    )}

                    <span className='relative z-10 flex items-center gap-2'>
                      <UserPlus className='h-4 w-4' />
                      Register
                    </span>
                  </button>
                </div>

                <div className='flex flex-1 flex-col justify-center'>
                  <AnimatePresence mode='wait'>
                    {mode === "login" ? (
                      <motion.div
                        key='login'
                        initial={{ opacity: 0, x: -14 }}
                        animate={{ opacity: 1, x: 0 }}
                        exit={{ opacity: 0, x: 14 }}
                        transition={{ duration: 0.16 }}
                      >
                        <LoginForm
                          onSuccess={handleSuccess}
                          onSwitchToRegister={() => setMode("register")}
                        />
                      </motion.div>
                    ) : (
                      <motion.div
                        key='register'
                        initial={{ opacity: 0, x: 14 }}
                        animate={{ opacity: 1, x: 0 }}
                        exit={{ opacity: 0, x: -14 }}
                        transition={{ duration: 0.16 }}
                      >
                        <RegisterForm
                          onSuccess={handleSuccess}
                          onSwitchToLogin={() => setMode("login")}
                        />
                      </motion.div>
                    )}
                  </AnimatePresence>
                </div>
              </section>
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
