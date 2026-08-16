export default function GameControls({
     game,
     onNewGame,
     disabled
 }) {
    if (!game) {
        return null;
    }
    const statusText = {
        IN_PROGRESS: "Game in progress",
        WON: "You won!",
        LOST: "Game over"
    }[game.status];

    return (
        <section
            className="controls"
            aria-label="Game controls"
        >
            <button
                type="button"
                onClick={onNewGame}
                disabled={disabled}
            >
                New game
            </button>

            <div
                className="game-info"
                aria-live="polite"
            >
                <span>
                    Mines remaining: {game.flagsRemaining}
                </span>

                <span>
                    {statusText}
                </span>
            </div>

        </section>
    );
}
