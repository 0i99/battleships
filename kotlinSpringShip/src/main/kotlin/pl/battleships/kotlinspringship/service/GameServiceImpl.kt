package pl.battleships.kotlinspringship.service

import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.HistoryProvider

class GameServiceImpl(
    val battleshipGame: BattleshipGame,
    val historyProvider: HistoryProvider,
) : GameService {
    override fun joinTheGame(game: GameDto) {
        battleshipGame.start(game.id,game.propertySize,game.firstShotIsYours)
    }

    override fun opponentShot(gameId: String, position: PositionDto): ShotStatusDto {
        TODO("Not yet implemented")
    }

    override fun getAllShots(id: String): List<PositionDto> {
        TODO("Not yet implemented")
    }

    override fun getGameStatus(gameId: String): GameStatusDto {
        TODO("Not yet implemented")
    }
}