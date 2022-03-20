package pl.battleships.javaspringship.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.battleships.api.model.Ship;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Builder
public class BoardReadyToPlay {
    private final String gameId;
    private final Board board;
    private final List<Ship> ships;

    public Board getBoard() {
        updateBoard();
        return board;
    }

    private void updateBoard() {
        ships.forEach(ship -> ship.getLocaction().forEach(position ->
                board.setOnBoard(position.getX(), position.getY(), Boolean.TRUE.equals(position.getHit()) ? Math.negateExact(ship.getType().getValue()) : ship.getType().getValue())
        ));
    }
}
