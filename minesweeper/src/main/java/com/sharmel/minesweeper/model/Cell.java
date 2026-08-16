package com.sharmel.minesweeper.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Cell {

    @NonNull
    private final int row;
    @NonNull
    private final int column;
    private boolean mine;
    private boolean reveal;
    private boolean flag;
    private int adjacentMines;
}
