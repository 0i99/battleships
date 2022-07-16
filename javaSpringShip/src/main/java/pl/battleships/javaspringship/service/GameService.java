package pl.battleships.javaspringship.service;

import pl.battleships.api.dto.GameDto;
import pl.battleships.api.dto.GameStatusDto;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShotStatusDto;

import java.util.List;

public interface GameService {

    void joinTheGame(GameDto game);

    ShotStatusDto opponentShot(String gameId, PositionDto position);

    List<PositionDto> getAllShots(String id);

    GameStatusDto getGameStatus(String gameId);
}
