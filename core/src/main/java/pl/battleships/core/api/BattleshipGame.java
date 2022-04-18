package pl.battleships.core.api;


import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;
import pl.battleships.core.model.ShotResult;

import java.util.List;

public interface BattleshipGame {

    /**
     * Create board and join to the game
     *
     * @param gameId
     * @param size
     * @param firstShotIsYours
     * @return
     */
    Board start(String gameId, int size, boolean firstShotIsYours);

    /**
     * Got opponent shot
     *
     * @param gameId
     * @param position
     * @return
     */
    ShotResult opponentShot(String gameId, Position position);

    List<Ship> findShips(String gameId, boolean destroyed);

    boolean isMyMove(String gameId);
}
