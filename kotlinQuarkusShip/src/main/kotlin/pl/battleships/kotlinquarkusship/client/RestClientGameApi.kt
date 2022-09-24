package pl.battleships.kotlinquarkusship.client

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import pl.battleships.api.dto.PositionDto
import pl.battleships.api.dto.ShotStatusDto
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam


@Path("/game")
@RegisterRestClient(configKey = "game")
interface RestClientGameApi {

    @POST
    @Path("/{id}/shot")
    fun shot(@PathParam("id") id: String, @Valid @NotNull positionDto: PositionDto): ShotStatusDto
}