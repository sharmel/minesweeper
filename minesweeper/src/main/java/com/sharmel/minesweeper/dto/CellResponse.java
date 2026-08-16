package com.sharmel.minesweeper.dto;

import com.sharmel.minesweeper.model.Cell;
import com.sharmel.minesweeper.model.GameStatus;

public record CellResponse(
        int row,
        int column,
        boolean revealed,
        boolean flagged,
        boolean mine,
        int adjacentMines
) {

    static CellResponse from(Cell cell){

        boolean exposedMine = cell.isReveal() && cell.isMine();
        return new CellResponse(
                cell.getRow(),
                cell.getColumn(),
                cell.isReveal(),
                cell.isFlag(),
                exposedMine,
                cell.isReveal() ? cell.getAdjacentMines() : 0);
    }
}
