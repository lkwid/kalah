package com.backbase.kalah.controller;

import com.backbase.kalah.model.game.Game;
import com.backbase.kalah.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Map;

@RestController
@RequestMapping("/games")
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__( @Autowired ))
public class GameController {

    private GameService service;

    @Operation(summary = "Creates a new game with standard rules")
    @ApiResponse(responseCode = "201", description = "A new game has been created")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Map<String, String>> createNewGame() {
        Game game = service.createNewGame();

        // TODO: 01/02/2021 change String.format to automatically determine host and port
        Map<String, String> body = Map.of(
                "id", game.getId().toString(),
                "uri", String.format("http://<host>:<port>/games/%d", game.getId()));

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @Operation(summary = "Starting next turn in given game")
    @ApiResponse(responseCode = "200", description = "A move has been made")
    @PutMapping(value = "/{gameId}/pits/{pitId}", produces = "application/json")
    public ResponseEntity<Map<String, String>> makeMove(
            @Parameter(description = "Id of a game to be player") @PathVariable() Long gameId,
            @Parameter(description = "Id of a chosen pit") @PathVariable() Integer pitId) {
        Game game = service.makeMove(gameId, pitId);

        // TODO: 01/02/2021 change String.format to automatically determine host and port
        Map<String, String> body = Map.of(
                "id", game.getId().toString(),
                "uri", String.format("http://<host>:<port>/games/%d", game.getId()),
                "status", game.getScore().toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
