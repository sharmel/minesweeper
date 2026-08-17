package com.sharmel.minesweeper.service;

import com.sharmel.minesweeper.dto.CreateGameRequest;
import com.sharmel.minesweeper.dto.MoveRequest;
import com.sharmel.minesweeper.model.Cell;
import com.sharmel.minesweeper.model.Game;
import com.sharmel.minesweeper.model.GameStatus;
import com.sharmel.minesweeper.repository.GameRepository;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Map<UUID, Game> games = new ConcurrentHashMap<>();
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(CreateGameRequest request) {

        int boardSize = request.rows() * request.columns();
        if (request.mines() >= boardSize) {
            throw new IllegalArgumentException(
                    "Number of mines must be less than board size"
            );
        }

        // This condition is not needed as I have not null validation in CreateGame field
//        if(request.mines() < 1) {
//            throw new IllegalArgumentException("Number of mines must be at least 1");
//        }

        Game game = new Game(
                UUID.randomUUID(),
                request.rows(),
                request.columns(),
                request.mines()
        );

        placeMines(game);
        calculateAdjacentMines(game);

        games.put(game.getId(), game);

        return game;
    }

    public Game getGame(UUID id) {
        Game game = games.get(id);

        if (game == null) {
            throw new NoSuchElementException("Game not found");
        }

        return game;
    }

    public Game reveal(UUID id, MoveRequest moveRequest) {
        Game game = games.get(id);


        // Task 1: Prevent moves after the game has finished.
        validateGameInProgress(game);

        validateCordinate(game, moveRequest);
        // Task 2: Reject flagged cells.
        Cell cell = getCell(game, moveRequest);

        if (cell.isFlag()) {
            throw new IllegalStateException(
                    "Cannot reveal flagged cell"
            );
        }

        // Task 3: Reveal the selected cell.
        if (cell.isReveal()) {
            return game;
        }

        // Task 4: If the cell is a mine, lose the game.
        if (cell.isMine()) {
            lossGame(game);
            return game;
        }

        // Task 5: If the cell has zero neighbouring mines,
        //      recursively/flood-fill neighbouring cells.
        floodReveal(game, moveRequest.row(), moveRequest.column());

        // Task 6: Detect when the player has won.
        if (hasWon(game)) {
            winGame(game);
        }
        /*
         * LIVE CODING TASK
         *
         * Implement:
         *
         * 1. Prevent moves after the game has finished.
         * 2. Reject flagged cells.
         * 3. Reveal the selected cell.
         * 4. If the cell is a mine, lose the game.
         * 5. If the cell has zero neighbouring mines,
         *    recursively/flood-fill neighbouring cells.
         * 6. Detect when the player has won.
         */
        return game;
    }

    private void validateCordinate(Game game, MoveRequest moveRequest) {
        if (!isInsideBoard(game, moveRequest.row(), moveRequest.column())) {
            throw new IllegalArgumentException("Cell is outside the board");
        }
    }

    private void winGame(Game game) {
        game.setStatus(GameStatus.WON);
        revealMines(game);
        gameRepository.saveCompletedGame(game);
    }

    private void revealMines(Game game) {
        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0; column < game.getColumns(); column++) {
                Cell cell = game.getBoard()[row][column];

                if (cell.isReveal()) {
                    cell.setReveal(true);
                }
            }
        }
    }

    private boolean hasWon(Game game) {
        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0; column < game.getColumns(); column++) {
                Cell cell = game.getBoard()[row][column];

                if (!cell.isMine() && !cell.isReveal()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void floodReveal(Game game, @Min(0) int row, @Min(0) int column) {
        Queue queue = new ArrayDeque<>();

        Cell cell = game.getBoard()[row][column];
        queue.add(cell);
        while (!queue.isEmpty()) {
            Cell current = (Cell) queue.poll();
            if (current.isReveal() || current.isMine() || current.isFlag()) {
                continue;
            }
            current.setReveal(true);
            if (current.getAdjacentMines() > 0) {
                continue;
            }

            for (int cRow = current.getRow() -1; cRow <= current.getRow() +1; cRow++) {
                for (int cColumn = current.getColumn() -1;
                     cColumn < current.getColumn()+1; cColumn++) {

                    if(!isInsideBoard(game,cRow,cColumn)) {
                        continue;
                    }

                    Cell neighbour = game.getBoard()[cRow][cColumn];

                    if(!neighbour.isReveal() && !neighbour.isMine()
                    && !neighbour.isFlag()){
                        queue.add(current);
                    }
                }

            }
        }
    }

    private void lossGame(Game game) {
        game.setStatus(GameStatus.LOST);
        revealMines(game);
        gameRepository.saveCompletedGame(game);
    }

    private Cell  getCell(Game game, MoveRequest moveRequest) {
        return game.getBoard()[moveRequest.row()][moveRequest.column()];
    }

    private void validateGameInProgress(Game game) {
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Game has already finished"
            );
        }
    }

    public Game toggleFlag(UUID id, MoveRequest moveRequest) {
        Game game = games.get(id);
        validateCordinate(game, moveRequest);
        Cell cell = getCell(game, moveRequest);
        if (!cell.isReveal()) {
            cell.setFlag(!cell.isFlag());
        }
        return game;
    }

    private void validateMove(Game game, MoveRequest moveRequest) {

        if (moveRequest.row() >= game.getRows() ||
                moveRequest.column() >= game.getColumns()) {
            throw new IllegalArgumentException("Cell is outside the board");
        }
    }

    private boolean isInsideBoard(Game game, int row, int column) {
        return row >= 0 && row < game.getRows() && column >= 0 &&
                column < game.getColumns();
    }

    private void calculateAdjacentMines(Game game) {

        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0; column < game.getColumns(); column++) {

                Cell cell = game.getBoard()[row][column];
                if (cell.isMine()) {
                    continue;
                }
                int count = 0;

                for (int neighbourRow = row - 1; neighbourRow <= row + 1; neighbourRow++) {
                    for (int neighbourColumn = column - 1; neighbourColumn <= neighbourColumn + 1;
                         neighbourColumn++) {

                        if (!isInsideBoard(game, neighbourRow, neighbourColumn)) {
                            continue;
                        }

                        if (game.getBoard()[neighbourRow][neighbourColumn].isMine()) {
                            count++;
                        }
                    }
                }
                cell.setAdjacentMines(count);
            }
        }
    }

    private void placeMines(Game game) {
        List<Cell> cells = new ArrayList<>();
        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0; column < game.getColumns(); column++) {
                cells.add(game.getBoard()[row][column]);
            }
        }
        Collections.shuffle(cells);
        for (int i = 0; i < game.getMines(); i++) {
            cells.get(i).setMine(true);
        }
    }

    void calculateAdjacentMinesForTest(Game game) {
        calculateAdjacentMines(game);
    }

}
