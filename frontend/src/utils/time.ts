export function formatDurationString(totalSeconds: number) {
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;

  return totalSeconds <= 60 ? `${seconds.toString()}s` : `${minutes}m ${seconds.toString()}s`;
}

export function formatDurationClock(totalSeconds: number) {
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;

  return `${minutes}:${seconds.toString().padStart(2, "0")}`;
}