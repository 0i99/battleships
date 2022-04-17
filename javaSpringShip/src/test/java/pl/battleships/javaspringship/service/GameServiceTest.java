package pl.battleships.javaspringship.service;



import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.battleships.api.dto.GameDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.core.exception.DuplicatedGameException;
import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;
import pl.battleships.javaspringship.mapper.GameModelMapper;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@Slf4j
class GameServiceTest {

    @Mock
    BattleshipGame battleshipGame;

    @Spy
    GameModelMapper mapper = new GameModelMapper(new ModelMapper());

    @Mock
    HistoryProvider historyProvider;

    @InjectMocks
    GameServiceImpl gameService;

    @DisplayName("check positive scenario of joining to the game")
    @Test
    void checkJoinTheGame() {
        GameDto game = new GameDto().id("x").size(10);
        Mockito.when(battleshipGame.joinTheGame(Mockito.any(), Mockito.any())).thenReturn(
                Board.builder().ships(
                        List.of(
                                Ship.builder().type(1).destroyed(true).location(
                                        List.of(Position.builder().x(1).y(1).hit(true).build())).build(),
                                Ship.builder().type(2).destroyed(false).location(
                                        List.of(Position.builder().x(4).y(4).build())).build())
                ).build()
        );
        List<ShipDto> ships = gameService.joinTheGame(game);
        Assertions.assertNotNull(ships);
        Assertions.assertEquals(2, ships.size());
    }

    @DisplayName("check proper handling of duplicates")
    @Test
    void checkJoinForDuplicates() {
        GameDto game = new GameDto().id("x").size(10);
        Mockito.when(battleshipGame.joinTheGame(Mockito.any(), Mockito.any())).thenThrow(new DuplicatedGameException());
        //do not catch
        Assertions.assertThrows(DuplicatedGameException.class, () -> gameService.joinTheGame(game));
    }
//
//    @DisplayName("check finding ships for game")
//    @Test
//    void checkFindingShipsForGame() {
//        BoardReadyToPlay boardReadyToPlay = gameService.joinTheGame(new Game().id("x").size(10));
//        Assertions.assertNotNull(boardReadyToPlay);
//
//        boardReadyToPlay.getShips().stream().filter(ship -> Ship.TypeEnum.Submarine.equals(ship.getType())).findAny().get().destroyed(Boolean.TRUE); //destroy one ship
//        List<Ship> xShips = gameService.findShips("x", true);
//        Assertions.assertEquals(1, xShips.size());
//        Assertions.assertTrue(xShips.stream().allMatch(ship -> ship.getType().equals(Submarine)));
//    }
//
//    @DisplayName("check proper handling of invalid game size")
//    @Test
//    void checkBoardSize() {
//        var game = new Game().id("x").size(0);
//        Assertions.assertThrows(InvalidParamException.class, () -> gameService.joinTheGame(game));
//    }
//
//    @DisplayName("")
//    @Test
//    void checkShotPositive() {
//        var game = new Game().id("x").size(10);
//        BoardReadyToPlay board = gameService.joinTheGame(game);
//        ArgumentCaptor<BoardReadyToPlay> captor = ArgumentCaptor.forClass(BoardReadyToPlay.class);
//        Mockito.when(shotService.shot(captor.capture(), Mockito.any())).thenReturn(ShotResult.HIT);
//
//        ShotResult shotResult = gameService.shot("x", new Position().x(0).y(0));
//        Assertions.assertEquals(ShotResult.HIT, shotResult);
//        Assertions.assertEquals(board.getBoard().getHash(), captor.getValue().getBoard().getHash());
//    }
//
//    @DisplayName("check shot for game that not exists")
//    @Test
//    void checkShotNoGame() {
//        Assertions.assertThrows(NoGameFoundException.class, () -> gameService.shot("x", new Position().x(0).y(0)));
//    }

}
