import { motion } from "framer-motion";
import { X } from "lucide-react";
import type { ToastItem } from "../../context/ToastContext";

type ToastProps = {
  toast: ToastItem;
  onClose: (id: string) => void;
};

const toastClasses: Record<ToastItem["type"], string> = {
  success: "border-green-500/40 bg-green-500/10 text-green-300",
  error: "border-red-500/40 bg-red-500/10 text-red-300",
  info: "border-blue-500/40 bg-blue-500/10 text-blue-300",
  warning: "border-yellow-500/40 bg-yellow-500/10 text-yellow-300",
};

export function Toast({ toast, onClose }: ToastProps) {
  return (
    <motion.div
      layout
      initial={{ opacity: 0, x: 40, scale: 0.96 }}
      animate={{ opacity: 1, x: 0, scale: 1 }}
      exit={{ opacity: 0, x: 40, scale: 1.04 }}
      transition={{ duration: 0.18, ease: "easeOut" }}
      className={`pointer-events-auto flex w-full items-start justify-between gap-3 rounded-xl border px-4 py-3 shadow-lg backdrop-blur ${toastClasses[toast.type]}`}
    >
      <p className='text-sm font-medium'>{toast.message}</p>
      <button
        onClick={() => onClose(toast.id)}
        className='rounded-md opacity-70 transition hover:opacity-100'
        aria-label='Close notification'
      >
        <X className='h-4 w-4' />
      </button>
    </motion.div>
  );
}
