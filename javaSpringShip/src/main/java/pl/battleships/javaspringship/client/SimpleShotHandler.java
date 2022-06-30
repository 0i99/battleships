package pl.battleships.javaspringship.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import pl.battleships.core.api.ShotHandler;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;
import pl.battleships.javaspringship.config.GameProperties;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SimpleShotHandler implements ShotHandler {

    private final GameProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public ShotResult shotToOpponent(String gameId, Position position) {
        log.debug("Sleeping {}ms and making shot to {} with position {}", properties.getSleep(),properties.getOpponentUrl(), position);
        try {
            Thread.sleep(properties.getSleep());
        } catch (InterruptedException e) {
            log.error("Problem while sleeping", e);
        }
        return restTemplate.postForEntity(properties.getOpponentUrl() + "/game/{id}/shot", position, ShotResult.class, Map.of("id", gameId))
                .getBody();
    }
}
