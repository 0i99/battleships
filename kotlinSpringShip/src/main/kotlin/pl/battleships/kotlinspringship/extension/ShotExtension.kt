package pl.battleships.kotlinspringship.extension

import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.model.ShotResult

fun ShotResult.toShotStatusDto(): ShotStatusDto = ShotStatusDto.valueOf(name)

fun ShotStatusDto.toShotResult(): ShotResult = ShotResult.valueOf(value)