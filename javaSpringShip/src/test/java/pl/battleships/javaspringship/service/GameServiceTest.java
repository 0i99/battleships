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
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.core.exception.DuplicatedGameException;
import pl.battleships.core.model.Board;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;
import pl.battleships.core.model.ShotResult;
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
        Mockito.when(battleshipGame.joinTheGame(Mockito.any(), Mockito.anyInt())).thenReturn(
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
        Mockito.when(battleshipGame.joinTheGame(Mockito.any(), Mockito.anyInt())).thenThrow(new DuplicatedGameException());
        //do not catch
        Assertions.assertThrows(DuplicatedGameException.class, () -> gameService.joinTheGame(new GameDto().id("x").size(10)));
    }

    @DisplayName("check finding ships for game")
    @Test
    void checkFindingShipsForGame() {
        Mockito.when(battleshipGame.findShips(Mockito.any(), Mockito.anyBoolean())).thenReturn(
                List.of(
                        Ship.builder().type(1).location(List.of(Position.builder().x(5).y(6).build())).build(),
                        Ship.builder().type(1).location(List.of(Position.builder().x(8).y(9).build())).build()
                )
        );
        List<ShipDto> ships = gameService.findShips("x", true);
        Assertions.assertEquals(2, ships.size());
        Assertions.assertTrue(ships.stream().allMatch(ship -> ship.getType().getValue().equals(1)));
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

    @DisplayName("check positive case of opponent shot")
    @Test
    void checkOpponentShot() {
        Mockito.when(battleshipGame.opponentShot(Mockito.any(), Mockito.any())).thenReturn(ShotResult.HIT);
        ShotStatusDto shot = gameService.opponentShot("x", new PositionDto().x(1).y(1));
        Assertions.assertEquals(ShotStatusDto.HIT, shot);

    }
}
