package pl.battleships.core.api;

import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;

import java.util.List;

public interface HistoryProvider {

    /**
     * Add game board to history
     *
     * @param gameId
     * @param board
     * @return
     */
    Long addGame(String gameId, Board board);

    /**
     * Add shot to history for specific game
     *
     * @param gameId
     * @param position
     * @return
     */
    Long opponentShotForGame(String gameId, Position position);

    /**
     * Get all shots for gameId
     *
     * @param gameId
     * @return
     */
    List<Position> getAllShots(String gameId);
}
