package pl.battleships.core


import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.ShotHandler
import pl.battleships.core.model.Position
import pl.battleships.core.model.ShotResult

class SimpleShotHandler implements ShotHandler {

    BattleshipGame opponent;

    @Override
    ShotResult shotToOpponent(String gameId, Position position) {
        return opponent.opponentShot(gameId,position)
    }

    def addOpponent(BattleshipGame opponent) {
        this.opponent = opponent;
    }
}
