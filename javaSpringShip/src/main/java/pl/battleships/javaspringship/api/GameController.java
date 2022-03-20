package pl.battleships.javaspringship.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.battleships.api.GameApi;
import pl.battleships.api.model.Game;
import pl.battleships.api.model.Position;
import pl.battleships.api.model.Ship;
import pl.battleships.api.model.ShotResult;
import pl.battleships.javaspringship.exception.DuplicatedGameException;
import pl.battleships.javaspringship.exception.InvalidParamException;
import pl.battleships.javaspringship.exception.NoGameFoundException;
import pl.battleships.javaspringship.service.GameService;
import pl.battleships.javaspringship.service.ShotService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GameController implements GameApi {

    protected final GameService gameService;
    protected final ShotService shotService;

    @Override
    public ResponseEntity<List<Ship>> find(String id, Boolean destroyed) {
        return ResponseEntity.ok(
                gameService.findShips(id, destroyed)
        );
    }

    @Override
    public ResponseEntity<List<Position>> getAllShots(String id) {
        return ResponseEntity.ok(
                shotService.getAllShots(id)
        );
    }

    @Override
    public ResponseEntity<List<Ship>> joinGame(Game game) {
        return ResponseEntity.ok(
                gameService.joinTheGame(game).getShips()
        );
    }

    @Override
    public ResponseEntity<ShotResult> shot(String id, Position position) {
        return ResponseEntity.ok(
                gameService.shot(id, position)
        );
    }

    @ExceptionHandler(DuplicatedGameException.class)
    protected ResponseEntity<Void> duplicatedGame() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(NoGameFoundException.class)
    protected ResponseEntity<Void> noGameFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(InvalidParamException.class)
    protected ResponseEntity<Void> invalidParams() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

}
