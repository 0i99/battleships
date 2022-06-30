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

import java.util.concurrent.TimeUnit

class GameRunnerTest extends Specification {

    def "run game for 2 local players"() {
        def player1ShotHandler = new SimpleShotHandler()
        def player2ShotHandler = new SimpleShotHandler()
        def player1 = new BattleshipGameImpl(new OneGameHistoryProvider(), player1ShotHandler)
        def player2 = new BattleshipGameImpl(new OneGameHistoryProvider(), player2ShotHandler)
        player1ShotHandler.addOpponent(player2)
        player2ShotHandler.addOpponent(player1)

        def gameId = UUID.randomUUID().toString()
        when:
        def player1Board = player1.start(gameId, 10, true)
        def player2Board = player2.start(gameId, 10, true)

        then:
        player1Board != null
        player1Board.gameStatus == GameStatus.RUNNING
        player2Board != null
        player2Board.gameStatus == GameStatus.RUNNING

        when: "get some time players to play"
        TimeUnit.SECONDS.sleep(10)

        then: "got shots"
        true
//        GameStatus gameStatus = GameStatus.RUNNING;
//        while (gameStatus != GameStatus.OVER){
//            TimeUnit.SECONDS.sleep(1)
//        }
    }

    @Unroll
    def "checking shot to position #x,#y with expected result #expected"(int x, int y, ShotResult expected) {
        setup: "prepared game board"
        def historyProvider = Mockito.mock(HistoryProvider.class)
        def shotHandler = Mockito.mock(ShotHandler.class)
        def game = new BattleshipGameImpl(historyProvider, shotHandler)
        def board = game.start("x", 10, false)
        board.ships.clear()
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
