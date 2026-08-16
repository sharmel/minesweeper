export default function Cell({
     cell,
     onReveal,
     onFlag,
     disabled
 }) {
    const label = getAccessibleLabel(cell);
    function handleClick() {
        if (
            !disabled &&
            !cell.revealed &&
            !cell.flagged
        ) {
            onReveal(
                cell.row,
                cell.column
            );
        }
    }
    function handleContextMenu(event) {
        event.preventDefault();
        if (!disabled && !cell.revealed) {
            onFlag(
                cell.row,
                cell.column
            );
        }
    }
    function handleKeyDown(event) {
        /*
         * F = flag
         */
        if (
            event.key.toLowerCase() === "f"
            && !cell.revealed
        ) {
            event.preventDefault();

            onFlag(
                cell.row,
                cell.column
            );
        }
    }

    let content = "";
    if (cell.flagged) {
        content = "⚑";
    } else if (cell.revealed && cell.mine) {
        content = "💣";
    } else if (
        cell.revealed &&
        cell.adjacentMines > 0
    ) {
        content = cell.adjacentMines;
    }
    return (
        <button
            type="button"
            role="gridcell"
            className={
                `cell ${
                    cell.revealed
                        ? "revealed"
                        : "hidden"
                }`
            }
            aria-label={label}
            aria-pressed={cell.flagged}
            disabled={disabled}
            onClick={handleClick}
            onContextMenu={handleContextMenu}
            onKeyDown={handleKeyDown}
        >
            {content}
        </button>
    );
}

function getAccessibleLabel(cell) {

    const position =
        `Row ${cell.row + 1}, column ${cell.column + 1}`;
    if (cell.flagged) {
        return `${position}, flagged`;
    }
    if (!cell.revealed) {
        return `${position}, hidden. Press F to flag`;
    }
    if (cell.mine) {
        return `${position}, mine`;
    }
    if (cell.adjacentMines === 0) {
        return `${position}, empty`;
    }
    return (
        `${position}, ` +
        `${cell.adjacentMines} adjacent mines`
    );
}
