package pl.battleships.kotlinspringship.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import pl.battleships.api.dto.PositionDto
import pl.battleships.core.exception.GameOverException
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

        val rsp = mockMvc.post("/game/x/shot") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(PositionDto(1, 1, false))
        }.andExpect {
            status { isGone() }
        }.andReturn()
    }
}