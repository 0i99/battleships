package pl.battleships.kotlinquarkusship.config

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
class GamePropertiesTest {

    @Inject
    lateinit var properties: GameProperties

    @Test
    fun `check proper context and properties loading`() {
        Assertions.assertNotNull(properties)
        Assertions.assertEquals("localhost:8060", properties.opponentUrl())
        Assertions.assertEquals(200, properties.sleep())
        Assertions.assertEquals(1100, properties.timeout())
    }
}