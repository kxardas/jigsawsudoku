import { useState } from "react";
import { getAverageRating, getRating, setRating } from "../api/ratingApi";
import type { Rating } from "../types/rating";
import { useToast } from "../context/ToastContext";
import { getApiErrorMessage } from "../utils/apiError";

export function useRating() {
  const [averageRating, setAverageRating] = useState<number>(0);
  const [currentRating, setCurrentRating] = useState<number>(0);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  const { showToast } = useToast();

  async function loadAverageRating(game: string) {
    try {
      setLoading(true);
      setError("");

      const gameAverage = await getAverageRating(game);
      setAverageRating(gameAverage);
    } catch {
      setError("Could not get average rating");

      showToast({
        type: "error",
        message: "Could not get average rating",
      });
    } finally {
      setLoading(false);
    }
  }

  async function loadPlayerRating(game: string, username: string) {
    if (!username.trim()) {
      setCurrentRating(0);
      return;
    }

    try {
      setLoading(true);
      setError("");

      const currentPlayerRating = await getRating(game, username);
      setCurrentRating(currentPlayerRating);
    } catch {
      setCurrentRating(0);
      setError("Could not get current player rating");

      showToast({
        type: "error",
        message: "Could not get current player rating",
      });
    } finally {
      setLoading(false);
    }
  }

  async function submitRating(rating: Rating) {
    try {
      setSubmitting(true);
      setError("");

      await setRating(rating);

      showToast({
        type: "success",
        message: "Rating was set successfully",
      });

      await loadAverageRating(rating.game);
      await loadPlayerRating(rating.game, rating.username);
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not set new rating.");

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
    loading,
    submitting,
    error,
    averageRating,
    currentRating,
    loadAverageRating,
    loadPlayerRating,
    submitRating,
  };
}
