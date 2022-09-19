package pl.battleships.kotlinquarkusship.resource

import pl.battleships.api.ApplicationInfoStackApi
import pl.battleships.api.dto.InfoDto

class InformationResource : ApplicationInfoStackApi {

    override fun info(): InfoDto {
        return InfoDto("kotlinQuarkus", listOf("kotlin", "quarkus"))
    }
}