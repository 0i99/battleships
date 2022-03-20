package pl.battleships.javaspringship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.battleships.javaspringship.service.GameService;
import pl.battleships.javaspringship.service.GameServiceImpl;
import pl.battleships.javaspringship.service.ShotService;
import pl.battleships.javaspringship.service.ShotServiceImpl;

@Configuration
public class GameConfig {

    @Bean
    public ShotService shotService() {
        return new ShotServiceImpl();
    }

    @Bean
    public GameService gameService(ShotService shotService) {
        return new GameServiceImpl(shotService);
    }
}
