package pl.battleships.javaspringship.service;

import pl.battleships.api.model.Position;
import pl.battleships.api.model.ShotResult;
import pl.battleships.javaspringship.model.BoardReadyToPlay;

import java.util.List;

public interface ShotService {

    /**
     * Someone shot to us for specific gameId
     *
     * @param board
     * @param position
     * @return
     */
    ShotResult shot(BoardReadyToPlay board, Position position);

    /**
     * Get all shot positions for specific game
     *
     * @param gameId
     * @return
     */
    List<Position> getAllShots(String gameId);
}
