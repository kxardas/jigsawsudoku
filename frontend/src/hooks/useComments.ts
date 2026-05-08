import { useState } from "react";
import { getComments, addComment } from "../api/commentApi";
import type { Comment } from "../types/comment";
import { useToast } from "../context/ToastContext";
import { getApiErrorMessage } from "../utils/apiError";

export function useComments() {
  const [comments, setComments] = useState<Comment[]>([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  const { showToast } = useToast();

  async function loadComments(game: string) {
    try {
      setLoading(true);
      setError("");

      const allComments = await getComments(game);
      setComments(allComments);
    } catch {
      setError("Could not load comments");

      showToast({
        type: "error",
        message: "Could not load comments",
      });
    } finally {
      setLoading(false);
    }
  }

  async function submitComment(comment: Comment) {
    try {
      setSubmitting(true);
      setError("");

      await addComment(comment);

      showToast({
        type: "success",
        message: "Comment added successfully!",
      });

      await loadComments(comment.game);
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not add comment.");

      setError(message);

      showToast({
        type: "error",
        message,
      });
    } finally {
      setSubmitting(false);
    }
  }

  return {
    comments,
    loading,
    submitting,
    error,

    loadComments,
    submitComment,
  };
}
