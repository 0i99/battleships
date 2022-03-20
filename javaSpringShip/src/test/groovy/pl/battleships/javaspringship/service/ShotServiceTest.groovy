package pl.battleships.javaspringship.service

import pl.battleships.api.model.Position
import pl.battleships.api.model.Ship
import pl.battleships.api.model.ShotResult
import pl.battleships.javaspringship.exception.InvalidParamException
import pl.battleships.javaspringship.model.Board
import pl.battleships.javaspringship.model.BoardReadyToPlay
import spock.lang.Specification
import spock.lang.Unroll

class ShotServiceTest extends Specification {

    @Unroll
    def "checking shot to position #x,#y with expected result #expected"(int x, int y, ShotResult expected) {
        setup: "prepared game board"
        def shotService = new ShotServiceImpl()
        def game = prepareBoard()

        when: "shot to position (x,y)"
        def result = shotService.shot(game, new Position().x(x).y(y))

        then: "result should be"
        expected == result

        where:
        x | y | expected
        0 | 0 | ShotResult.MISSED
        1 | 1 | ShotResult.HIT
        8 | 6 | ShotResult.DESTROYED
    }

    def "invalid shot"(){
        setup: "prepared game board"
        def shotService = new ShotServiceImpl()
        def game = prepareBoard()

        when: "shot to position out of board"
        def result = shotService.shot(game, new Position().x(game.getBoard().getSize()+50).y(game.getBoard().getSize()+50))

        then: "exception thrown"
        thrown InvalidParamException
    }

    @Unroll
    def "checking proper getting all shots"() {
        setup: "prepared game board"
        def shotService = new ShotServiceImpl()
        def game = prepareBoard()

        when: "shot to multiple positions and getting shots"
        shotService.shot(game, new Position().x(0).y(0))
        shotService.shot(game, new Position().x(4).y(1))
        shotService.shot(game, new Position().x(1).y(4))
        shotService.shot(game, new Position().x(3).y(6))
        shotService.shot(game, new Position().x(2).y(9))

        def shots = shotService.getAllShots(game.getGameId())

        then:
        !shots.isEmpty()
        5 == shots.size()
    }

    private BoardReadyToPlay prepareBoard() {
        BoardReadyToPlay.builder()
                .gameId("x")
                .ships(List.of(
                        Dockyard.INSTANCE.build(Ship.TypeEnum.Carrier, List.of(
                                new Position().x(1).y(1).hit(false),
                                new Position().x(1).y(2).hit(false),
                                new Position().x(1).y(3).hit(false),
                                new Position().x(1).y(4).hit(false),
                                new Position().x(2).y(4).hit(false)
                        )),
                        Dockyard.INSTANCE.build(Ship.TypeEnum.Destroyer, List.of(new Position().x(8).y(6).hit(false)))
                ))
                .board(new Board(10))
                .build()
    }

}
