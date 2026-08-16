import Cell from "./Cell";

export default function GameBoard({
      game,
      onReveal,
      onFlag,
      disabled
  }) {
    return (
        <section
            className="board-wrapper"
            aria-label="Minesweeper board"
        >
            <div
                className="board"
                role="grid"
                aria-rowcount={game.rows}
                aria-colcount={game.columns}
                style={{
                    gridTemplateColumns:
                        `repeat(${game.columns}, 40px)`
                }}
            >
                {game.board.flat().map(cell => (
                    <Cell
                        key={`${cell.row}-${cell.column}`}
                        cell={cell}
                        onReveal={onReveal}
                        onFlag={onFlag}
                        disabled={disabled}
                    />
                ))}
            </div>
        </section>
    );
}
