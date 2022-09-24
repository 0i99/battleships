package pl.battleships.kotlinquarkusship.resource

import pl.battleships.api.ApplicationInfoStackApi
import pl.battleships.api.dto.InfoDto
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class InformationResource : ApplicationInfoStackApi {

    override fun info(): InfoDto {
        return InfoDto("kotlinQuarkus", listOf("kotlin", "quarkus"))
    }
}