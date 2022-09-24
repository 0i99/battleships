package pl.battleships.kotlinquarkusship.extension

import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.model.ShotResult

fun ShotResult.toShotStatusDto(): ShotStatusDto = ShotStatusDto.values().first { it.value == name }

fun ShotStatusDto.toShotResult(): ShotResult = ShotResult.valueOf(value)