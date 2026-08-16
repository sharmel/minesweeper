export default function GameControls({
                                         game,
                                         onNewGame
                                     }) {

    return (
        <section className="controls">

            <button
                type="button"
                onClick={onNewGame}
            >
                New game
            </button>

            {game && (
                <div
                    aria-live="polite"
                    className="status"
                >
                    Status: {game.status}
                </div>
            )}

        </section>
    );
}
