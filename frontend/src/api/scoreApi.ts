import { axiosInstance } from "./client";
import type { Score } from "../types/score";

export async function getTopScores(game: string): Promise<Score[]> {
  const res = await axiosInstance.get<Score[]>(`/score/${encodeURIComponent(game)}`);

  return res.data;
}