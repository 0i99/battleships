package pl.battleships.javaquarkusship.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.battleships.javaquarkusship.api.dto.GameDto;
import pl.battleships.javaquarkusship.api.dto.PositionDto;
import pl.battleships.javaquarkusship.api.dto.ShotStatusDto;
import pl.battleships.core.exception.DuplicatedGameException;
import pl.battleships.core.exception.GameOverException;
import pl.battleships.core.exception.NoGameFoundException;
import pl.battleships.javaquarkusship.service.GameService;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GameResourcesTest {

    @InjectMock
    GameService gameService;

    @DisplayName("positive join the game")
    @Test
    void joinGame() throws Exception {
        given().header("Content-type", "application/json")
                .and()
                .body(new GameDto().id(UUID.randomUUID().toString()).size(10))
                .when()
                .post("/game")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @DisplayName("duplicate while joining the game")
    @Test
    void joinGameDuplicate() throws Exception {
        Mockito.doThrow(DuplicatedGameException.class).when(gameService).joinTheGame(Mockito.any());
        given().header("Content-type", "application/json")
                .and()
                .body(new GameDto().id(UUID.randomUUID().toString()).size(10))
                .when()
                .post("/game")
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }


    @DisplayName("check shot endpoint")
    @Test
    void shot() throws Exception {
        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenReturn(ShotStatusDto.HIT);
        given()
                .contentType(ContentType.JSON)
                .and()
                .accept(ContentType.JSON)
                .body(new PositionDto().x(1).y(2))
                .when()
                .post("/game/x/shot")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(is("\"HIT\""));
    }

    @DisplayName("check shot endpoint, not found")
    @Test
    void shotNotFound() throws Exception {
        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenThrow(new NoGameFoundException());
        given()
                .contentType(ContentType.JSON)
                .and()
                .accept(ContentType.JSON)
                .body(new PositionDto().x(1).y(2))
                .when()
                .post("/game/x/shot")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @DisplayName("check shot endpoint, game over")
    @Test
    void gameOVer() throws Exception {
        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenThrow(new GameOverException());

        given()
                .contentType(ContentType.JSON)
                .and()
                .accept(ContentType.JSON)
                .body(new PositionDto().x(1).y(2))
                .when()
                .post("/game/x/shot")
                .then()
                .statusCode(Response.Status.GONE.getStatusCode());
    }

    @DisplayName("check get all shots enpoint")
    @Test
    void getAllShots() throws Exception {
        Mockito.when(gameService.getAllShots(Mockito.any())).thenReturn(List.of(new PositionDto().x(1).y(2).hit(Boolean.FALSE), new PositionDto().x(6).y(8).hit(Boolean.TRUE)));

        io.restassured.response.Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get("/game/x/shot")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .response();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2,response.jsonPath().getInt("[0].y"));
        Assertions.assertTrue(response.jsonPath().getBoolean("[1].hit"));

    }
}
