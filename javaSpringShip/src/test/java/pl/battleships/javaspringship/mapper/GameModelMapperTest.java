package pl.battleships.javaspringship.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;
import pl.battleships.core.model.ShotResult;

import java.util.List;

class GameModelMapperTest {

    private GameModelMapper mapper = new GameModelMapper(new ModelMapper());

    @DisplayName("check mappers for Ship to ShipDto and vice verse")
    @Test
    void checkMapperForShip() {
        Ship ship = Ship.builder().type(2).destroyed(true).location(List.of(
                Position.builder().hit(true).x(1).y(2).build(),
                Position.builder().hit(true).x(2).y(2).build()
        )).build();

        ShipDto dto = mapper.convert(ship);

        Assertions.assertEquals(ship.getType(), dto.getType().getValue());
        Assertions.assertEquals(true, dto.getDestroyed());
        Assertions.assertFalse(dto.getLocaction().isEmpty());
        Assertions.assertEquals(2, dto.getLocaction().size());
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

    @DisplayName("check mappers for Position to PositionDto and vice verse")
    @Test
    void checkMapperForShotResult() {
        ShotResult shotResult = ShotResult.DESTROYED;

        ShotStatusDto dto = mapper.convert(shotResult);
        Assertions.assertEquals(ShotStatusDto.DESTROYED, dto);
    }

}
