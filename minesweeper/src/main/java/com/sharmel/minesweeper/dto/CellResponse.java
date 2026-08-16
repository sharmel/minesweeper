package com.sharmel.minesweeper.dto;

import com.sharmel.minesweeper.model.Cell;

public record CellResponse(
        int row,
        int column,
        boolean revealed,
        boolean flagged,
        boolean mine,
        int adjacentMines
) {

    static CellResponse from(Cell cell){
        return new CellResponse(
                cell.getRow(),
                cell.getColumn(),
                cell.isReveal(),
                cell.isFlag(),
                cell.isMine(),
                cell.getAdjacentMines());
    }
}
