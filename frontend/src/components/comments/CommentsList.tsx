import type { Comment } from "../../types/comment";
import { formatDate } from "../../utils/date";
import { EmptyState } from "../ui/EmptyState";

type CommentsListProps = {
  comments: Comment[];
};

function getInitials(userName: string) {
  const trimmed = userName.trim();

  if (!trimmed) {
    return "?";
  }

  return trimmed.slice(0, 1).toUpperCase();
}

export function CommentsList({ comments }: CommentsListProps) {
  if (comments.length === 0) {
    return (
      <EmptyState
        title='No comments yet'
        description='Be the first one to share your thoughts.'
        className='rounded-[1.75rem]'
      />
    );
  }

  return (
    <div className='space-y-3'>
      {comments.map((comment) => (
        <article
          key={comment.ident ?? `${comment.userName}-${comment.createdAt}`}
          className='group min-w-0 rounded-[1.75rem] border border-white/10 bg-[var(--bg-color)] p-4 shadow-[0_10px_28px_rgba(0,0,0,0.12)] transition-all duration-200 hover:-translate-y-0.5 hover:border-white/20 hover:shadow-[0_16px_34px_rgba(0,0,0,0.16)]'
        >
          <div className='flex min-w-0 gap-3'>
            <div className='flex h-11 w-11 shrink-0 items-center justify-center rounded-2xl bg-[var(--accent-color)] text-sm font-black text-[var(--bg-color)] shadow-lg'>
              {getInitials(comment.userName)}
            </div>

            <div className='min-w-0 flex-1'>
              <div className='flex min-w-0 flex-wrap items-center justify-between gap-2'>
                <div className='min-w-0'>
                  <p className='min-w-0 max-w-full truncate font-bold text-[var(--text-color)]'>
                    {comment.userName}
                  </p>
                </div>

                <time className='shrink-0 rounded-full border border-white/10 bg-[var(--sub-alt-color)] px-2.5 py-1 text-xs text-[var(--sub-color)]'>
                  {formatDate(comment.createdAt)}
                </time>
              </div>

              <p className='mt-2 whitespace-pre-wrap break-words text-sm leading-relaxed text-[var(--text-color)]/90'>
                {comment.text}
              </p>
            </div>
          </div>
        </article>
      ))}
    </div>
  );
}
