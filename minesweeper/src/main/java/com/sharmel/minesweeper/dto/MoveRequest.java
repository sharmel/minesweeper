package com.sharmel.minesweeper.dto;

import jakarta.validation.constraints.Min;

public record MoveRequest(
        @Min(0)
        int row,
        @Min(0)
        int column
) {
}
