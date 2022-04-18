package pl.battleships.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class Board {
    private final String gameId;
    private final TwoDimensionalBoard board;
    private final List<Ship> ships;
    @Builder.Default
    private GameStatus status = GameStatus.NOT_STARTED;

    /**
     * Update board and game status. Based on ship condition.
     */
    public GameStatus getGameStatus() {
        if (ships.stream().allMatch(Ship::isDestroyed)) {
            status = GameStatus.OVER;
        }
        return status;
    }

    public TwoDimensionalBoard getUpdatedBoard() {
        updateBoard();
        return board;
    }

    private void updateBoard() {
        ships.forEach(ship -> ship.getLocation().forEach(position ->
                board.setOnBoard(position.getX(), position.getY(), Boolean.TRUE.equals(position.isHit()) ? Math.negateExact(ship.getType()) : ship.getType())
        ));
    }
}
