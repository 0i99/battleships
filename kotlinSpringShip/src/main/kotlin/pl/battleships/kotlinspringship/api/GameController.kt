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
import pl.battleships.core.exception.*
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
    fun gameOverExceptionHandler(): ResponseEntity<Void?>? {
        return ResponseEntity.status(HttpStatus.GONE).build()
    }

    @ExceptionHandler(NoGameFoundException::class)
    fun noGameFoundExceptionHandler(): ResponseEntity<Void?>? {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @ExceptionHandler(DuplicatedGameException::class)
    fun duplicatedGameExceptionHandler(): ResponseEntity<Void?>? {
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }

    @ExceptionHandler(InvalidParamException::class)
    fun invalidParamsExceptionHandler(): ResponseEntity<Void?>? {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
    }

    @ExceptionHandler(InvalidMoveException::class)
    fun invalidMoveExceptionHandler(): ResponseEntity<Void?>? {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
}