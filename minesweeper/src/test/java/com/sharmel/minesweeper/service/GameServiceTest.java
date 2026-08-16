package com.sharmel.minesweeper.service;

import com.sharmel.minesweeper.dto.CreateGameRequest;
import com.sharmel.minesweeper.dto.MoveRequest;
import com.sharmel.minesweeper.model.Game;
import com.sharmel.minesweeper.model.GameStatus;
import com.sharmel.minesweeper.repository.GameRepository;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private final GameRepository repository = new GameRepository( null){
        @Override
        public void saveCompletedGame(Game game){
            //
        }
    };
    private final GameService service = new GameService(repository);

    @Test
    void createsGameWithRequestedDimensions() {
        Game game = service.createGame(
                new CreateGameRequest(9, 9, 10)
        );

        assertEquals(9, game.getRows());
        assertEquals(9, game.getColumns());
        assertEquals(10, game.getMines());
        assertEquals(
                GameStatus.IN_PROGRESS,
                game.getStatus()
        );
    }

    @Test
    void createsExactlyRequestedNumberOfMines() {
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
    void rejectsGameWithZeroMines() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createGame(
                        new CreateGameRequest(5, 5, 0)
                )
        );
    }

    @Test
    void retrievesExistingGame() {
        Game created = service.createGame(
                new CreateGameRequest(5, 5, 5)
        );
        Game retrieved =
                service.getGame(created.getId());

        assertSame(created, retrieved);
    }

    @Test
    void throwsWhenGameDoesNotExist() {
        assertThrows(
                NoSuchElementException.class,
                () -> service.getGame(
                        java.util.UUID.randomUUID()
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
    void canFlagCell() {
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

    @Test
    void canUnflagCell() {
        Game game = service.createGame(
                new CreateGameRequest(5, 5, 5)
        );
        MoveRequest move =
                new MoveRequest(0, 0);

        service.toggleFlag(game.getId(), move);
        service.toggleFlag(game.getId(), move);

        assertFalse(
                game.getBoard()[0][0].isFlag()
        );
    }

    @Test
    void flaggedCellCannotBeRevealed() {
        Game game = service.createGame(
                new CreateGameRequest(5, 5, 1)
        );
        MoveRequest move =
                new MoveRequest(0, 0);
        game.getBoard()[0][0].setFlag(true);

        assertThrows(
                IllegalStateException.class,
                () -> service.reveal(
                        game.getId(),
                        move
                )
        );
    }

    @Test
    void revealedCellCanBeRevealedAgainWithoutChangingState() {
        Game game = service.createGame(
                new CreateGameRequest(5, 5, 1)
        );
        /*
         * Find a guaranteed safe cell.
         */
        var safeCell = findSafeCell(game);

        MoveRequest move = new MoveRequest(
                safeCell.row(),
                safeCell.column()
        );
        service.reveal(game.getId(), move);

        assertDoesNotThrow(
                () -> service.reveal(
                        game.getId(),
                        move
                )
        );
    }

    private TestCell findSafeCell(Game game) {
        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0;
                 column < game.getColumns();
                 column++) {

                if (!game.getBoard()[row][column].isMine()) {
                    return new TestCell(row, column);
                }
            }
        }
        throw new IllegalStateException(
                "No safe cell found"
        );
    }

    @Test
    void calculatesAdjacentMinesForCentreCell() {
        Game game = new Game(
                UUID.randomUUID(),
                3,
                3,
                1
        );
        game.getBoard()[0][1].setMine(true);
        service.calculateAdjacentMinesForTest(game);

        assertEquals(
                1,
                game.getBoard()[1][1].getAdjacentMines()
        );
    }


    private record TestCell(int row, int column) {
        //
    }
}
