package pl.battleships.kotlinspringship.extension

import pl.battleships.api.dto.PositionDto
import pl.battleships.core.model.Position

fun Position.toPositionDto(): PositionDto = PositionDto(x = x, y = y, hit = isHit)

fun PositionDto.toPosition(): Position = Position.builder().x(x).y(y).hit(hit == true).build()