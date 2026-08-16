package com.sharmel.minesweeper.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CreateGameRequest(
        @Min(5)
        @Max(30)
        int rows,

        @Min(5)
        @Max(30)
        int columns,

        @Min(1)
        int mines
) {
}
