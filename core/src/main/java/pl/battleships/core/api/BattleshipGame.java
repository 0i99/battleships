package pl.battleships.core.api;



import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;
import pl.battleships.core.model.ShotResult;

import java.util.List;

public interface BattleshipGame {

    Board joinTheGame(String gameId, int size);

    List<Ship> findShips(String gameId, boolean destroyed);

    ShotResult opponentShot(String gameId, Position position);
}
