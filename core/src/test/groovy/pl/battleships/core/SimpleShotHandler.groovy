package pl.battleships.core

import pl.battleships.core.api.ShotHandler
import pl.battleships.core.model.Position
import pl.battleships.core.model.ShotResult

class SimpleShotHandler implements ShotHandler{

    @Override
    ShotResult shotToOpponent(String gameId, Position position) {
        return ShotResult.MISSED;
    }
}
