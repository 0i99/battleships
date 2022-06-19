package pl.battleships.javaspringship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.battleships.api.dto.GameDto;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.core.model.Board;
import pl.battleships.javaspringship.mapper.GameModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final BattleshipGame battleshipGame;
    private final GameModelMapper mapper;
    private final HistoryProvider historyProvider;

    @Override
    public List<ShipDto> joinTheGame(GameDto game) {
        Board board = battleshipGame.start(game.getId(), game.getSize(),game.getFirstShotIsYours());
        return board.getShips().stream().map(mapper::convert).collect(Collectors.toList());
    }

    @Override
    public List<ShipDto> findShips(String gameId, boolean destroyed) {
        return battleshipGame.findShips(gameId, destroyed).stream().map(mapper::convert).collect(Collectors.toList());
    }

    @Override
    public ShotStatusDto opponentShot(String gameId, PositionDto position) {
        return mapper.convert(
                battleshipGame.opponentShot(gameId, mapper.convert(position))
        );
    }

    @Override
    public List<PositionDto> getAllShots(String id) {
        return historyProvider.getAllShots(id).stream().map(mapper::convert).collect(Collectors.toList());
    }

}
