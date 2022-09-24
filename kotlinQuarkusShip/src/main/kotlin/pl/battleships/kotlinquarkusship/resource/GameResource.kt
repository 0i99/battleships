package pl.battleships.kotlinquarkusship.resource

import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import pl.battleships.api.GameApi
import pl.battleships.api.GameStatusApi
import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.exception.*
import pl.battleships.kotlinquarkusship.service.GameService
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class GameResource(
    private val gameService: GameService
) : GameApi, GameStatusApi {
    override fun getAllShots(gameId: String): List<PositionDto> {
        return gameService.getAllShots(gameId)
    }

    override fun joinGame(game: GameDto) {
        gameService.joinTheGame(game)
    }

    override fun shot(gameId: String, position: PositionDto): ShotStatusDto {
        return gameService.opponentShot(gameId, position)
    }

    @ServerExceptionMapper
    fun duplicatedGameException(x: DuplicatedGameException?): Response {
        return Response.status(Response.Status.CONFLICT).build()
    }

    @ServerExceptionMapper
    fun noGameFoundException(x: NoGameFoundException?): Response {
        return Response.status(Response.Status.NOT_FOUND).build()
    }

    @ServerExceptionMapper
    fun gameOver(x: GameOverException?): Response {
        return Response.status(Response.Status.GONE).build()
    }

    @ServerExceptionMapper
    fun invalidParams(x: InvalidParamException?): Response {
        return Response.status(422).build()
    }

    @ServerExceptionMapper
    fun invalidMoveException(x: InvalidMoveException?): Response {
        return Response.status(Response.Status.FORBIDDEN).build()
    }

    override fun getGameStatus(id: String): GameStatusDto {
        return gameService.getGameStatus(id)
    }
}