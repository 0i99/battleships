package pl.battleships.kotlinspringship.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.exception.DuplicatedGameException
import pl.battleships.core.model.*
import java.util.List
import kotlin.system.measureTimeMillis


@ExtendWith(MockitoExtension::class)
class GameServiceTest {

    @Mock
    lateinit var battleshipGame: BattleshipGame

    @Mock
    lateinit var historyProvider: HistoryProvider

    @InjectMocks
    lateinit var gameServiceImpl: GameServiceImpl

    @Test
    fun `check getting all shots`() {
        whenever(historyProvider.getAllShots(any())).thenReturn(
            listOf(
                Position.builder().x(1).y(1).hit(true).build(),
                Position.builder().x(1).y(2).hit(false).build(),
                Position.builder().x(1).y(3).hit(false).build()
            )
        )
        val time = measureTimeMillis {
            val allShots = gameServiceImpl.getAllShots("x")
            Assertions.assertEquals(3, allShots.size)
            Assertions.assertEquals(1, allShots.filter { it -> it.hit == true }.size)
        }
        println("time $time ms")
    }

    @Test
    fun `check positive scenario of joining to the game`() {
        whenever(battleshipGame.start(any(), any(), any())).thenReturn(
            Board.builder().ships(
                List.of(
                    Ship.builder().type(1).destroyed(true).location(
                        List.of(Position.builder().x(1).y(1).hit(true).build())
                    ).build(),
                    Ship.builder().type(2).destroyed(false).location(
                        List.of(Position.builder().x(4).y(4).build())
                    ).build()
                )
            ).build()
        )

        gameServiceImpl.joinTheGame(GameDto("x", 10, true))
    }

    @Test
    fun `check proper duplicates handling`() {
        val gameIdCaptor = argumentCaptor<String>()
        whenever(battleshipGame.start(gameIdCaptor.capture(), any(), any())).thenThrow(DuplicatedGameException())

        Assertions.assertThrows(DuplicatedGameException::class.java) {
            gameServiceImpl.joinTheGame(GameDto("x", 10, true))
        }

        Assertions.assertEquals("x",gameIdCaptor.lastValue)
    }

    @Test
    fun `check game status`(){
        whenever(battleshipGame.getGameStatus(any())).thenReturn(GameStatus.RUNNING)

        val gameStatus = gameServiceImpl.getGameStatus("x")
        Assertions.assertEquals(GameStatusDto.RUNNING,gameStatus)
    }

    @Test
    fun `check opponent shot`(){
        whenever(battleshipGame.opponentShot(any(), any())).thenReturn(ShotResult.HIT)

        val result = gameServiceImpl.opponentShot("x", PositionDto(1, 2))
        Assertions.assertEquals(ShotStatusDto.HIT,result)
    }
}