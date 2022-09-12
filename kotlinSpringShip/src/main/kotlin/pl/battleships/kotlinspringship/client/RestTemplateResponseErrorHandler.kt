package pl.battleships.kotlinspringship.client

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import pl.battleships.core.exception.GameOverException
import pl.battleships.core.exception.InvalidMoveException
import pl.battleships.core.exception.NoGameFoundException

class RestTemplateResponseErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean {
        return !response.statusCode.is2xxSuccessful
    }

    override fun handleError(response: ClientHttpResponse) {
        when (response.statusCode) {
            HttpStatus.NOT_FOUND -> throw NoGameFoundException()
            HttpStatus.GONE -> throw GameOverException()
            HttpStatus.FORBIDDEN -> throw InvalidMoveException()
        }
    }
}