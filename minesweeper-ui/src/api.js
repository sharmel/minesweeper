const API_URL = "http://localhost:8080/api";

export async function createGame(rows, columns, mines) {
    const response = await fetch(`${API_URL}/games`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            rows,
            columns,
            mines
        })
    });

    if (!response.ok) {
        throw new Error("Unable to create game");
    }

    return response.json();
}

export async function revealCell(gameId, row, column) {
    const response = await fetch(
        `${API_URL}/games/${gameId}/moves`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                row,
                column
            })
        }
    );

    if (!response.ok) {
        throw new Error("Unable to reveal cell");
    }

    return response.json();
}

export async function toggleFlag(gameId, row, column) {
    const response = await fetch(
        `${API_URL}/games/${gameId}/flags`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                row,
                column
            })
        }
    );

    if (!response.ok) {
        throw new Error("Unable to flag cell");
    }

    return response.json();
}
