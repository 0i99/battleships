package pl.battleships.javaquarkusship.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.battleships.javaquarkusship.api.dto.GameStatusDto;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.javaquarkusship.api.dto.ShotStatusDto;
import pl.battleships.core.model.GameStatus;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;

class GameMapperTest {

    @DisplayName("check position mappings")
    @Test
    void checkPositionMappings() {
        Position position = GameMapper.INSTANCE.map(new PositionDto().x(1).y(2).hit(true));
        Assertions.assertEquals(1, position.getX());
        Assertions.assertEquals(2, position.getY());
        Assertions.assertTrue(position.isHit());

        PositionDto positionDto = GameMapper.INSTANCE.map(Position.builder().x(3).y(4).hit(true).build());
        Assertions.assertEquals(3, positionDto.getX());
        Assertions.assertEquals(4, positionDto.getY());
        Assertions.assertTrue(positionDto.getHit());
    }

    @DisplayName("check shot mappings")
    @Test
    void checkShotMappings(){
        Assertions.assertEquals(ShotResult.HIT,GameMapper.INSTANCE.map(ShotStatusDto.HIT));
        Assertions.assertEquals(ShotResult.MISSED,GameMapper.INSTANCE.map(ShotStatusDto.MISSED));
        Assertions.assertEquals(ShotResult.DESTROYED,GameMapper.INSTANCE.map(ShotStatusDto.DESTROYED));
        Assertions.assertEquals(ShotResult.ALL_DESTROYED,GameMapper.INSTANCE.map(ShotStatusDto.ALL_DESTROYED));

        Assertions.assertEquals(ShotStatusDto.HIT,GameMapper.INSTANCE.map(ShotResult.HIT));
        Assertions.assertEquals(ShotStatusDto.MISSED,GameMapper.INSTANCE.map(ShotResult.MISSED));
        Assertions.assertEquals(ShotStatusDto.DESTROYED,GameMapper.INSTANCE.map(ShotResult.DESTROYED));
        Assertions.assertEquals(ShotStatusDto.ALL_DESTROYED,GameMapper.INSTANCE.map(ShotResult.ALL_DESTROYED));
    }

    @DisplayName("check game mappings")
    @Test
    void gameShotMappings(){
        Assertions.assertEquals(GameStatusDto.OVER,GameMapper.INSTANCE.map(GameStatus.OVER));
        Assertions.assertEquals(GameStatusDto.RUNNING,GameMapper.INSTANCE.map(GameStatus.RUNNING));
        Assertions.assertNull(GameMapper.INSTANCE.map(GameStatus.NOT_STARTED));
    }
}
