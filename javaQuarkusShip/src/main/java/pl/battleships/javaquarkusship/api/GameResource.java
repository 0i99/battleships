package pl.battleships.javaquarkusship.api;


import io.quarkus.logging.Log;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import pl.battleships.javaquarkusship.api.dto.GameDto;
import pl.battleships.javaquarkusship.api.dto.GameStatusDto;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.javaquarkusship.api.dto.ShotStatusDto;
import pl.battleships.core.exception.*;
import pl.battleships.javaquarkusship.service.GameService;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class GameResource implements GameApi {

    @Inject
    GameService gameService;

    @Override
    public List<PositionDto> getAllShots(String id) {
        return gameService.getAllShots(id);
    }

    @Override
    public GameStatusDto getGameStatus(String id) {
        return gameService.getGameStatus(id);
    }

    @Override
    public void joinGame(GameDto gameDto) {
        gameService.joinTheGame(gameDto);
    }

    @Override
    public ShotStatusDto shot(String id, PositionDto positionDto) {
        return gameService.opponentShot(id, positionDto);
    }

    @ServerExceptionMapper
    public RestResponse<Void> duplicatedGameException(DuplicatedGameException x) {
        return RestResponse.status(Response.Status.CONFLICT);
    }

    @ServerExceptionMapper
    public RestResponse<Void> noGameFoundException(NoGameFoundException x) {
        return RestResponse.status(Response.Status.NOT_FOUND);
    }

    @ServerExceptionMapper
    public RestResponse<Void> gameOver(GameOverException x) {
        Log.info("Game over");
        return RestResponse.status(Response.Status.GONE);
    }

    @ServerExceptionMapper
    public RestResponse<Void> invalidParams(InvalidParamException x) {
        return RestResponse.status(422);
    }

    @ServerExceptionMapper
    public RestResponse<Void> invalidMoveException(InvalidMoveException x) {
        return RestResponse.status(Response.Status.FORBIDDEN);
    }

}
