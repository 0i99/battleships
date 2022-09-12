package pl.battleships.kotlinspringship.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import pl.battleships.api.dto.GameDto
import pl.battleships.api.dto.GameStatusDto
import pl.battleships.api.dto.PositionDto
import pl.battleships.core.exception.GameOverException
import pl.battleships.core.exception.NoGameFoundException
import pl.battleships.kotlinspringship.service.GameService

@WebMvcTest(GameController::class)
class GameControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var gameService: GameService

    @Test
    fun `check shot endpoint, game over`() {
        whenever(gameService.opponentShot(any(), any())).thenThrow(GameOverException())

        mockMvc.post("/game/x/shot") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(PositionDto(1, 1, false))
        }.andExpect {
            status { isGone() }
        }
    }

    @Test
    fun `check shot endpoint, not game found`() {
        whenever(gameService.opponentShot(any(), any())).thenThrow(NoGameFoundException())
        mockMvc.post("/game/x/shot") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(PositionDto(1, 1, false))
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `positive join the game`() {
        mockMvc.post("/game") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(GameDto("y", 10, true))
        }.andExpect {
            status { is2xxSuccessful() }
        }

        verify(gameService, times(1)).joinTheGame(any())
    }

    @Test
    fun `check get all shots`() {
        whenever(gameService.getAllShots(any())).thenReturn(
            listOf(PositionDto(1, 1, true), PositionDto(2, 3, true))
        )
        mockMvc.get("/game/x/shot") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }

    @Test
    fun `check game status`() {
        whenever(gameService.getGameStatus(any())).thenReturn(GameStatusDto.RUNNING)

        mockMvc.get("/game/x") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }
}