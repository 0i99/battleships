package pl.battleships.core.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.battleships.core.exception.*;
import pl.battleships.core.model.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@Slf4j
class BattleshipGameTest {

    @Mock
    HistoryProvider historyProvider;

    @Mock
    ShotHandler shotHandler;

    @InjectMocks
    BattleshipGameImpl player1Game;

    @DisplayName("check positive scenario of joining to the game")
    @Test
    void checkJoinTheGame() {
        Mockito.when(historyProvider.addGame(Mockito.any(), Mockito.any())).thenReturn(0L);
        Board board = player1Game.start("qwerty", 10, false);
        Assertions.assertNotNull(board);
        Assertions.assertNotNull(board.getValue());
        Assertions.assertEquals(10, board.getValue().getSize());
        Assertions.assertEquals("qwerty", board.getGameId());
        Assertions.assertEquals(6, board.getShips().size());
    }

    @DisplayName("check proper handling of duplicates")
    @Test
    void checkJoinForDuplicates() {
        player1Game.start("x", 10, false);
        Assertions.assertThrows(DuplicatedGameException.class, () -> player1Game.start("x", 10, false));
    }

    @DisplayName("check proper handling of invalid game size")
    @Test
    void checkBoardSize() {
        Assertions.assertThrows(InvalidParamException.class, () -> player1Game.start("x", 0, false));
    }

    @DisplayName("positive scenario for shot")
    @Test
    void checkShotPositive() {
        Board board = player1Game.start("x", 10, false);
        Optional<Ship> ship = board.getShips().stream().filter(x -> x.getType() == 2).findFirst();
        ShotResult shotResult = player1Game.opponentShot("x", ship.get().getLocation().get(0));
        Assertions.assertEquals(ShotResult.HIT, shotResult);
    }

    @DisplayName("check shot for game that not exists")
    @Test
    void checkShotNoGame() {
        Assertions.assertThrows(NoGameFoundException.class, () -> player1Game.opponentShot("x", Position.builder().x(0).y(0).build()));
    }

    @DisplayName("check shot for game that is already over")
    @Test
    void checkGameOver() {
        Board board = player1Game.start("x", 10, false);
        board.setStatus(GameStatus.OVER);
        Assertions.assertThrows(GameOverException.class, () -> player1Game.opponentShot("x", Position.builder().x(0).y(0).build()));
    }

    @DisplayName("check opponent invalid move")
    @Test
    void checkInvalidMove() {
        //after start
        Board board = player1Game.start("y", 10, true);
        Assertions.assertThrows(InvalidMoveException.class, () -> player1Game.opponentShot("y", Position.builder().x(0).y(0).build()));

        //after hit shot
        board = player1Game.start("z", 10, false);
        List<Position> positions = board.getShips().stream().flatMap(ship -> ship.getLocation().stream()).collect(Collectors.toList());
        ShotResult shotResult = player1Game.opponentShot("z", positions.get(0));
        Assertions.assertNotEquals(ShotResult.MISSED, shotResult);
        shotResult = player1Game.opponentShot("z", positions.get(1));
        Assertions.assertNotEquals(ShotResult.MISSED, shotResult);
    }

    @DisplayName("check shooting to opponent")
    @Test
    void checkShot() throws InterruptedException {
        Mockito.when(shotHandler.shotToOpponent(Mockito.any(), Mockito.any())).thenThrow(new InvalidMoveException());
        player1Game.start("w", 10, true);
        TimeUnit.MILLISECONDS.sleep(1000);
        Mockito.verify(shotHandler, Mockito.times(1)).shotToOpponent(Mockito.any(), Mockito.any());
        Assertions.assertFalse(player1Game.isMyMove("w"));

    }

    @DisplayName("check shooting to opponent, lucky scenario")
    @Test
    void checkShotHit() throws InterruptedException {
        Mockito.when(shotHandler.shotToOpponent(Mockito.any(), Mockito.any())).thenReturn(ShotResult.HIT);
        player1Game.start("q", 10, true);
        TimeUnit.MILLISECONDS.sleep(1000);
        Assertions.assertTrue(player1Game.isMyMove("q"));
        Mockito.verify(shotHandler, Mockito.atLeastOnce()).shotToOpponent(Mockito.any(), Mockito.any());

        Mockito.reset(shotHandler);
        Mockito.when(shotHandler.shotToOpponent(Mockito.any(), Mockito.any())).thenReturn(ShotResult.MISSED);
        TimeUnit.MILLISECONDS.sleep(1000);
        Assertions.assertFalse(player1Game.isMyMove("q"));
    }

    @DisplayName("check game status")
    @Test
    void checkGameStatus() {
        var gameId = "qwerty";
        Assertions.assertThrows(NoGameFoundException.class, () -> player1Game.getGameStatus(gameId));

        Board board = player1Game.start(gameId, 10, false);
        Assertions.assertNotNull(board);
        Assertions.assertEquals(GameStatus.RUNNING, player1Game.getGameStatus(gameId));

        //destoy all ships hack
        board.getShips().forEach(p -> p.setDestroyed(true));
        Assertions.assertEquals(GameStatus.OVER, player1Game.getGameStatus(gameId));
    }

}
