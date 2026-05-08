import { useState } from "react";
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
    <form onSubmit={handleSubmit} className='space-y-3 rounded-2xl bg-[var(--bg-color)] p-4'>
      <Textarea
        label='Write a comment'
        value={text}
        disabled={disabled || submitting}
        onChange={(event) => setText(event.target.value)}
        rows={4}
        className={error ? "border-red-500/40" : "border-white/10"}
      />

      {error && <p className='text-sm text-red-300'>{error}</p>}

      <div className='flex justify-end'>
        <Button type='submit' disabled={disabled || submitting}>
          {submitting ? "Publishing..." : "Publish"}
        </Button>
      </div>
    </form>
  );
}
