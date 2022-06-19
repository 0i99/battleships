package pl.battleships.core.api;

import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;

public interface ShotHandler {

    ShotResult shotToOpponent(String gameId, Position position);
}
