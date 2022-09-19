package pl.battleships.kotlinquarkusship.service

import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.HistoryProvider
import pl.battleships.kotlinquarkusship.extension.toGameStatusDto
import pl.battleships.kotlinquarkusship.extension.toPosition
import pl.battleships.kotlinquarkusship.extension.toPositionDto
import pl.battleships.kotlinquarkusship.extension.toShotStatusDto
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
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