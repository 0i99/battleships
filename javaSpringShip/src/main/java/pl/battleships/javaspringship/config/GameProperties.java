package pl.battleships.javaspringship.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game")
@Getter
@Setter
public class GameProperties {
    private String team;
    private String opponentUrl;
    private int timeout;
    private long sleep;
}
