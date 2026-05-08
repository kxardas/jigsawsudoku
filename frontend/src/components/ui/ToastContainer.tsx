import { AnimatePresence } from "framer-motion";
import { useToast } from "../../context/ToastContext";
import { Toast } from "./Toast";

export function ToastContainer() {
  const { toasts, removeToast } = useToast();

  return (
    <div className='pointer-events-none fixed right-4 top-4 z-50 flex w-[360px] max-w-[calc(100vw-32px)] flex-col gap-3'>
      <AnimatePresence>
        {toasts.map((toast) => (
          <Toast key={toast.id} toast={toast} onClose={removeToast} />
        ))}
      </AnimatePresence>
    </div>
  );
}
