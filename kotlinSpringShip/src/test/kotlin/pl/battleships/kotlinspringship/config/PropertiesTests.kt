package pl.battleships.kotlinspringship.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@EnableConfigurationProperties(GameProperties::class)
class PropertiesTests {

    @Autowired
    lateinit var gameProperties: GameProperties

    @Test
    fun `configuration load test`() {
        Assertions.assertEquals(1000, gameProperties.timeout)
        Assertions.assertEquals(100,gameProperties.sleep)
        Assertions.assertEquals("localhost:8050",gameProperties.opponentUrl)
    }
}