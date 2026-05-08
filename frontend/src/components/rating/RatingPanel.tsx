import { useEffect } from "react";
import { DEFAULT_PLAYER_NAME, GAME_NAME } from "../../utils/constants";
import { getCurrentIsoDate } from "../../utils/date";
import { useRating } from "../../hooks/useRating";
import { Card } from "../ui/Card";
import { ErrorMessage } from "../ui/ErrorMessage";
import { RatingSkeleton } from "./RatingSkeleton";
import { RatingStars } from "./RatingStars";
import { useAuth } from "../../context/AuthContext";
import { EmptyState } from "../ui/EmptyState";

export function RatingPanel() {
  const { user, isAuthenticated } = useAuth();
  const username = user?.username ?? DEFAULT_PLAYER_NAME;
  const {
    loading,
    submitting,
    error,
    averageRating,
    currentRating,
    loadAverageRating,
    loadPlayerRating,
    submitRating,
  } = useRating();

  useEffect(() => {
    loadAverageRating(GAME_NAME);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    loadPlayerRating(GAME_NAME, username);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [username]);

  async function handleRatingChange(value: number) {
    await submitRating({
      game: GAME_NAME,
      username,
      value,
      createdAt: getCurrentIsoDate(),
    });
  }

  return (
    <Card className='space-y-5 overflow-hidden'>
      <div>
        <h2 className='text-2xl font-bold text-[var(--text-color)]'>Rating</h2>
        <p className='mt-1 text-sm text-[var(--sub-color)]'>Community score for this game.</p>
      </div>

      {error && <ErrorMessage message={error} />}

      {loading ? (
        <RatingSkeleton />
      ) : (
        <div className='space-y-4'>
          <div className='rounded-2xl bg-[var(--bg-color)] p-5'>
            <div className='flex items-center gap-4'>
              <div className='flex h-16 w-16 shrink-0 items-center justify-center rounded-2xl bg-yellow-400/10 text-3xl font-black text-yellow-300'>
                {averageRating.toFixed(1)}
              </div>

              <div className='min-w-0'>
                <RatingStars value={averageRating} disabled />

                <p className='mt-2 text-sm text-[var(--sub-color)]'>Average rating from players</p>
              </div>
            </div>
          </div>
          {isAuthenticated ? (
            <div className='rounded-2xl border border-[var(--border-color)] bg-[var(--bg-color)] p-5'>
              <div className='mb-3'>
                <p className='text-sm font-bold text-[var(--text-color)]'>Your rating</p>

                <p className='mt-1 text-sm text-[var(--sub-color)]'>
                  {isAuthenticated
                    ? "Click a star to update your rating."
                    : "Sign in later to rate this game."}
                </p>
              </div>
              <RatingStars
                value={currentRating}
                disabled={submitting}
                onChange={handleRatingChange}
              />

              {submitting && (
                <p className='mt-3 text-sm text-[var(--sub-color)]'>Saving rating...</p>
              )}
            </div>
          ) : (
            <EmptyState
              title='Sign in to rate game'
              description='You need an account to leave rating.'
            />
          )}
        </div>
      )}
    </Card>
  );
}
