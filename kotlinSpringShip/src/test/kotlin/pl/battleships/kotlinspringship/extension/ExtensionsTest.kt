package pl.battleships.kotlinspringship.extension

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pl.battleships.api.dto.PositionDto
import pl.battleships.core.model.Position

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
}