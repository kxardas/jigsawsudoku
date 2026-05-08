import { axiosInstance } from "./client";
import type { AuthResponse, AuthUser, LoginRequest, RegisterRequest } from "../types/auth";

export async function login(req: LoginRequest): Promise<AuthResponse> {
  const res = await axiosInstance.post<AuthResponse>("/auth/login", req);

  return res.data;
}

export async function register(req: RegisterRequest): Promise<AuthResponse> {
  const res = await axiosInstance.post<AuthResponse>("/auth/register", req);

  return res.data;
}

export async function getCurrentUser(): Promise<AuthUser> {
  const res = await axiosInstance.get<AuthUser>("/auth/me");

  return res.data;
}

export async function logout(): Promise<void> {
  await axiosInstance.post("/auth/logout");
}
