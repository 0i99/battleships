package pl.battleships.kotlinquarkusship.service

import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.exception.DuplicatedGameException
import pl.battleships.core.model.*
import java.util.List
import javax.inject.Inject

@QuarkusTest
class GameServiceTest {

    @InjectMock
    lateinit var battleshipGame: BattleshipGame

    @InjectMock
    lateinit var historyProvider: HistoryProvider

    @Inject
    lateinit var gameService: GameServiceImpl

    @Test
    fun `check getting all shots`() {
        every { historyProvider.getAllShots(any()) } returns listOf(
            Position.builder().x(1).y(1).hit(false).build(),
        ) andThen listOf(
            Position.builder().x(1).y(1).hit(false).build(),
            Position.builder().x(2).y(2).hit(true).build()
        )

        val shots = gameService.getAllShots("x")
        assertEquals(1, shots.size)

        val shots2 = gameService.getAllShots("y")
        assertEquals(2, shots2.size)


        verify(exactly = 2) {
            historyProvider.getAllShots(any())
        }
    }

    @Test
    fun `check proper handling of duplicates`() {
        val slot = slot<String>()
        every { battleshipGame.start(capture(slot), any(), any()) } throws DuplicatedGameException()

        assertThrows<DuplicatedGameException> {
            gameService.joinTheGame(GameDto("x", 10, true))
        }

        assertEquals("x", slot.captured)
    }

    @Test
    fun `check positive scenario of joining to the game`() {
        every { battleshipGame.start(any(), any(), any()) } returns Board.builder().ships(
            List.of(
                Ship.builder().type(1).destroyed(true).location(
                    List.of(Position.builder().x(1).y(1).hit(true).build())
                ).build(),
                Ship.builder().type(2).destroyed(false).location(
                    List.of(Position.builder().x(4).y(4).build())
                ).build()
            )
        ).build()

        gameService.joinTheGame(GameDto("x", 10, true))

        verify {
            battleshipGame.start(any(), any(), any())
        }
    }

    @Test
    fun `check game status`() {
        every { battleshipGame.getGameStatus(any()) } returns GameStatus.RUNNING

        val gameStatus = gameService.getGameStatus("x")
        assertEquals(GameStatusDto.RUNNING, gameStatus)
    }

    @Test
    fun `check opponent shot`() {
        every { battleshipGame.opponentShot(any(), any()) } returns ShotResult.HIT

        val result = gameService.opponentShot("x", PositionDto(1, 2))
        assertEquals(ShotStatusDto.HIT, result)
    }
}