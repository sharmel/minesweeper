package com.sharmel.minesweeper.dto;

import com.sharmel.minesweeper.model.Game;
import com.sharmel.minesweeper.model.GameStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public record GameResponse(
        UUID id,
        int rows,
        int columns,
        int mines,
        GameStatus status,
        List<List<CellResponse>> board
) {

    public static GameResponse from (Game game) {
        List<List<CellResponse>> board = IntStream.range(0,game.getRows())
                .mapToObj(row ->
                        IntStream.range(0, game.getColumns())
                                        .mapToObj(column ->
                        CellResponse.from(game.getBoard()[row][column]))
                                .toList()
                ).toList();

        return new GameResponse(
                game.getId(),
                game.getRows(),
                game.getColumns(),
                game.getMines(),
                game.getStatus(),
                board
        );
    }
}
