import { useEffect } from "react";
import { useComments } from "../../hooks/useComments";
import { GAME_NAME } from "../../utils/constants";
import { getCurrentIsoDate } from "../../utils/date";
import { Card } from "../ui/Card";
import { ErrorMessage } from "../ui/ErrorMessage";
import { CommentForm } from "./CommentForm";
import { CommentsList } from "./CommentsList";
import { CommentsSkeleton } from "./CommentsSkeleton";
import { useAuth } from "../../context/AuthContext";
import { EmptyState } from "../ui/EmptyState";

export function CommentsPanel() {
  const { user, isAuthenticated } = useAuth();
  const username = user?.username;
  const { comments, loading, submitting, error, loadComments, submitComment } = useComments();

  useEffect(() => {
    loadComments(GAME_NAME);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <Card className='space-y-6 rounded-[2rem]'>
      <div className='flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between'>
        <div className='flex flex-col gap-1'>
          <h2 className='text-2xl font-bold text-[var(--text-color)]'>Comments</h2>

          <p className='text-sm text-[var(--sub-color)]'>Discuss the game with other players.</p>
        </div>

        <div className='w-fit rounded-full border border-white/10 bg-[var(--sub-alt-color)] px-3 py-1 text-xs font-bold text-[var(--sub-color)]'>
          {comments.length} {comments.length === 1 ? "comment" : "comments"}
        </div>
      </div>

      {error && <ErrorMessage message={error} />}

      {isAuthenticated && username ? (
        <CommentForm
          submitting={submitting}
          onSubmit={async (text) => {
            await submitComment({
              game: GAME_NAME,
              userName: username,
              text,
              createdAt: getCurrentIsoDate(),
            });
          }}
        />
      ) : (
        <EmptyState
          title='Sign in to comment'
          description='You can read comments as guest, but you need an account to write one.'
          className='rounded-[1.75rem]'
        />
      )}

      <div className='pr-1'>
        {loading ? <CommentsSkeleton /> : <CommentsList comments={comments} />}
      </div>
    </Card>
  );
}
