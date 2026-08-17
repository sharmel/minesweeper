import { useCallback, useEffect, useState } from "react";
import {
    createGame,
    revealCell,
    toggleFlag
} from "../api.js";

import GameBoard from "./GameBoard.jsx";
import GameControls from "./GameControls";

const DEFAULT_GAME = {
    rows: 9,
    columns: 9,
    mines: 10
};

export default function App() {

    const [game, setGame] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const startGame = useCallback(async () => {

        setLoading(true);
        setError("");

        try {
            const newGame = await createGame(
                DEFAULT_GAME.rows,
                DEFAULT_GAME.columns,
                DEFAULT_GAME.mines
            );

            setGame(newGame);

        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }

    }, []);

    useEffect(() => {
        startGame();
    }, [startGame]);

    async function handleReveal(row, column) {

        if (!game || loading) {
            return;
        }

        setLoading(true);
        setError("");

        try {

            const updatedGame =
                await revealCell(
                    game.id,
                    row,
                    column
                );

            setGame(updatedGame);

        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    async function handleFlag(row, column) {

        if (!game || loading) {
            return;
        }

        setError("");

        try {

            const updatedGame =
                await toggleFlag(
                    game.id,
                    row,
                    column
                );

            setGame(updatedGame);

        } catch (err) {
            setError(err.message);
        }
    }

    return (
        <main className="app">

            <header>
                <h1>Minesweeper</h1>

                <p>
                    Minesweeper
                    coding exercise
                </p>
            </header>

            <GameControls
                game={game}
                onNewGame={startGame}
                disabled={loading}
            />

            {error && (
                <div
                    className="error"
                    role="alert"
                >
                    {error}
                </div>
            )}

            {loading && (
                <p aria-live="polite">
                    Loading…
                </p>
            )}

            {game && (
                <GameBoard
                    game={game}
                    onReveal={handleReveal}
                    onFlag={handleFlag}
                    disabled={loading}
                />
            )}

        </main>
    );
}
