import type { BoardSize, GameDifficulty, GameState, SelectedCell } from "../types/game";
import {
  createGame,
  makeMove,
  clearCell,
  solveGame,
  clearBoard,
  requestHint,
  getGame,
} from "../api/gameApi";
import { ACTIVE_GAME_ID_STORAGE_KEY } from "../utils/constants";
import { useEffect, useState } from "react";
import { useToast } from "../context/ToastContext";
import { getApiErrorMessage } from "../utils/apiError";

function createEmptyNotes(size: number) {
  return Array.from({ length: size }, () => Array.from({ length: size }, () => [] as number[]));
}

function clearNotesForFilledCells(notes: number[][][], game: GameState) {
  return notes.map((rowNotes, rowIndex) =>
    rowNotes.map((cellNotes, colIndex) => {
      if (game.board[rowIndex][colIndex] !== 0) {
        return [];
      }

      return cellNotes;
    }),
  );
}

export function useGame() {
  const [game, setGame] = useState<GameState | null>(null);
  const [selectedCell, setSelectedCell] = useState<SelectedCell | null>(null);
  const [boardSize, setBoardSize] = useState<BoardSize>(5);
  const [difficulty, setDifficulty] = useState<GameDifficulty>("EASY");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [displayElapsedSeconds, setDisplayElapsedSeconds] = useState(0);
  const [notes, setNotes] = useState<number[][][]>([]);
  const [notesMode, setNotesMode] = useState(false);

  const { showToast } = useToast();

  useEffect(() => {
    if (!game) {
      setDisplayElapsedSeconds(0);
      return;
    }

    setDisplayElapsedSeconds(game.elapsedSeconds);

    if (game.state !== "PLAYING") {
      return;
    }

    const intervalId = window.setInterval(() => {
      setDisplayElapsedSeconds((current) => current + 1);
    }, 1000);

    return () => window.clearInterval(intervalId);
  }, [game?.gameId, game?.state, game?.elapsedSeconds]);

  async function startNewGame(
    selectedSize: BoardSize = boardSize,
    selectedDifficulty: GameDifficulty = difficulty,
  ) {
    try {
      setLoading(true);
      setError("");
      setSelectedCell(null);
      setDisplayElapsedSeconds(0);

      const newGame = await createGame({
        size: selectedSize,
        difficulty: selectedDifficulty,
      });

      setGame(newGame);
      setNotes(createEmptyNotes(newGame.size));
      setNotesMode(false);

      localStorage.setItem(ACTIVE_GAME_ID_STORAGE_KEY, newGame.gameId);

      setDisplayElapsedSeconds(newGame.elapsedSeconds);
      setBoardSize(selectedSize);
      setDifficulty(selectedDifficulty);

      showToast({
        type: "success",
        message: "New game created",
      });
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not create game.");

      setError(message);

      showToast({
        type: "error",
        message,
      });
    } finally {
      setLoading(false);
    }
  }

  async function loadSavedGame() {
    const savedGameId = localStorage.getItem(ACTIVE_GAME_ID_STORAGE_KEY);

    if (!savedGameId) {
      return false;
    }

    try {
      setLoading(true);
      setError("");

      const savedGame = await getGame(savedGameId);

      setGame(savedGame);
      setNotes(createEmptyNotes(savedGame.size));
      setNotesMode(false);

      setBoardSize(savedGame.size);
      setDifficulty(savedGame.difficulty);
      setSelectedCell(null);
      setDisplayElapsedSeconds(savedGame.elapsedSeconds);

      return true;
    } catch {
      localStorage.removeItem(ACTIVE_GAME_ID_STORAGE_KEY);
      return false;
    } finally {
      setLoading(false);
    }
  }

  function selectCell(row: number, col: number) {
    if (!game) {
      showToast({
        type: "error",
        message: "Game is not loaded",
      });
      return;
    }

    if (game.fixedCells[row][col]) {
      return;
    }

    if (row === selectedCell?.row && col === selectedCell.col) {
      setSelectedCell(null);
      return;
    }

    setSelectedCell({ row, col });
  }

  function toggleNote(row: number, col: number, value: number) {
    setNotes((currentNotes) => {
      const baseNotes =
        currentNotes.length === game?.size
          ? currentNotes
          : createEmptyNotes(game?.size ?? boardSize);

      const nextNotes = baseNotes.map((notesRow) => notesRow.map((cellNotes) => [...cellNotes]));

      const cellNotes = nextNotes[row][col];

      if (cellNotes.includes(value)) {
        nextNotes[row][col] = cellNotes.filter((note) => note !== value);
      } else {
        nextNotes[row][col] = [...cellNotes, value].sort((a, b) => a - b);
      }

      return nextNotes;
    });
  }

  async function placeNumber(value: number) {
    if (!game) {
      showToast({
        type: "error",
        message: "Game is not loaded",
      });
      return;
    }

    if (game.state !== "PLAYING") {
      showToast({
        type: "info",
        message: "This game is already finished",
      });
      return;
    }

    if (!selectedCell) {
      showToast({
        type: "info",
        message: "Select a cell first",
      });
      return;
    }

    if (notesMode) {
      const currentValue = game.board[selectedCell.row][selectedCell.col];

      if (currentValue !== 0) {
        showToast({
          type: "info",
          message: "Notes can be added only to empty cells",
        });
        return;
      }

      toggleNote(selectedCell.row, selectedCell.col, value);
      return;
    }

    try {
      setError("");

      const updatedGame = await makeMove(game.gameId, {
        row: selectedCell.row,
        col: selectedCell.col,
        value,
      });

      setGame(updatedGame);
      setDisplayElapsedSeconds(updatedGame.elapsedSeconds);

      if (!updatedGame.lastMoveValid) {
        showToast({
          type: "error",
          message: updatedGame.message || "Invalid move",
        });
        return;
      }

      setNotes((currentNotes) => {
        const nextNotes = currentNotes.map((notesRow) =>
          notesRow.map((cellNotes) => [...cellNotes]),
        );

        if (nextNotes[selectedCell.row]?.[selectedCell.col]) {
          nextNotes[selectedCell.row][selectedCell.col] = [];
        }

        return clearNotesForFilledCells(nextNotes, updatedGame);
      });

      if (updatedGame.state === "SOLVED") {
        showToast({
          type: "success",
          message: "Puzzle solved!",
        });
      }
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not make move.");

      setError(message);

      showToast({
        type: "error",
        message,
      });
    }
  }

  async function resetBoard() {
    if (!game) {
      showToast({
        type: "error",
        message: "Game is not loaded",
      });
      return;
    }

    try {
      setError("");
      setSelectedCell(null);

      const updatedGame = await clearBoard(game.gameId);

      setGame(updatedGame);
      setDisplayElapsedSeconds(updatedGame.elapsedSeconds);
      setNotes(createEmptyNotes(updatedGame.size));
      setNotesMode(false);
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not reset board.");

      setError(message);

      showToast({
        type: "error",
        message,
      });
    }
  }

  async function useHint() {
    if (!game) {
      showToast({
        type: "error",
        message: "Game is not loaded",
      });
      return;
    }

    if (game.state !== "PLAYING") {
      showToast({
        type: "error",
        message: "Hints are only available while playing.",
      });
      return;
    }

    if (game.hintsLeft <= 0) {
      showToast({
        type: "error",
        message: "No hints left.",
      });
      return;
    }

    try {
      setLoading(true);
      setError("");

      const updatedGame = await requestHint(game.gameId);

      setGame(updatedGame);
      setDisplayElapsedSeconds(updatedGame.elapsedSeconds);

      setNotes((currentNotes) => clearNotesForFilledCells(currentNotes, updatedGame));

      if (updatedGame.hintsLeft === 0) {
        showToast({
          type: "warning",
          message: "Hint used. No more hints left",
        });
      } else {
        showToast({
          type: "success",
          message: "Hint used",
        });
      }

      if (updatedGame.state === "SOLVED") {
        showToast({
          type: "success",
          message: "Puzzle solved!",
        });
      }
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not use hint.");

      setError(message);

      showToast({
        type: "error",
        message,
      });
    } finally {
      setLoading(false);
    }
  }

  async function clearSelectedCell() {
    if (!game) {
      showToast({
        type: "error",
        message: "Game is not loaded",
      });
      return;
    }

    if (game.state !== "PLAYING") {
      showToast({
        type: "info",
        message: "This game is already finished",
      });
      return;
    }

    if (!selectedCell) {
      showToast({
        type: "info",
        message: "Select a cell first",
      });
      return;
    }

    try {
      setError("");

      const updatedGame = await clearCell(game.gameId, selectedCell.row, selectedCell.col);

      setGame(updatedGame);
      setDisplayElapsedSeconds(updatedGame.elapsedSeconds);

      if (!updatedGame.lastMoveValid) {
        showToast({
          type: "error",
          message: updatedGame.message || "Could not clear this cell",
        });
      }
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not clear cell.");

      setError(message);

      showToast({
        type: "error",
        message,
      });
    }
  }

  // ONLY FOR DEBUG !!!
  async function solveCurrentGame() {
    if (!game) {
      showToast({
        type: "error",
        message: "Game is not loaded",
      });
      return;
    }

    try {
      setError("");

      const updatedGame = await solveGame(game.gameId);

      setGame(updatedGame);
      setDisplayElapsedSeconds(updatedGame.elapsedSeconds);
      setNotes(createEmptyNotes(updatedGame.size));
      setNotesMode(false);

      showToast({
        type: "info",
        message: "Puzzle solved automatically",
      });
    } catch (error) {
      const message = getApiErrorMessage(error, "Could not solve game.");

      setError(message);

      showToast({
        type: "error",
        message,
      });
    }
  }

  return {
    game,
    selectedCell,
    boardSize,
    difficulty,
    loading,
    error,
    displayElapsedSeconds,
    notes,
    notesMode,

    setBoardSize,
    setDifficulty,
    setNotesMode,
    startNewGame,
    loadSavedGame,
    selectCell,
    placeNumber,
    clearSelectedCell,
    solveCurrentGame,
    resetBoard,
    useHint,
  };
}
