import { z } from "zod";

export const loginSchema = z.object({
  username: z.string().trim().min(1, "Username cannot be empty."),

  password: z.string().min(1, "Password cannot be empty"),
});

export const registerSchema = z
  .object({
    username: z
      .string()
      .trim()
      .min(4, "Username must be at least 4 characters.")
      .max(32, "Username cannot be longer than 32 characters.")
      .regex(/^[a-zA-Z0-9_]+$/, "Username can contain only letters, numbers and underscores."),

    password: z
      .string()
      .min(4, "Password must be at least 4 characters.")
      .max(72, "Password cannot be longer than 72 characters."),

    confirmPassword: z.string().min(1, "Confirm your password."),
  })
  .refine((data) => data.password === data.confirmPassword, {
    path: ["confirmPassword"],
    message: "Passwords do not match.",
  });

export type LoginFormValues = z.infer<typeof loginSchema>;
export type RegisterFormValues = z.infer<typeof registerSchema>;
