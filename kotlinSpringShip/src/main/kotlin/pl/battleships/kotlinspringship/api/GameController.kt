package pl.battleships.kotlinspringship.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import pl.battleships.api.GameApi
import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.exception.GameOverException
import pl.battleships.kotlinspringship.service.GameService

@RestController
@CrossOrigin
class GameController(private val gameService: GameService) : GameApi {
    override fun getAllShots(id: String): ResponseEntity<List<PositionDto>> {
        return ResponseEntity.ok(gameService.getAllShots(id))
    }

    override fun getGameStatus(id: String): ResponseEntity<GameStatusDto> {
        return ResponseEntity.ok(gameService.getGameStatus(id))
    }

    override fun joinGame(gameDto: GameDto): ResponseEntity<Unit> {
        gameService.joinTheGame(gameDto)
        return ResponseEntity.ok().build()
    }

    override fun shot(id: String, positionDto: PositionDto): ResponseEntity<ShotStatusDto> {
        return ResponseEntity.ok(gameService.opponentShot(id, positionDto))
    }

    @ExceptionHandler(GameOverException::class)
    protected fun gameOver(): ResponseEntity<Void?>? {
        return ResponseEntity.status(HttpStatus.GONE).build()
    }

}