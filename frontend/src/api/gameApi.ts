import { axiosInstance } from "./client";
import type { CreateGameRequest, GameState, MoveRequest } from "../types/game";
import type { Score } from "../types/score";

export async function createGame(req: CreateGameRequest): Promise<GameState> {
  const res = await axiosInstance.post<GameState>("/engine/games", req);

  return res.data;
}

export async function getGame(gameId: string): Promise<GameState> {
  const res = await axiosInstance.get<GameState>(`/engine/games/${encodeURIComponent(gameId)}`);

  return res.data;
}

export async function makeMove(gameId: string, req: MoveRequest): Promise<GameState> {
  const res = await axiosInstance.post<GameState>(
    `/engine/games/${encodeURIComponent(gameId)}/move`,
    req,
  );

  return res.data;
}

export async function clearCell(gameId: string, row: number, col: number): Promise<GameState> {
  const res = await axiosInstance.post<GameState>(
    `/engine/games/${encodeURIComponent(gameId)}/clear`,
    null,
    {
      params: {
        row,
        col,
      },
    },
  );

  return res.data;
}

export async function solveGame(gameId: string): Promise<GameState> {
  const res = await axiosInstance.post<GameState>(
    `/engine/games/${encodeURIComponent(gameId)}/solve`,
  );

  return res.data;
}

export async function clearBoard(gameId: string): Promise<GameState> {
  const res = await axiosInstance.post<GameState>(`/engine/games/${encodeURIComponent(gameId)}/reset`);

  return res.data;
}

export async function requestHint(gameId: string): Promise<GameState> {
  const res = await axiosInstance.post<GameState>(`/engine/games/${encodeURIComponent(gameId)}/hint`);

  return res.data;
}

export async function submitGameScore(gameId: string): Promise<Score> {
  const res = await axiosInstance.post<Score>(`/engine/games/${encodeURIComponent(gameId)}/score`);

  return res.data;
}