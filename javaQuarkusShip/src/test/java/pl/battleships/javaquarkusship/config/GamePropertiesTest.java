package pl.battleships.javaquarkusship.config;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class GamePropertiesTest {

    @Inject
    GameProperties properties;

    @Test
    void checkLoadingProperties() {
        Assertions.assertNotNull(properties);
        Assertions.assertEquals("none", properties.team());
        Assertions.assertEquals(1500, properties.sleep());
    }
}
