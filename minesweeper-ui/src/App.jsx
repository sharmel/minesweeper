import { useEffect, useState } from "react";
import { createGame, revealCell, toggleFlag } from "./api";
import GameBoard from "./components/GameBoard";
import GameControls from "./components/GameControls";

export default function App() {
    const [game, setGame] = useState(null);
    const [error, setError] = useState(null);

    async function startGame() {
        try {
            setError(null);
            const newGame = await createGame(
                9,
                9,
                10
            );
            setGame(newGame);
        } catch (err) {
            setError(err.message);
        }
    }

    async function handleReveal(row, column) {
        if (!game) {
            return;
        }
        try {
            const updatedGame = await revealCell(
                game.id,
                row,
                column
            );
            setGame(updatedGame);
        } catch (err) {
            setError(err.message);
        }
    }

    async function handleFlag(row, column) {
        if (!game) {
            return;
        }
        try {
            const updatedGame = await toggleFlag(
                game.id,
                row,
                column
            );
            setGame(updatedGame);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        startGame();
    }, []);
    return (
        <main className="app">

            <header>
                <h1>Minesweeper</h1>
                <p>ReStart coding exercise</p>
            </header>
            <GameControls
                game={game}
                onNewGame={startGame}
            />
            {error && (
                <div
                    role="alert"
                    className="error"
                >
                    {error}
                </div>
            )}
            {game && (
                <GameBoard
                    game={game}
                    onReveal={handleReveal}
                    onFlag={handleFlag}
                />
            )}
        </main>
    );
}
