export default function Cell({
                                 cell,
                                 onReveal,
                                 onFlag
                             }) {
    function handleClick() {
        if (!cell.revealed && !cell.flagged) {
            onReveal(cell.row, cell.column);
        }
    }
    function handleContextMenu(event) {
        event.preventDefault();
        onFlag(
            cell.row,
            cell.column
        );
    }
    let content = "";
    if (cell.flagged) {
        content = "🚩";
    } else if (cell.revealed && cell.mine) {
        content = "💣";
    } else if (cell.revealed && cell.adjacentMines > 0) {
        content = cell.adjacentMines;
    }
    return (
        <button
            type="button"
            className={`cell ${
                cell.revealed ? "revealed" : ""
            }`}
            aria-label={
                cell.revealed
                    ? `Cell ${cell.row + 1}, ${cell.column + 1}`
                    : "Hidden cell"
            }
            onClick={handleClick}
            onContextMenu={handleContextMenu}
        >
            {content}
        </button>
    );
}
