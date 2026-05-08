import { axiosInstance } from "./client";
import type { Rating } from "../types/rating";

export async function getAverageRating(game: string): Promise<number> {
  const res = await axiosInstance.get(`/rating/${encodeURIComponent(game)}`);

  return res.data;
}

export async function getRating(game: string, username: string): Promise<number> {
  const res = await axiosInstance.get(
    `/rating/${encodeURIComponent(game)}/${encodeURIComponent(username)}`,
  );

  return res.data;
}

export async function setRating(req: Rating): Promise<void> {
  await axiosInstance.post("/rating", req);
}
