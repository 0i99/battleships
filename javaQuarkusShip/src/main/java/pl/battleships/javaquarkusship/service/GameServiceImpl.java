package pl.battleships.javaquarkusship.service;

import pl.battleships.javaquarkusship.api.dto.GameDto;
import pl.battleships.javaquarkusship.api.dto.GameStatusDto;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.javaquarkusship.api.dto.ShotStatusDto;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.javaquarkusship.mapper.GameMapper;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameServiceImpl implements GameService {

    @Inject
    HistoryProvider historyProvider;

    @Inject
    BattleshipGame battleshipGame;

    @Inject
    GameMapper mapper;

    @Override
    public void joinTheGame(GameDto game) {
        battleshipGame.start(game.getId(), game.getSize(), game.getFirstShotIsYours());
    }

    @Override
    public ShotStatusDto opponentShot(String gameId, PositionDto position) {
        return mapper.map(
                battleshipGame.opponentShot(gameId, mapper.map(position))
        );
    }

    @Override
    public List<PositionDto> getAllShots(String id) {
        return historyProvider.getAllShots(id).stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public GameStatusDto getGameStatus(String gameId) {
        return mapper.map(battleshipGame.getGameStatus(gameId));
    }
}
