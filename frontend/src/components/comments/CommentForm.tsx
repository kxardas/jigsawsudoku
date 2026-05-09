import { useState } from "react";
import { Send } from "lucide-react";
import { Button } from "../ui/Button";
import { Textarea } from "../ui/Textarea";

type CommentFormProps = {
  submitting?: boolean;
  disabled?: boolean;
  onSubmit: (text: string) => Promise<void> | void;
};

export function CommentForm({ submitting = false, disabled = false, onSubmit }: CommentFormProps) {
  const [text, setText] = useState("");
  const [error, setError] = useState("");

  async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
    event.preventDefault();

    const trimmedText = text.trim();

    if (!trimmedText) {
      setError("Comment cannot be empty.");
      return;
    }

    if (trimmedText.length < 3) {
      setError("Comment is too short.");
      return;
    }

    setError("");

    await onSubmit(trimmedText);

    setText("");
  }

  return (
    <form
      onSubmit={handleSubmit}
      className='space-y-4 rounded-[1.75rem] border border-white/10 bg-[var(--bg-color)] p-3 shadow-[inset_0_1px_0_rgba(255,255,255,0.035),0_14px_34px_rgba(0,0,0,0.16)] sm:p-4'
    >
      <Textarea
        label='Write a comment'
        value={text}
        disabled={disabled || submitting}
        onChange={(event) => setText(event.target.value)}
        rows={4}
        className={`min-h-32 rounded-[1.35rem] ${
          error ? "border-red-500/50 focus:border-red-400" : "border-white/10"
        }`}
      />

      {error && <p className='px-1 text-sm font-medium text-red-300'>{error}</p>}

      <div className='flex justify-end'>
        <Button
          type='submit'
          disabled={disabled || submitting}
          className='h-11 w-full rounded-2xl px-5 text-[var(--bg-color)] shadow-lg hover:brightness-110 sm:w-auto'
        >
          <Send className='h-4 w-4' />
          {submitting ? "Publishing..." : "Publish"}
        </Button>
      </div>
    </form>
  );
}
