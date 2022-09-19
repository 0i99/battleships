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
import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.exception.DuplicatedGameException
import pl.battleships.core.model.Position
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

}