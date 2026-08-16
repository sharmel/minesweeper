import Cell from "./Cell";

export default function GameBoard({
                                      game,
                                      onReveal,
                                      onFlag
                                  }) {
    return (
        <section
            aria-label="Minesweeper game board"
            className="board"
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
                />

            ))}
        </section>
    );
}
