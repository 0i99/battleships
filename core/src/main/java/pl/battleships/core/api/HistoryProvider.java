package pl.battleships.core.api;

import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;

import java.util.List;
import java.util.Optional;

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
     * add my shot to history
     * @param gameId
     * @param position
     * @return
     */
    Long shotForGame(String gameId, Position position);

    /**
     * Get all shots for gameId
     *
     * @param gameId
     * @return
     */
    List<Position> getAllShots(String gameId);
}
