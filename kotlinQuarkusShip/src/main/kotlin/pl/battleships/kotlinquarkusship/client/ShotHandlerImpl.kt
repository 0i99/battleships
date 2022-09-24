package pl.battleships.kotlinquarkusship.client

import io.quarkus.logging.Log
import org.eclipse.microprofile.rest.client.inject.RestClient
import pl.battleships.core.api.ShotHandler
import pl.battleships.core.model.Position
import pl.battleships.core.model.ShotResult
import pl.battleships.kotlinquarkusship.config.GameProperties
import pl.battleships.kotlinquarkusship.extension.toPositionDto
import pl.battleships.kotlinquarkusship.extension.toShotResult
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ShotHandlerImpl(
    private val gameProperties: GameProperties,
    @RestClient private val restClientGameApi: RestClientGameApi,
) : ShotHandler {

    override fun shotToOpponent(gameId: String, position: Position): ShotResult {
        Log.info("Sleeping ${gameProperties.sleep()}ms and making shot to ${gameProperties.opponentUrl()} with position $position")
        try {
            Thread.sleep(gameProperties.sleep())
        } catch (e: InterruptedException) {
            Log.error("Problem while sleeping", e)
        }
        return restClientGameApi.shot(gameId, position.toPositionDto()).toShotResult()
    }
}