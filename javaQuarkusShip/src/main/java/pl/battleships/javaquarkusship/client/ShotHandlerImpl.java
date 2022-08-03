package pl.battleships.javaquarkusship.client;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.battleships.core.api.ShotHandler;
import pl.battleships.core.model.Position;
import pl.battleships.core.model.ShotResult;
import pl.battleships.javaquarkusship.config.GameProperties;
import pl.battleships.javaquarkusship.mapper.GameMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ShotHandlerImpl implements ShotHandler {

    @Inject
    GameProperties properties;

    @ConfigProperty(name = "quarkus.rest-client.game.url")
    String opponentUrl;

    @Inject
    @RestClient
    RestClientGameApi restClientGameApi;

    @Inject
    GameMapper mapper;

    @Override
    public ShotResult shotToOpponent(String s, Position position) {
        Log.debug("Sleeping " + properties.sleep() + "ms and making shot to " + opponentUrl + " with position " + position);
        try {
            Thread.sleep(properties.sleep());
        } catch (InterruptedException e) {
            Log.error("Problem while sleeping", e);
        }
        return mapper.map(
                restClientGameApi.shot(s, mapper.map(position))
        );
    }
}
