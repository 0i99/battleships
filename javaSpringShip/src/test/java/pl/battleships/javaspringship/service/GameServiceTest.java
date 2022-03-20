package pl.battleships.javaspringship.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.battleships.api.model.Game;
import pl.battleships.api.model.Position;
import pl.battleships.api.model.Ship;
import pl.battleships.api.model.ShotResult;
import pl.battleships.javaspringship.exception.DuplicatedGameException;
import pl.battleships.javaspringship.exception.InvalidParamException;
import pl.battleships.javaspringship.exception.NoGameFoundException;
import pl.battleships.javaspringship.model.BoardReadyToPlay;

import java.util.List;

import static pl.battleships.api.model.Ship.TypeEnum.Submarine;

@ExtendWith(MockitoExtension.class)
@Slf4j
class GameServiceTest {

    @Mock
    ShotService shotService;

    @InjectMocks
    GameServiceImpl gameService;

    @DisplayName("check positive scenario of joining to the game")
    @Test
    void checkJoinTheGame() {
        Game game = new Game().id("x").size(10);
        BoardReadyToPlay boardReadyToPlay = gameService.joinTheGame(game);
        Assertions.assertNotNull(boardReadyToPlay);
        Assertions.assertNotNull(boardReadyToPlay.getBoard());
        Assertions.assertEquals(10, boardReadyToPlay.getBoard().getSize());
        Assertions.assertEquals("x", boardReadyToPlay.getGameId());
        Assertions.assertEquals(6, boardReadyToPlay.getShips().size());
    }

    @DisplayName("check proper handling of duplicates")
    @Test
    void checkJoinForDuplicates() {
        Game game = new Game().id("x").size(10);
        gameService.joinTheGame(game);
        Assertions.assertThrows(DuplicatedGameException.class, () -> gameService.joinTheGame(game));
    }

    @DisplayName("check finding ships for game")
    @Test
    void checkFindingShipsForGame() {
        BoardReadyToPlay boardReadyToPlay = gameService.joinTheGame(new Game().id("x").size(10));
        Assertions.assertNotNull(boardReadyToPlay);

        boardReadyToPlay.getShips().stream().filter(ship -> Ship.TypeEnum.Submarine.equals(ship.getType())).findAny().get().destroyed(Boolean.TRUE); //destroy one ship
        List<Ship> xShips = gameService.findShips("x", true);
        Assertions.assertEquals(1, xShips.size());
        Assertions.assertTrue(xShips.stream().allMatch(ship -> ship.getType().equals(Submarine)));
    }

    @DisplayName("check proper handling of invalid game size")
    @Test
    void checkBoardSize() {
        var game = new Game().id("x").size(0);
        Assertions.assertThrows(InvalidParamException.class, () -> gameService.joinTheGame(game));
    }

    @DisplayName("")
    @Test
    void checkShotPositive() {
        var game = new Game().id("x").size(10);
        BoardReadyToPlay board = gameService.joinTheGame(game);
        ArgumentCaptor<BoardReadyToPlay> captor = ArgumentCaptor.forClass(BoardReadyToPlay.class);
        Mockito.when(shotService.shot(captor.capture(), Mockito.any())).thenReturn(ShotResult.HIT);

        ShotResult shotResult = gameService.shot("x", new Position().x(0).y(0));
        Assertions.assertEquals(ShotResult.HIT, shotResult);
        Assertions.assertEquals(board.getBoard().getHash(), captor.getValue().getBoard().getHash());
    }

    @DisplayName("check shot for game that not exists")
    @Test
    void checkShotNoGame() {
        Assertions.assertThrows(NoGameFoundException.class, () -> gameService.shot("x", new Position().x(0).y(0)));
    }

}

