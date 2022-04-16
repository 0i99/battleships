package pl.battleships.core.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Board {
    private final String gameId;
    private final TwoDimensionalBoard board;
    private final List<Ship> ships;
    private GameStatus status = GameStatus.NOT_STARTED;

    /**
     * Update board and game status. Based on ship condition.
     */
    public GameStatus getGameStatus() {
        if (ships.stream().allMatch(ship -> Boolean.TRUE.equals(ship.isDestroyed()))) {
            status = GameStatus.OVER;
        }
        return status;
    }

    public void updateBoard(){
        ships.forEach(ship -> ship.getLocation().forEach(position ->
                board.setOnBoard(position.getX(), position.getY(), Boolean.TRUE.equals(position.isHit()) ? Math.negateExact(ship.getType()) : ship.getType())
        ));

    }

}
