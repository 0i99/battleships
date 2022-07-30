package pl.battleships.javaquarkusship.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.battleships.javaquarkusship.api.dto.GameDto;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.core.exception.DuplicatedGameException;
import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;

import javax.inject.Inject;
import java.util.List;

@QuarkusTest
class GameServiceTest {

    @InjectMock
    BattleshipGame battleshipGame;

    @InjectMock
    HistoryProvider historyProvider;

    @Inject
    GameServiceImpl gameService;

    @DisplayName("check positive scenario of joining to the game")
    @Test
    void checkJoinTheGame() {
        GameDto game = new GameDto().id("x").size(10);
        Mockito.when(battleshipGame.start(Mockito.any(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(
                Board.builder().ships(
                        List.of(
                                Ship.builder().type(1).destroyed(true).location(
                                        List.of(Position.builder().x(1).y(1).hit(true).build())).build(),
                                Ship.builder().type(2).destroyed(false).location(
                                        List.of(Position.builder().x(4).y(4).build())).build())
                ).build()
        );
        gameService.joinTheGame(game);
    }

    @DisplayName("check proper handling of duplicates")
    @Test
    void checkJoinForDuplicates() {
        Mockito.when(battleshipGame.start(Mockito.any(), Mockito.anyInt(), Mockito.anyBoolean())).thenThrow(new DuplicatedGameException());
        //do not catch
        Assertions.assertThrows(DuplicatedGameException.class, () -> gameService.joinTheGame(new GameDto().id("x").size(10)));
    }

    @DisplayName("check getting all shots")
    @Test
    void checkGetAllShots() {
        Mockito.when(historyProvider.getAllShots(Mockito.any())).thenReturn(
                List.of(
                        Position.builder().x(1).y(1).hit(true).build(),
                        Position.builder().x(1).y(2).hit(false).build(),
                        Position.builder().x(1).y(3).hit(false).build()
                )
        );
        List<PositionDto> allShots = gameService.getAllShots("x");
        Assertions.assertEquals(3, allShots.size());
        Assertions.assertEquals(1L, allShots.stream().filter(dto -> Boolean.TRUE.equals(dto.getHit())).count());
    }

}
