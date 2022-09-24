package pl.battleships.kotlinquarkusship.config

import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.BattleshipGameImpl
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.api.ShotHandler
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces

class GameConfig {

    @Produces
    @ApplicationScoped
    fun battleshipGame(historyProvider: HistoryProvider, shotHandler: ShotHandler): BattleshipGame {
        return BattleshipGameImpl(historyProvider, shotHandler)
    }
}