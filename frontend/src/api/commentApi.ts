import { axiosInstance } from "./client";
import type { Comment } from "../types/comment";

export async function getComments(game: string): Promise<Comment[]> {
  const res = await axiosInstance.get<Comment[]>(`/comment/${encodeURIComponent(game)}`);

  return res.data;
}

export async function addComment(req: Comment): Promise<void> {
  await axiosInstance.post("/comment", req);
}
