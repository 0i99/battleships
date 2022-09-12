package pl.battleships.kotlinspringship.service

import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto

interface GameService {

    fun joinTheGame(game: GameDto)

    fun opponentShot(gameId: String, position: PositionDto): ShotStatusDto

    fun getAllShots(id: String): List<PositionDto>

    fun getGameStatus(gameId: String): GameStatusDto
}