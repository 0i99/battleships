package pl.battleships.kotlinspringship.service

import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.HistoryProvider
import pl.battleships.kotlinspringship.extension.toGameStatusDto
import pl.battleships.kotlinspringship.extension.toPosition
import pl.battleships.kotlinspringship.extension.toPositionDto
import pl.battleships.kotlinspringship.extension.toShotStatusDto

class GameServiceImpl(
    private val battleshipGame: BattleshipGame,
    private val historyProvider: HistoryProvider, // trailing comma <3
) : GameService {

    override fun joinTheGame(game: GameDto) {
        battleshipGame.start(game.id, game.propertySize, game.firstShotIsYours)
    }

    override fun opponentShot(gameId: String, position: PositionDto): ShotStatusDto {
        return battleshipGame.opponentShot(gameId, position.toPosition()).toShotStatusDto()
    }

    override fun getAllShots(id: String): List<PositionDto> {
        return historyProvider.getAllShots(id).map { it.toPositionDto() }
    }

    override fun getGameStatus(gameId: String): GameStatusDto {
        return battleshipGame.getGameStatus(gameId).toGameStatusDto()
    }
}