import { createContext, useCallback, useContext, useMemo, useState, type ReactNode } from "react";

export type ToastType = "success" | "error" | "info" | "warning";

export type ToastItem = {
  id: string;
  type: ToastType;
  message: string;
};

type ShowToastOptions = {
  type?: ToastType;
  message: string;
};

type ToastContextValue = {
  toasts: ToastItem[];
  showToast: (options: ShowToastOptions) => void;
  removeToast: (id: string) => void;
};

const ToastContext = createContext<ToastContextValue | undefined>(undefined);

type ToastProviderProps = {
  children: ReactNode;
};

export function ToastProvider({ children }: ToastProviderProps) {
  const [toasts, setToasts] = useState<ToastItem[]>([]);

  const removeToast = useCallback((id: string) => {
    setToasts((currentToasts) => currentToasts.filter((toast) => toast.id !== id));
  }, []);

  const showToast = useCallback(
    ({ type = "info", message }: ShowToastOptions) => {
      const id = `${Date.now()}-${Math.random().toString(36).slice(2)}`;

      const newToast: ToastItem = {
        id,
        type,
        message,
      };

      setToasts((currentToasts) => [...currentToasts, newToast]);

      window.setTimeout(() => {
        removeToast(id);
      }, 3000);
    },
    [removeToast],
  );

  const value = useMemo(
    () => ({
      toasts,
      showToast,
      removeToast,
    }),
    [toasts, showToast, removeToast],
  );

  return <ToastContext.Provider value={value}>{children}</ToastContext.Provider>;
}

export function useToast() {
  const context = useContext(ToastContext);

  if (!context) throw new Error("useToast must be used inside ToastProvider");

  return context;
}
