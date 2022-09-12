package pl.battleships.kotlinspringship.client

import mu.KotlinLogging
import org.springframework.web.client.RestTemplate
import pl.battleships.core.api.ShotHandler
import pl.battleships.core.model.Position
import pl.battleships.core.model.ShotResult
import pl.battleships.kotlinspringship.config.GameProperties
import java.util.Map

class SimpleShotHandler (
    private val restTemplate : RestTemplate,
    private val gameProperties: GameProperties
) : ShotHandler {
    private val log = KotlinLogging.logger {}

    override fun shotToOpponent(gameId: String, position: Position): ShotResult {
        log.info { "Sleeping ${gameProperties.sleep}ms and making shot to ${gameProperties.opponentUrl} with position $position" }
        try {
            Thread.sleep(gameProperties.sleep)
        } catch (e: InterruptedException) {
            log.error("Problem while sleeping", e)
        }
        return restTemplate
            .postForEntity(gameProperties.opponentUrl + "/game/{id}/shot", position, ShotResult::class.java, Map.of("id", gameId))
            .body!!
    }
}