package pl.battleships.kotlinspringship.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(InformationController::class)
class InformationControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `check information api`() {
        mockMvc.get("/information") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{\"name\":\"kotlinSpring\",\"details\":[\"kotlin\",\"spring\"]}") }
        }
    }
}