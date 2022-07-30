package pl.battleships.javaquarkusship.service;

import pl.battleships.javaquarkusship.api.dto.GameDto;
import pl.battleships.javaquarkusship.api.dto.GameStatusDto;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.javaquarkusship.api.dto.ShotStatusDto;

import java.util.List;

public interface GameService {

    void joinTheGame(GameDto game);

    ShotStatusDto opponentShot(String gameId, PositionDto position);

    List<PositionDto> getAllShots(String id);

    GameStatusDto getGameStatus(String gameId);
}
