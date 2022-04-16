package pl.battleships.javaspringship.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;
import pl.battleships.core.model.ShotResult;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GameModelMapper {
    private final ModelMapper modelMapper;

    public ShipDto convert(Ship ship) {
        ShipDto dto = modelMapper.map(ship, ShipDto.class);
        dto.setType(ShipDto.TypeEnum.fromValue(ship.getType()));
        dto.setLocaction(ship.getLocation().stream().map(this::convert).collect(Collectors.toList()));
        return dto;
    }

    public PositionDto convert(Position position) {
        return modelMapper.map(position, PositionDto.class);
    }

    public Position convert(PositionDto dto) {
        return modelMapper.map(dto, Position.class);
    }

    public ShotStatusDto convert(ShotResult shotResult) {
        return ShotStatusDto.fromValue(shotResult.getValue());
    }
}
