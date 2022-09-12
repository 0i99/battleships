package pl.battleships.kotlinspringship.extension

import pl.battleships.api.dto.GameStatusDto
import pl.battleships.core.model.GameStatus


fun GameStatus.toGameStatusDto() : GameStatusDto = GameStatusDto.values().first { it.value == name }