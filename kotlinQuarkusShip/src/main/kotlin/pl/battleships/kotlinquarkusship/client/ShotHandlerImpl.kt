package pl.battleships.kotlinquarkusship.client

import pl.battleships.core.api.ShotHandler
import pl.battleships.core.model.Position
import pl.battleships.core.model.ShotResult
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ShotHandlerImpl : ShotHandler {

    override fun shotToOpponent(gameId: String, position: Position): ShotResult {
        TODO("Not yet implemented")
    }
}