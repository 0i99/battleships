package pl.battleships.javaspringship.service;

import pl.battleships.api.dto.GameDto;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.api.dto.ShotStatusDto;


import java.util.List;

public interface GameService {

    List<ShipDto> joinTheGame(GameDto game);

    List<ShipDto> findShips(String gameId, boolean destroyed);

    ShotStatusDto opponentShot(String gameId, PositionDto position);

    List<PositionDto> getAllShots(String id);
}
