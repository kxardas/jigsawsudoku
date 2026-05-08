import { useEffect, useState } from "react";
import { getTopScores } from "../api/scoreApi";
import type { Score } from "../types/score";
import { GAME_NAME } from "../utils/constants";
import { useToast } from "../context/ToastContext";

export function useLeaderboard() {
  const [scores, setScores] = useState<Score[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const { showToast } = useToast();

  async function loadScores() {
    try {
      setLoading(true);
      setError("");

      const topScores = await getTopScores(GAME_NAME);
      setScores(topScores);
    } catch {
      setError("Could not load leaderboard");

      showToast({
        type: "error",
        message: "Could not load leaderboard",
      });
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadScores();
  }, []);

  return {
    scores,
    loading,
    error,
    reload: loadScores
  }
}
