package pl.battleships.kotlinspringship.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import pl.battleships.api.InformationApi
import pl.battleships.api.dto.InfoDto

@RestController
@CrossOrigin
class InformationController : InformationApi {

    override fun info(): ResponseEntity<InfoDto> {
        return ResponseEntity.ok(InfoDto("kotlinSpring", listOf("kotlin", "spring")))
    }
}