package pl.battleships.core.api;


import pl.battleships.core.model.*;

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

    boolean isMyMove(String gameId);

    /**
     * Get game status
     * @param gameId
     * @return
     */
    GameStatus getGameStatus(String gameId);
}
