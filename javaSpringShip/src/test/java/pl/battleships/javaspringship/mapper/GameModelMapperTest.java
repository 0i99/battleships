package pl.battleships.javaspringship.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import pl.battleships.api.dto.GameStatusDto;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.model.GameStatus;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;

class GameModelMapperTest {

    private GameModelMapper mapper = new GameModelMapper(new ModelMapper());

    @DisplayName("check mappers for game status")
    @Test
    void checkMapperForGameStatus() {
        Assertions.assertEquals(GameStatusDto.RUNNING, mapper.convert(GameStatus.RUNNING));
        Assertions.assertEquals(GameStatusDto.OVER, mapper.convert(GameStatus.OVER));
    }

    @DisplayName("check mappers for Position to PositionDto and vice verse")
    @Test
    void checkMapperForPosition() {
        Position position = Position.builder().x(1).y(2).hit(true).build();

        PositionDto dto = mapper.convert(position);
        Assertions.assertEquals(position.getX(), dto.getX());
        Assertions.assertEquals(position.getY(), dto.getY());
        Assertions.assertEquals(position.isHit(), dto.getHit());
    }

    @DisplayName("check mappers for ShotResult to ShotStatusDto and vice verse")
    @Test
    void checkMapperForShotResult() {
        ShotResult shotResult = ShotResult.DESTROYED;

        ShotStatusDto dto = mapper.convert(shotResult);
        Assertions.assertEquals(ShotStatusDto.DESTROYED, dto);
    }

}
