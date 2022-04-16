package pl.battleships.core.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
class BattleshipGameTest {

    @Mock
    HistoryProvider historyProvider;

    @InjectMocks
    BattleshipGameImpl gameService;

//    @DisplayName("check positive scenario of joining to the game")
//    @Test
//    void checkJoinTheGame() {
//        Game game = new Game().id("x").size(10);
//        Board board = gameService.joinTheGame(game);
//        Assertions.assertNotNull(board);
//        Assertions.assertNotNull(board.getBoard());
//        Assertions.assertEquals(10, board.getBoard().getSize());
//        Assertions.assertEquals("x", board.getGameId());
//        Assertions.assertEquals(6, board.getShips().size());
//    }
//
//    @DisplayName("check proper handling of duplicates")
//    @Test
//    void checkJoinForDuplicates() {
//        Game game = new Game().id("x").size(10);
//        gameService.joinTheGame(game);
//        Assertions.assertThrows(DuplicatedGameException.class, () -> gameService.joinTheGame(game));
//    }
//
//    @DisplayName("check finding ships for game")
//    @Test
//    void checkFindingShipsForGame() {
//        Board board = gameService.joinTheGame(new Game().id("x").size(10));
//        Assertions.assertNotNull(board);
//
//        board.getShips().stream().filter(ship -> Ship.TypeEnum.Submarine.equals(ship.getType())).findAny().get().destroyed(Boolean.TRUE); //destroy one ship
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
//    @DisplayName("positive scenario for shot")
//    @Test
//    void checkShotPositive() {
//        var game = new Game().id("x").size(10);
//        Board board = gameService.joinTheGame(game);
//        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
//        Mockito.when(historyProvider.opponentShot(captor.capture(), Mockito.any())).thenReturn(ShotResult.HIT);
//
//        ShotResult shotResult = gameService.opponentShot("x", new Position().x(0).y(0));
//        Assertions.assertEquals(ShotResult.HIT, shotResult);
//        Assertions.assertEquals(board.getBoard().getHash(), captor.getValue().getBoard().getHash());
//    }
//
//    @DisplayName("check shot for game that not exists")
//    @Test
//    void checkShotNoGame() {
//        Assertions.assertThrows(NoGameFoundException.class, () -> gameService.opponentShot("x", new Position().x(0).y(0)));
//    }
//
//    @DisplayName("check shot for game that is already over")
//    @Test
//    void checkGameOver() {
//        var game = new Game().id("x").size(10);
//        Board board = gameService.joinTheGame(game);
//        board.getShips().forEach(ship -> ship.setDestroyed(true));
//        board.updateBoard();
//        Assertions.assertThrows(GameOverException.class, () -> gameService.opponentShot("x", new Position().x(0).y(0)));
//    }
//

}

