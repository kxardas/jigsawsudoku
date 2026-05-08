export type AuthUser = {
  id: number;
  username: string;
  role: string;
};

export type AuthResponse = {
  user: AuthUser;
};

export type LoginRequest = {
  username: string;
  password: string;
};

export type RegisterRequest = {
  username: string;
  password: string;
  confirmPassword: string;
};

export type AuthState = {
  user: AuthUser | null;
  isAuthenticated: boolean;
  loading: boolean;
}