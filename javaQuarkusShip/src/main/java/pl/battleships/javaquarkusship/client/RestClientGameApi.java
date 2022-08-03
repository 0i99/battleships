package pl.battleships.javaquarkusship.client;

import io.swagger.annotations.ApiParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.javaquarkusship.api.dto.ShotStatusDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;

@Path("/game")
@RegisterRestClient(configKey = "game")
public interface RestClientGameApi {

    @POST
    @Path("/{id}/shot")
    ShotStatusDto shot(@PathParam("id") @ApiParam("game id") String id, @Valid @NotNull PositionDto positionDto);
}
