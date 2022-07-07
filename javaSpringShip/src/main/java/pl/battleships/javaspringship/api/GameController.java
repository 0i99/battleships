package pl.battleships.javaspringship.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.battleships.api.GameApi;
import pl.battleships.api.dto.GameDto;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.exception.*;
import pl.battleships.javaspringship.service.GameService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GameController implements GameApi {

    protected final GameService gameService;

    @Override
    public ResponseEntity<List<ShipDto>> find(String id, Boolean destroyed) {
        return ResponseEntity.ok(gameService.findShips(id, destroyed));
    }

    @Override
    public ResponseEntity<List<PositionDto>> getAllShots(String id) {
        return ResponseEntity.ok(gameService.getAllShots(id));
    }

    @Override
    public ResponseEntity<List<ShipDto>> joinGame(GameDto game) {
        return ResponseEntity.ok(gameService.joinTheGame(game));
    }

    @Override
    public ResponseEntity<ShotStatusDto> shot(String id, PositionDto position) {
        return ResponseEntity.ok(gameService.opponentShot(id, position));
    }

    @ExceptionHandler(DuplicatedGameException.class)
    protected ResponseEntity<Void> duplicatedGame() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(NoGameFoundException.class)
    protected ResponseEntity<Void> noGameFound() {
        log.error("Game not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(GameOverException.class)
    protected ResponseEntity<Void> gameOver() {
        log.error("Game over");
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @ExceptionHandler(InvalidParamException.class)
    protected ResponseEntity<Void> invalidParams() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @ExceptionHandler(InvalidMoveException.class)
    protected ResponseEntity<Void> invalidMoveException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
