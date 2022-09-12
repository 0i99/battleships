package pl.battleships.kotlinspringship.extension

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import pl.battleships.core.model.GameStatus
import pl.battleships.core.model.Position
import pl.battleships.core.model.ShotResult

class ExtensionsTest {

    @DisplayName("check mappings for positions")
    @Test
    fun checkPositionMappings(){
        val positionDto = PositionDto(1,2,true)
        val position = positionDto.toPosition()
        Assertions.assertEquals(1,position.x)
        Assertions.assertEquals(2,position.y)
        Assertions.assertTrue(position.isHit)

        val position1 = Position.builder().x(3).y(4).hit(true).build()
        val positionDto1 = position1.toPositionDto()
        Assertions.assertEquals(3,positionDto1.x)
        Assertions.assertEquals(4,positionDto1.y)
        Assertions.assertTrue(positionDto1.hit == true)
    }

    @DisplayName("check mappings for shots")
    @Test
    fun checkShotExtension(){
        val dto = ShotStatusDto.DESTROYED
        val shot = dto.toShotResult()
        Assertions.assertEquals(ShotResult.DESTROYED,shot)

        val shot1 = ShotStatusDto.ALL_DESTROYED
        val dto1 = shot1.toShotResult()
        Assertions.assertEquals(ShotResult.ALL_DESTROYED,dto1)
    }

    @DisplayName("check mappings for games")
    @Test
    fun checkGameExtenstion(){
        val game = GameStatus.OVER
        val dto = game.toGameStatusDto()
        Assertions.assertEquals(GameStatusDto.OVER,dto)
    }
}