package pl.battleships.javaspringship.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.battleships.api.dto.GameDto;
import pl.battleships.api.dto.PositionDto;
import pl.battleships.api.dto.ShipDto;
import pl.battleships.api.dto.ShotStatusDto;
import pl.battleships.core.api.ShotHandler;
import pl.battleships.core.exception.DuplicatedGameException;
import pl.battleships.core.exception.GameOverException;
import pl.battleships.core.exception.NoGameFoundException;
import pl.battleships.javaspringship.service.GameService;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ShotHandler shotHandler;

    @MockBean
    GameService gameService;

    @DisplayName("positive join the game")
    @Test
    void joinGame() throws Exception {
        var ships = List.of(
                new ShipDto().type(ShipDto.TypeEnum.Destroyer).destroyed(false).locaction(
                        List.of(new PositionDto().x(1).y(1), new PositionDto().x(2).y(2))
                )
        );
        Mockito.when(gameService.joinTheGame(Mockito.any())).thenReturn(ships);
        String expected = mapper.writeValueAsString(ships);
        mockMvc.perform(
                        post("/game")
                                .content(mapper.writeValueAsString(
                                        new GameDto().id(UUID.randomUUID().toString()).size(10).startAt(OffsetDateTime.now().plus(5L, ChronoUnit.MINUTES))
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is2xxSuccessful())
                .andExpect(content().json(expected))
                .andReturn();
    }

    @DisplayName("duplicate while joining the game")
    @Test
    void joinGameDuplicate() throws Exception {
        Mockito.when(gameService.joinTheGame(Mockito.any())).thenThrow(new DuplicatedGameException());
        mockMvc.perform(
                post("/game")
                        .content(mapper.writeValueAsString(
                                new GameDto().id(UUID.randomUUID().toString()).size(10).startAt(OffsetDateTime.now().plus(5L, ChronoUnit.MINUTES))
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @DisplayName("check find endpoint")
    @Test
    void find() throws Exception {

        Mockito.when(gameService.findShips(Mockito.any(), Mockito.anyBoolean())).thenReturn(List.of(new ShipDto().type(ShipDto.TypeEnum.Destroyer)));
        MvcResult mvcResult = mockMvc.perform(
                        get("/game/x/ship")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is2xxSuccessful())
                .andReturn();

        var list = mapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        Assertions.assertEquals(1, list.size());
    }

    @DisplayName("check find endpoint, not found")
    @Test
    void findNotFound() throws Exception {
        Mockito.reset(gameService);
        Mockito.when(gameService.findShips(Mockito.any(), Mockito.anyBoolean())).thenThrow(new NoGameFoundException());

        mockMvc.perform(
                get("/game/x/ship")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("check shot endpoint")
    @Test
    void shot() throws Exception {
        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenReturn(ShotStatusDto.HIT);
        mockMvc.perform(
                post("/game/x/shot")
                        .content(mapper.writeValueAsString(
                                new PositionDto().x(1).y(2)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }

    @DisplayName("check shot endpoint, not found")
    @Test
    void shotNotFound() throws Exception {
        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenThrow(new NoGameFoundException());

        mockMvc.perform(
                post("/game/x/shot")
                        .content(mapper.writeValueAsString(
                                new PositionDto().x(1).y(2)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("check shot endpoint, game over")
    @Test
    void gameOVer() throws Exception {
        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenThrow(new GameOverException());

        mockMvc.perform(
                post("/game/x/shot")
                        .content(mapper.writeValueAsString(
                                new PositionDto().x(1).y(2)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(HttpStatus.GONE.value()));
    }

    @DisplayName("check get all shots enpoint")
    @Test
    void getAllShots() throws Exception {
        Mockito.when(gameService.getAllShots(Mockito.any())).thenReturn(List.of(new PositionDto().x(1).y(2).hit(Boolean.FALSE), new PositionDto().x(6).y(8).hit(Boolean.TRUE)));
        mockMvc.perform(
                get("/game/x/shot")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }

}