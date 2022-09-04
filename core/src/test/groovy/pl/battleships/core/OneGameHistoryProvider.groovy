package pl.battleships.core

import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.model.Board
import pl.battleships.core.model.Position

class OneGameHistoryProvider implements HistoryProvider {

    Board board;
    Map<String, List<Position>> opponentShots = new HashMap<>();
    Map<String, List<Position>> shots = new HashMap<>();

    @Override
    Long addGame(String gameId, Board board) {
        this.board = board
        this.shots.put(gameId, new ArrayList<>())
        this.opponentShots.put(gameId, new ArrayList<>())
        return 0L
    }

    @Override
    Long opponentShotForGame(String gameId, Position position) {
        opponentShots.get(gameId).add(position)
        return 0L
    }

    @Override
    List<Position> getAllShots(String gameId) {
        return Optional.ofNullable(opponentShots.get(gameId)).orElse(Collections.emptyList())
    }
}
