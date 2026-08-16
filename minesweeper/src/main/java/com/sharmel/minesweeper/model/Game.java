package com.sharmel.minesweeper.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Game {

    @NonNull
    private final UUID id;
    @NonNull
    private final int rows;
    @NonNull
    private final int columns;
    @NonNull
    private final int mines;

    private final Cell[][] board;
    private GameStatus status;

    public Game(UUID id, int rows, int columns, int mines) {
        this.id = id;
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;

        this.status = GameStatus.IN_PROGRESS;
        this.board = new Cell[rows][columns];

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                board[row][column] = new Cell(row,column);
            }
        }
    }
}
