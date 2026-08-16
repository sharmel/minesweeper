package com.sharmel.minesweeper.service;

import com.sharmel.minesweeper.dto.CreateGameRequest;
import com.sharmel.minesweeper.dto.MoveRequest;
import com.sharmel.minesweeper.model.Game;
import com.sharmel.minesweeper.model.GameStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private final GameService service = new GameService();

    @Test
    void createsGameWithRequestedDimensions() {

        Game game = service.createGame(
                new CreateGameRequest(9, 9, 10)
        );

        assertEquals(9, game.getRows());
        assertEquals(9, game.getColumns());
        assertEquals(10, game.getMines());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    void createsRequestedNumberOfMines() {

        Game game = service.createGame(
                new CreateGameRequest(9, 9, 10)
        );

        long mines = 0;

        for (var row : game.getBoard()) {
            for (var cell : row) {
                if (cell.isMine()) {
                    mines++;
                }
            }
        }

        assertEquals(10, mines);
    }

    @Test
    void rejectsTooManyMines() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createGame(
                        new CreateGameRequest(5, 5, 25)
                )
        );
    }

    @Test
    void rejectsMoveOutsideBoard() {

        Game game = service.createGame(
                new CreateGameRequest(5, 5, 5)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.reveal(
                        game.getId(),
                        new MoveRequest(10, 10)
                )
        );
    }

    @Test
    void canFlagUnrevealedCell() {

        Game game = service.createGame(
                new CreateGameRequest(5, 5, 5)
        );

        service.toggleFlag(
                game.getId(),
                new MoveRequest(0, 0)
        );

        assertTrue(
                game.getBoard()[0][0].isFlag()
        );
    }

    /*
     * Candidate tasks:
     *
     * - reveal a safe cell
     * - reveal a mine
     * - flood fill zero cells
     * - detect a win
     * - prevent moves after WIN
     * - prevent moves after LOSS
     * - prevent revealing flagged cells
     */
}
