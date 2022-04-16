package pl.battleships.core.api;

import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;

import java.util.List;
import java.util.Optional;

public interface HistoryProvider {

    /**
     * Find game board by gameId
     *
     * @param gameId
     * @return
     */
    Optional<Board> findGame(String gameId);

    /**
     * Get game board by gameId
     *
     * @param gameId
     * @return
     */
    Board getGame(String gameId);

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
    Long addShotForGame(String gameId, Position position);

    /**
     * Get all shots for gameId
     *
     * @param gameId
     * @return
     */
    List<Position> getAllShots(String gameId);
}
