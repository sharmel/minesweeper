package com.sharmel.minesweeper.repository;

import com.sharmel.minesweeper.model.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveCompletedGame(Game game) {
        jdbcTemplate.update(
                """
                INSERT INTO completed_games
                    (id, rows, columns, mines, status)
                VALUES
                    (?, ?, ?, ?, ?)
                ON CONFLICT (id) DO UPDATE SET
                    status = EXCLUDED.status
                """,
                game.getId(),
                game.getRows(),
                game.getColumns(),
                game.getMines(),
                game.getStatus().name()
        );
    }
}
