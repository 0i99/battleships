package pl.battleships.javaspringship.history;


import pl.battleships.core.api.HistoryProvider;
import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;

import java.util.*;

public class SimpleHistoryProvider implements HistoryProvider {

    private final Map<String, List<Position>> shots = new HashMap<>();
    private Map<String, Board> boards = new HashMap<>();

    @Override
    public Optional<Board> findGame(String gameId) {
        return Optional.ofNullable(boards.get(gameId));
    }

    @Override
    public Board getGame(String gameId) {
        return boards.get(gameId);
    }

    @Override
    public Long addGame(String gameId, Board board) {
        boards.put(gameId, board);
        return 0L;
    }

    @Override
    public Long addShotForGame(String gameId, pl.battleships.core.model.Position position) {
        if (!shots.containsKey(gameId)) {
            shots.put(gameId, new ArrayList<>());
        }
        shots.get(gameId).add(position);
        return 0L;
    }

    @Override
    public List<pl.battleships.core.model.Position> getAllShots(String gameId) {
        return Optional.ofNullable(shots.get(gameId)).orElse(Collections.emptyList());
    }
}
