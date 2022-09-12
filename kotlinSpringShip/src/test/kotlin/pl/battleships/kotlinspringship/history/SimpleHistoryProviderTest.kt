package pl.battleships.kotlinspringship.history

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.model.Board
import pl.battleships.core.model.GameStatus
import pl.battleships.core.model.Position

class SimpleHistoryProviderTest {

    @Test
    fun `check simple history provider`() {
        val historyProvider: HistoryProvider = SimpleHistoryProvider()

        val game = historyProvider.addGame(
            "x",
            Board.builder().gameId("x").ships(listOf()).status(GameStatus.NOT_STARTED).build()
        )
        Assertions.assertEquals(0, game)

        val shots1 = historyProvider.getAllShots("x")
        Assertions.assertTrue(shots1.isEmpty())

        val shotId = historyProvider.opponentShotForGame("x", Position.builder().x(1).y(2).hit(false).build())
        Assertions.assertEquals(0, shotId)

        val shots2 = historyProvider.getAllShots("x")
        Assertions.assertEquals(1, shots2.size)
    }
}