package com.sharmel.minesweeper.controller;

import com.sharmel.minesweeper.dto.CreateGameRequest;
import com.sharmel.minesweeper.dto.GameResponse;
import com.sharmel.minesweeper.dto.MoveRequest;
import com.sharmel.minesweeper.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/")
@CrossOrigin
public class GameController {

    private  final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("games")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@Valid @RequestBody CreateGameRequest createGameRequest){

        return GameResponse.from(gameService.createGame(createGameRequest));
    }

    @GetMapping("games/{id}")
    public GameResponse getGame(@PathVariable UUID id){
        return GameResponse.from(gameService.getGame(id));
    }

    @PostMapping("games/{id}/moves")
    public GameResponse reveal(@PathVariable UUID id,
                               @Valid @RequestBody MoveRequest moveRequest){

        return GameResponse.from(gameService.reveal(id,moveRequest));

    }

    @PostMapping("games/{id}/flags")
    public GameResponse flag(@PathVariable UUID id,
                             @Valid @RequestBody MoveRequest moveRequest) {
        return GameResponse.from(gameService.toggleFlag(id, moveRequest));
    }

}
