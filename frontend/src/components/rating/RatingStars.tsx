import { Star } from "lucide-react";

type RatingStarsProps = {
  value: number;
  disabled?: boolean;
  onChange?: (value: number) => void;
};

export function RatingStars({
  value,
  disabled = false,
  onChange,
}: RatingStarsProps) {
  const stars = [1, 2, 3, 4, 5];

  function handleClick(starValue: number) {
    if (disabled || !onChange) {
      return;
    }

    onChange(starValue);
  }

  function getFillPercent(starValue: number) {
    const diff = value - (starValue - 1);

    if (diff >= 1) {
      return 100;
    }

    if (diff <= 0) {
      return 0;
    }

    return diff * 100;
  }

  return (
    <div className="flex items-center gap-1">
      {stars.map((starValue) => {
        const fillPercent = getFillPercent(starValue);

        return (
          <button
            key={starValue}
            type="button"
            disabled={disabled}
            onClick={() => handleClick(starValue)}
            className={`
              rounded-md transition
              disabled:cursor-default
              ${
                disabled
                  ? ""
                  : "cursor-pointer hover:scale-110 hover:bg-white/5"
              }
            `}
            aria-label={`Rate ${starValue} stars`}
          >
            <span className="relative block h-6 w-6">
              <Star
                size={24}
                className="absolute inset-0 fill-transparent text-[var(--sub-color)]"
              />

              <span
                className="absolute inset-0 overflow-hidden"
                style={{ width: `${fillPercent}%` }}
              >
                <Star
                  size={24}
                  className="fill-yellow-400 text-yellow-400"
                />
              </span>
            </span>
          </button>
        );
      })}
    </div>
  );
}