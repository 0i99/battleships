package pl.battleships.core

import org.mockito.Mockito
import pl.battleships.core.api.BattleshipGameImpl
import pl.battleships.core.api.Dockyard
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.model.Board
import pl.battleships.core.model.GameStatus
import pl.battleships.core.model.Position
import pl.battleships.core.model.Ship
import pl.battleships.core.model.ShotResult
import pl.battleships.core.model.TwoDimensionalBoard
import spock.lang.Specification
import spock.lang.Unroll

class GameRunnerTest extends Specification {

    @Unroll
    def "checking shot to position #x,#y with expected result #expected"(int x, int y, ShotResult expected) {
        setup: "prepared game board"
        def historyProvider = Mockito.mock(HistoryProvider.class)
        def game = new BattleshipGameImpl(historyProvider)
        def board = prepareBoard()
        Mockito.when(historyProvider.findGame("x")).thenReturn(Optional.of(board))

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

    Board prepareBoard() {
        Board.builder()
                .gameId("x")
                .ships([
                        Dockyard.INSTANCE.build(Ship.Type.Carrier, [
                                Position.builder().x(1).y(1).hit(false).build(),
                                Position.builder().x(1).y(2).hit(false).build(),
                                Position.builder().x(1).y(3).hit(false).build(),
                                Position.builder().x(1).y(4).hit(false).build(),
                                Position.builder().x(2).y(4).hit(false).build()
                        ]),
                        Dockyard.INSTANCE.build(Ship.Type.Destroyer, List.of(Position.builder().x(8).y(6).hit(false).build()))
                ])
                .board(new TwoDimensionalBoard(10))
                .status(GameStatus.RUNNING)
                .build()
    }

}
