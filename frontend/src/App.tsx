import { useEffect, useRef, useState } from "react";
import { ToastContainer } from "./components/ui/ToastContainer";
import { NewGameModal } from "./components/game/NewGameModal";
import { GamePanel } from "./components/game/GamePanel";
import { useGame } from "./hooks/useGame";
import { LeaderboardPanel } from "./components/leaderboard/LeaderboardPanel";
import { RatingPanel } from "./components/rating/RatingPanel";
import { CommentsPanel } from "./components/comments/CommentsPanel";
import { AppHeader } from "./components/layout/AppHeader";
import { PageLayout } from "./components/layout/PageLayout";
import { AppGrid } from "./components/layout/AppGrid";
import { AppErrorState } from "./components/layout/AppErrorState";
import { AuthModal } from "./components/auth/AuthModal";
import { VictoryModal } from "./components/game/VictoryModal";
import { useAuth } from "./context/AuthContext";
import { useToast } from "./context/ToastContext";
import { submitGameScore } from "./api/gameApi";

export default function App() {
  const [hasShownVictoryModal, setHasShownVictoryModal] = useState(false);
  const [leaderboardReloadKey, setLeaderboardReloadKey] = useState(0);
  const [isNewGameModalOpen, setIsNewGameModalOpen] = useState(false);
  const [isVictoryModalOpen, setIsVictoryModalOpen] = useState(false);
  const [scoreSubmitting, setScoreSubmitting] = useState(false);
  const [scoreSubmitted, setScoreSubmitted] = useState(false);
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const hasStartedInitialGame = useRef(false);

  const {
    game,
    selectedCell,
    boardSize,
    difficulty,
    loading,
    error,
    displayElapsedSeconds,
    notes,
    notesMode,

    setNotesMode,
    setBoardSize,
    setDifficulty,
    startNewGame,
    selectCell,
    placeNumber,
    clearSelectedCell,
    resetBoard,
    solveCurrentGame,
    useHint,
  } = useGame();

  const { user, isAuthenticated } = useAuth();

  const { showToast } = useToast();

  useEffect(() => {
    if (hasStartedInitialGame.current) {
      return;
    }

    hasStartedInitialGame.current = true;

    startNewGame(boardSize, difficulty);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (game?.state === "SOLVED" && !hasShownVictoryModal) {
      setIsVictoryModalOpen(true);
      setHasShownVictoryModal(true);
    }

    if (game?.state !== "SOLVED") {
      setHasShownVictoryModal(false);
    }
  }, [game?.state, hasShownVictoryModal]);

  async function handleStartNewGame() {
    setScoreSubmitted(false);
    setScoreSubmitting(false);

    await startNewGame(boardSize, difficulty);
    setIsNewGameModalOpen(false);
  }

  async function handleSubmitScore() {
    if (!game || !user || !isAuthenticated) {
      setIsAuthModalOpen(true);
      return;
    }

    try {
      setScoreSubmitting(true);

      await submitGameScore(game.gameId);

      setScoreSubmitted(true);
      setLeaderboardReloadKey((current) => current + 1);

      showToast({
        type: "success",
        message: "Score submitted",
      });
    } catch {
      showToast({
        type: "error",
        message: "Could not submit score",
      });
    } finally {
      setScoreSubmitting(false);
    }
  }

  return (
    <>
      <ToastContainer />

      <NewGameModal
        isOpen={isNewGameModalOpen}
        boardSize={boardSize}
        difficulty={difficulty}
        loading={loading}
        onBoardSizeChange={setBoardSize}
        onDifficultyChange={setDifficulty}
        onClose={() => setIsNewGameModalOpen(false)}
        onStart={handleStartNewGame}
      />

      <VictoryModal
        isOpen={isVictoryModalOpen}
        game={game}
        submitted={scoreSubmitted}
        submitting={scoreSubmitting}
        onClose={() => setIsVictoryModalOpen(false)}
        onSubmitScore={handleSubmitScore}
        onOpenAuthModal={() => {
          setIsAuthModalOpen(true);
        }}
      />

      <AuthModal isOpen={isAuthModalOpen} onClose={() => setIsAuthModalOpen(false)} />

      <PageLayout>
        <AppHeader onOpenModal={() => setIsAuthModalOpen(true)} />

        {error && !game && (
          <AppErrorState error={error} onStartNewGame={() => setIsNewGameModalOpen(true)} />
        )}

        <AppGrid
          sidebar={
            <>
              <RatingPanel /> <LeaderboardPanel reloadKey={leaderboardReloadKey} /> 
            </>
          }
        >
          {game && (
            <GamePanel
              game={game}
              selectedCell={selectedCell}
              loading={loading}
              onOpenNewGameModal={() => setIsNewGameModalOpen(true)}
              onSolve={solveCurrentGame}
              onClearBoard={resetBoard}
              onHint={useHint}
              onSelectCell={selectCell}
              onNumberClick={placeNumber}
              onClear={clearSelectedCell}
              displayElapsedSeconds={displayElapsedSeconds}
              notes={notes}
              notesMode={notesMode}
              onNotesModeChange={setNotesMode}
            />
          )}

          <CommentsPanel />
        </AppGrid>
      </PageLayout>
    </>
  );
}
