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
import pl.battleships.core.exception.DuplicatedGameException;
import pl.battleships.core.exception.GameOverException;
import pl.battleships.core.exception.InvalidParamException;
import pl.battleships.core.exception.NoGameFoundException;
import pl.battleships.core.model.*;

import java.util.List;
import java.util.Optional;

import static pl.battleships.core.model.Ship.Type.Submarine;

@ExtendWith(MockitoExtension.class)
@Slf4j
class BattleshipGameTest {

    @Mock
    HistoryProvider historyProvider;

    @InjectMocks
    BattleshipGameImpl game;


    @DisplayName("check positive scenario of joining to the game")
    @Test
    void checkJoinTheGame() {
        Mockito.when(historyProvider.findGame(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(historyProvider.addGame(Mockito.any(), Mockito.any())).thenReturn(0L);

        Board board = game.joinTheGame("qwerty", 10);
        Assertions.assertNotNull(board);
        Assertions.assertNotNull(board.getBoard());
        Assertions.assertEquals(10, board.getBoard().getSize());
        Assertions.assertEquals("qwerty", board.getGameId());
        Assertions.assertEquals(6, board.getShips().size());
    }

    @DisplayName("check proper handling of duplicates")
    @Test
    void checkJoinForDuplicates() {
        Mockito.when(historyProvider.findGame(Mockito.any())).thenReturn(Optional.of(Board.builder().build()));
        Assertions.assertThrows(DuplicatedGameException.class, () -> game.joinTheGame("x", 10));
    }

    @DisplayName("check finding ships for game")
    @Test
    void checkFindingShipsForGame() {
        var board = generateSampleBoard();
        board.getShips().stream().filter(ship -> Submarine.getValue() == ship.getType()).findAny().get().setDestroyed(Boolean.TRUE); //destroy one ship
        Mockito.when(historyProvider.findGame(Mockito.any())).thenReturn(Optional.of(board));

        List<Ship> xShips = game.findShips("x", true);
        Assertions.assertEquals(1, xShips.size());
        Assertions.assertTrue(xShips.stream().allMatch(ship -> ship.getType() == Submarine.getValue()));
    }

    @DisplayName("check proper handling of invalid game size")
    @Test
    void checkBoardSize() {
        Mockito.when(historyProvider.findGame(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidParamException.class, () -> game.joinTheGame("x", 0));
    }

    @DisplayName("positive scenario for shot")
    @Test
    void checkShotPositive() {
        Board board = Board.builder()
                .gameId("x")
                .board(new TwoDimensionalBoard(10))
                .ships(List.of(
                        Ship.builder()
                                .type(2)
                                .location(List.of(Position.builder().x(4).y(5).build(), Position.builder().x(4).y(6).build()))
                                .build()
                ))
                .status(GameStatus.RUNNING)
                .build();
        log.info("Board \n{}", board.getUpdatedBoard());

        Mockito.when(historyProvider.findGame(Mockito.any())).thenReturn(Optional.of(board)); //running game
        ShotResult shotResult = game.opponentShot("x", Position.builder().x(4).y(5).build());
        Assertions.assertEquals(ShotResult.HIT, shotResult);
    }

    @DisplayName("check shot for game that not exists")
    @Test
    void checkShotNoGame() {
        Mockito.when(historyProvider.findGame(Mockito.any())).thenReturn(Optional.empty()); //running game
        Assertions.assertThrows(NoGameFoundException.class, () -> game.opponentShot("x", Position.builder().x(0).y(0).build()));
    }

    @DisplayName("check shot for game that is already over")
    @Test
    void checkGameOver() {
        var board = Board.builder()
                .gameId("x")
                .status(GameStatus.OVER)
                .build();
        Mockito.when(historyProvider.findGame(Mockito.any())).thenReturn(Optional.of(board)); //running game
        Assertions.assertThrows(GameOverException.class, () -> game.opponentShot("x", Position.builder().x(0).y(0).build()));
    }

    private Board generateSampleBoard() {
        Board board = Board.builder()
                .gameId("x")
                .board(new TwoDimensionalBoard(10))
                .ships(List.of(
                        Ship.builder()
                                .type(2)
                                .location(List.of(Position.builder().x(4).y(5).build(), Position.builder().x(4).y(6).build()))
                                .build()
                ))
                .status(GameStatus.RUNNING)
                .build();
        log.info("Board \n{}", board.getUpdatedBoard());
        return board;
    }

}
