package pl.battleships.javaquarkusship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import pl.battleships.core.model.GameStatus;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;
import pl.battleships.javaquarkusship.api.dto.GameStatusDto;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.javaquarkusship.api.dto.ShotStatusDto;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(
        componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GameMapper {

    GameMapper INSTANCE = getMapper(GameMapper.class);

    PositionDto map(Position position);

    Position map(PositionDto dto);

    ShotStatusDto map(ShotResult position);

    ShotResult map(ShotStatusDto dto);

    @ValueMapping(source = "RUNNING", target = "RUNNING")
    @ValueMapping(source = "OVER", target = "OVER")
    @ValueMapping(source = "NOT_STARTED", target = MappingConstants.NULL)
    GameStatusDto map(GameStatus status);
}
