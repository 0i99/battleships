package pl.battleships.javaspringship.service;

import pl.battleships.api.model.Game;
import pl.battleships.api.model.Position;
import pl.battleships.api.model.Ship;
import pl.battleships.api.model.ShotResult;
import pl.battleships.javaspringship.model.BoardReadyToPlay;

import java.util.List;

public interface GameService {

    BoardReadyToPlay joinTheGame(Game game);

    List<Ship> findShips(String gameId, boolean destroyed);

    ShotResult shot(String gameId, Position position);
}
