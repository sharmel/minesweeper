CREATE TABLE IF NOT EXISTS games (
    id UUID PRIMARY KEY,
    rows INTEGER NOT NULL,
    columns INTEGER NOT NULL,
    mines INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_games_status
    ON games(status);

CREATE INDEX IF NOT EXISTS idx_games_created_at
    ON games(created_at);

CREATE TABLE IF NOT EXISTS completed_games (
    id UUID PRIMARY KEY,
    rows INTEGER NOT NULL,
    columns INTEGER NOT NULL,
    mines INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_completed_games_completed_at
    ON completed_games(completed_at);

CREATE INDEX IF NOT EXISTS idx_completed_games_status
    ON completed_games(status);

