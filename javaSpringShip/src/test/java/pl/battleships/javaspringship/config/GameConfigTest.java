package pl.battleships.javaspringship.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class GameConfigTest {

    @Autowired
    GameProperties properties;

    @DisplayName("check if properties properly parsed")
    @Test
    void checkProperties() {
        Assertions.assertEquals("none", properties.getTeam());
        Assertions.assertEquals("localhost:8080", properties.getOpponentUrl());
    }

}
