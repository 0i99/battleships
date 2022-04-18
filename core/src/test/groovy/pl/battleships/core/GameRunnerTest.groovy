package pl.battleships.core

import org.mockito.Mockito
import pl.battleships.core.api.BattleshipGameImpl
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.api.ShotHandler
import pl.battleships.core.model.GameStatus
import pl.battleships.core.model.Position
import pl.battleships.core.model.Ship
import pl.battleships.core.model.ShotResult
import spock.lang.Specification
import spock.lang.Unroll

class GameRunnerTest extends Specification {

    def "run game"() {
        def player1 = new BattleshipGameImpl(new OneGameHistoryProvider(), new SimpleShotHandler())
        def player2 = new BattleshipGameImpl(new OneGameHistoryProvider(), new SimpleShotHandler())
        def gameId = UUID.randomUUID().toString()
        when:
        def player1Board = player1.start(gameId, 10, true)
        def player2Board = player2.start(gameId, 10, false)

        then:
        player1Board != null
        player1Board.gameStatus == GameStatus.RUNNING
        player2Board != null
        player2Board.gameStatus == GameStatus.RUNNING

//        when:
//        player1.opponentShot(gameId,Position.builder().x(0).y(0).build())
    }

    @Unroll
    def "checking shot to position #x,#y with expected result #expected"(int x, int y, ShotResult expected) {
        setup: "prepared game board"
        def historyProvider = Mockito.mock(HistoryProvider.class)
        def shotHandler = Mockito.mock(ShotHandler.class)
        def game = new BattleshipGameImpl(historyProvider, shotHandler)
        def board = game.start("x", 10, false)
        board.ships.addAll([
                Ship.builder().type(2).location([Position.builder().x(1).y(1).build(), Position.builder().x(1).y(2).build()]).build(),
                Ship.builder().type(1).location([Position.builder().x(8).y(6).build()]).build()
        ])

        when: "shot to position (x,y)"
        def result = game.opponentShot("x", Position.builder().x(x).y(y).build())

        then: "result should be"
        expected == result

        where:
        x | y | expected
        0 | 0 | ShotResult.MISSED
        1 | 1 | ShotResult.HIT
        8 | 6 | ShotResult.DESTROYED
    }

}
