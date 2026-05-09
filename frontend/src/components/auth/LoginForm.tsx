import { useState, useEffect } from "react";
// import { LockKeyhole, ShieldCheck, Sparkles } from "lucide-react";
import { useToast } from "../../context/ToastContext";
import { useAuth } from "../../context/AuthContext";
import { Input } from "../ui/Input";
import { Button } from "../ui/Button";
import { loginSchema } from "../../validation/authSchemas";
import z from "zod";

type LoginFormProps = {
  onSuccess: () => void;
  onSwitchToRegister?: () => void;
};

export function LoginForm({ onSuccess, onSwitchToRegister }: LoginFormProps) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [usernameError, setUsernameError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [formError, setFormError] = useState("");

  const { login } = useAuth();
  const { showToast } = useToast();

  async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
    event.preventDefault();

    setUsernameError("");
    setPasswordError("");
    setFormError("");

    const result = loginSchema.safeParse({
      username,
      password,
    });

    if (!result.success) {
      const errors = z.treeifyError(result.error);

      const usernameError = errors.properties?.username?.errors[0];
      const passwordError = errors.properties?.password?.errors[0];

      setUsernameError("");
      setPasswordError("");
      setFormError("");

      if (usernameError) {
        setUsernameError(usernameError);
        return;
      }

      if (passwordError) {
        setPasswordError(passwordError);
        return;
      }

      setFormError(errors.errors[0] ?? "Invalid form data.");
      return;
    }

    try {
      setSubmitting(true);
      setUsernameError("");
      setPasswordError("");
      setFormError("");

      await login(result.data);

      showToast({
        type: "success",
        message: "Successfully logged in",
      });

      onSuccess();
    } catch {
      setFormError("Invalid username or password.");

      showToast({
        type: "error",
        message: "Invalid username or password.",
      });
    } finally {
      setSubmitting(false);
    }
  }

  useEffect(() => {
    if (!usernameError && !passwordError && !formError) {
      return;
    }

    const timeoutId = window.setTimeout(() => {
      setUsernameError("");
      setPasswordError("");
      setFormError("");
    }, 3500);

    return () => window.clearTimeout(timeoutId);
  }, [usernameError, passwordError, formError]);

  return (
    <form onSubmit={handleSubmit} className='space-y-4'>
      <div className='space-y-6'>
        <Input
          label='Username'
          value={username}
          disabled={submitting}
          error={usernameError}
          onErrorDismiss={() => setUsernameError("")}
          autoComplete='username'
          onChange={(event) => {
            setUsername(event.target.value);
            setUsernameError("");
          }}
        />

        <Input
          label='Password'
          type='password'
          value={password}
          disabled={submitting}
          error={passwordError}
          onErrorDismiss={() => setPasswordError("")}
          autoComplete='current-password'
          onChange={(event) => {
            setPassword(event.target.value);
            setPasswordError("");
          }}
        />
      </div>

      {formError && (
        <p className='rounded-xl border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-300'>
          {formError}
        </p>
      )}

      <Button type='submit' disabled={submitting} className='w-full'>
        {submitting ? "Logging in..." : "Log in"}
      </Button>

      <div className='flex items-center gap-3'>
        <div className='h-px flex-1 bg-white/10' />
        <p className='text-xs font-medium text-[var(--sub-color)]'>or</p>
        <div className='h-px flex-1 bg-white/10' />
      </div>

      <div className='grid grid-cols-2 gap-2'>
        <Button type='button' variant='ghost' disabled={submitting} className='w-full'>
          Google
        </Button>

        <Button type='button' variant='ghost' disabled={submitting} className='w-full'>
          GitHub
        </Button>
      </div>

      <p className='text-center text-sm text-[var(--sub-color)]'>
        New here?{" "}
        <button
          type='button'
          disabled={submitting}
          onClick={onSwitchToRegister}
          className='font-bold text-[var(--accent-color)] transition hover:brightness-125 disabled:cursor-not-allowed disabled:opacity-50'
        >
          Create account
        </button>
      </p>
    </form>
  );
}
