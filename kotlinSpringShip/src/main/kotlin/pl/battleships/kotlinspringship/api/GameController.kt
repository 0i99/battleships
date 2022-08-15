package pl.battleships.kotlinspringship.api

import org.springframework.http.ResponseEntity
import pl.battleships.api.GameApi
import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto

class GameController : GameApi {
    override fun getAllShots(id: String): ResponseEntity<List<PositionDto>> {
        return super.getAllShots(id)
    }

    override fun getGameStatus(id: String): ResponseEntity<GameStatusDto> {
        return super.getGameStatus(id)
    }

    override fun joinGame(gameDto: GameDto): ResponseEntity<Unit> {
        return super.joinGame(gameDto)
    }

    override fun shot(id: String, positionDto: PositionDto): ResponseEntity<ShotStatusDto> {
        return super.shot(id, positionDto)
    }
}