package pl.battleships.javaspringship.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import pl.battleships.api.dto.GameStatusDto;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.model.GameStatus;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;

@RequiredArgsConstructor
public class GameModelMapper {
    private final ModelMapper modelMapper;

    public PositionDto convert(Position position) {
        return modelMapper.map(position, PositionDto.class);
    }

    public Position convert(PositionDto dto) {
        return Position.builder().x(dto.getX()).y(dto.getY()).hit(dto.getHit()).build();
    }

    public ShotStatusDto convert(ShotResult shotResult) {
        return ShotStatusDto.fromValue(shotResult.name());
    }

    public GameStatusDto convert(GameStatus gameStatus) {
        return GameStatusDto.fromValue(gameStatus.name());
    }
}
