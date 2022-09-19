package pl.battleships.kotlinquarkusship.resources

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.vertx.ext.web.client.predicate.ResponsePredicate.status
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.ws.rs.core.Response

@QuarkusTest
class InformationResourceTest {

    @Test
    fun `check information api`() {
        val stack: List<String> = When {
            get("/information")
        } Then {
            status(Response.Status.OK.statusCode)
            body("name", equalTo("kotlinQuarkus"))
            body("details", not(emptyOrNullString()))
        } Extract {
            path("details")
        }

        Assertions.assertTrue(stack.contains("quarkus"))
    }
}