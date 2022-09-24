package pl.battleships.kotlinquarkusship.resources

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.vertx.ext.web.client.predicate.ResponsePredicate
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.core.exception.GameOverException
import pl.battleships.core.exception.NoGameFoundException
import pl.battleships.kotlinquarkusship.service.GameService
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
class GameResourceTest {

    @InjectMock
    lateinit var gameService: GameService

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `check shot endpoint, game over`() {
        every{ gameService.opponentShot(any(), any()) } throws GameOverException()

        Given {
            contentType(ContentType.JSON)
            body( objectMapper.writeValueAsString(PositionDto(1,2,true)) )
        } When {
            post("/game/x/shot")
        } Then {
            statusCode(Response.Status.GONE.statusCode)
        }
    }

    @Test
    fun `check shot endpoint, not game found`() {
        every{ gameService.opponentShot(any(), any()) } throws NoGameFoundException()

        Given {
            contentType(ContentType.JSON)
            body( objectMapper.writeValueAsString(PositionDto(1,2,true)) )
        } When {
            post("/game/x/shot")
        } Then {
            statusCode(Response.Status.NOT_FOUND.statusCode)
        }
    }

    @Test
    fun `positive join the game`() {
        every { gameService.joinTheGame(any()) } just Runs //Unit

        Given {
            contentType(ContentType.JSON)
            body( objectMapper.writeValueAsString(GameDto("x",10,true)) )
        } When {
            post("/game")
        } Then {
            statusCode(Response.Status.NO_CONTENT.statusCode)
        }

        verify {
            gameService.joinTheGame(any())
        }
    }

    @Test
    fun `check get all shots`() {
        every { gameService.getAllShots(any()) } returns listOf(PositionDto(1, 1, true), PositionDto(2, 3, true))

        When {
            get("/game/x/shot")
        } Then {
            statusCode(Response.Status.OK.statusCode)
        }
    }

    @Test
    fun `check game status`() {
        every { gameService.getGameStatus(any()) } returns GameStatusDto.RUNNING

        When {
            get("/game/x")
        } Then {
            statusCode(Response.Status.OK.statusCode)
        }
    }
}