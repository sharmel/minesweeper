package com.sharmel.minesweeper.service;

import com.sharmel.minesweeper.dto.CreateGameRequest;
import com.sharmel.minesweeper.dto.MoveRequest;
import com.sharmel.minesweeper.model.Cell;
import com.sharmel.minesweeper.model.Game;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public Game createGame(CreateGameRequest request) {

        if(request.mines() >= request.rows() * request.columns()) {
            throw new IllegalArgumentException(
                    "Number of lines must be less than board size"
            );
        }

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

        validateMove(game, moveRequest);

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

    public Game toggleFlag(UUID id, MoveRequest moveRequest) {
        Game game = games.get(id);

        validateMove(game, moveRequest);

        Cell cell = game.getBoard()[moveRequest.row()][moveRequest.column()];

        if(!cell.isReveal()) {
            cell.setFlag(!cell.isFlag());
        }

        return game;
    }

    private void validateMove(Game game, MoveRequest moveRequest) {

        if(moveRequest.row() >= game.getRows() ||
        moveRequest.column() >= game.getColumns()) {
            throw new IllegalArgumentException("Cell is outside the board");
        }
    }

    private boolean isInsideBoard(Game game, int row, int column) {
        return row >= 0 && row < game.getRows() && column >= 0 &&
                column < game.getColumns();
    }

    private void calculateAdjacentMines(Game game) {

        for(int row = 0; row < game.getRows(); row++){
            for(int column = 0; column < game.getColumns(); column++) {

                Cell cell = game.getBoard()[row][column];

                if(cell.isMine()){
                    continue;
                }
                int count = 0;

                for(int neighbourRow = row -1; neighbourRow <= row +1; neighbourRow++) {
                    for (int neighbourColumn = column-1; neighbourColumn <= neighbourColumn+1;
                    neighbourColumn++) {

                        if(!isInsideBoard(game,neighbourRow,neighbourColumn)) {
                            continue;
                        }

                        if(game.getBoard()[neighbourRow][neighbourColumn].isMine()){
                            count++;
                        }
                    }
                }
                cell.setAdjacentMines(count);
            }
        }
    }

    private void placeMines(Game game) {

        Random random = new Random();

        int placed = 0;
        while (placed < game.getMines()) {
            int row = random.nextInt(game.getRows());
            int column = random.nextInt(game.getColumns());
            Cell cell = game.getBoard()[row][column];

            if(!cell.isMine()) {
                cell.setMine(true);
                placed++;
            }
        }
    }


}
