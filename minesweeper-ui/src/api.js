const API_URL = "http://localhost:8080/v1/api";

async function request(url, options = {}) {
    const response = await fetch(url, options);

    let body = null;
    try {
        body = await response.json();
    } catch {
        console.log(response);
    }

    if (!response.ok) {
        throw new Error(
            body?.message || "Something went wrong"
        );
    }
    return body;
}

export function createGame(rows, columns, mines) {
    return request(`${API_URL}/games`, {
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
}

export function revealCell(gameId, row, column) {
    return request(
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
}

export function toggleFlag(gameId, row, column) {
    return request(
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
}
