package pl.battleships.javaspringship.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@Slf4j
@WebMvcTest(GameController.class)
class GameControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper mapper;
//
//    @MockBean
//    GameService gameService;
//
//    @DisplayName("positive join the game")
//    @Test
//    void joinGame() throws Exception {
//        Board board = prepareGame();
//        Mockito.when(gameService.joinTheGame(Mockito.any())).thenReturn(board);
//        String expected = mapper.writeValueAsString(board.getShips());
//        mockMvc.perform(
//                        post("/game")
//                                .content(mapper.writeValueAsString(
//                                        new Game().id(UUID.randomUUID().toString()).size(10).startAt(OffsetDateTime.now().plus(5L, ChronoUnit.MINUTES))
//                                ))
//                                .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().is2xxSuccessful())
//                .andExpect(content().json(expected))
//                .andReturn();
//    }
//
//    @DisplayName("duplicate while joining the game")
//    @Test
//    void joinGameDuplicate() throws Exception {
//        Board board = prepareGame();
//        Mockito.when(gameService.joinTheGame(Mockito.any())).thenThrow(new DuplicatedGameException());
//
//        mockMvc.perform(
//                post("/game")
//                        .content(mapper.writeValueAsString(
//                                new Game().id(UUID.randomUUID().toString()).size(10).startAt(OffsetDateTime.now().plus(5L, ChronoUnit.MINUTES))
//                        ))
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().is(HttpStatus.CONFLICT.value()));
//    }
//
//    @DisplayName("check find endpoint")
//    @Test
//    void find() throws Exception {
//        Board board = prepareGame();
//        Mockito.when(gameService.findShips(Mockito.any(), Mockito.anyBoolean())).thenReturn(List.of(new Ship().type(Ship.TypeEnum.Destroyer)));
//        MvcResult mvcResult = mockMvc.perform(
//                        get("/game/x/ship")
//                                .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().is2xxSuccessful())
//                .andReturn();
//
//        var list = mapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
//        Assertions.assertEquals(1, list.size());
//    }
//
//    @DisplayName("check find endpoint, not found")
//    @Test
//    void findNotFound() throws Exception {
//        Mockito.reset(gameService);
//        Mockito.when(gameService.findShips(Mockito.any(), Mockito.anyBoolean())).thenThrow(new NoGameFoundException());
//
//        mockMvc.perform(
//                get("/game/x/ship")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
//    }
//
//    @DisplayName("check shot endpoint")
//    @Test
//    void shot() throws Exception {
//        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenReturn(ShotResult.HIT);
//        mockMvc.perform(
//                post("/game/x/shot")
//                        .content(mapper.writeValueAsString(
//                                new Position().x(1).y(2)
//                        ))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().is2xxSuccessful());
//    }
//
//    @DisplayName("check shot endpoint, not found")
//    @Test
//    void shotNotFound() throws Exception {
//        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenThrow(new NoGameFoundException());
//
//        mockMvc.perform(
//                post("/game/x/shot")
//                        .content(mapper.writeValueAsString(
//                                new Position().x(1).y(2)
//                        ))
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
//    }
//
//    @DisplayName("check shot endpoint, game over")
//    @Test
//    void gameOVer() throws Exception {
//        Mockito.when(gameService.opponentShot(Mockito.any(), Mockito.any())).thenThrow(new GameOverException());
//
//        mockMvc.perform(
//                post("/game/x/shot")
//                        .content(mapper.writeValueAsString(
//                                new Position().x(1).y(2)
//                        ))
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().is(HttpStatus.GONE.value()));
//    }
//
//    @DisplayName("check get all shots enpoint")
//    @Test
//    void getAllShots() throws Exception {
//        Mockito.when(shotService.getAllShots(Mockito.any())).thenReturn(List.of(new Position().x(1).y(2).hit(Boolean.FALSE), new Position().x(6).y(8).hit(Boolean.TRUE)));
//        mockMvc.perform(
//                get("/game/x/shot")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().is2xxSuccessful());
//    }
//
//    private Board prepareGame() {
//        return Board.builder()
//                .gameId("x")
//                .ships(List.of(
//                        Dockyard.INSTANCE.build(Ship.TypeEnum.Carrier, List.of(
//                                new Position().x(1).y(1).hit(false),
//                                new Position().x(1).y(2).hit(false),
//                                new Position().x(1).y(3).hit(false),
//                                new Position().x(1).y(4).hit(false),
//                                new Position().x(2).y(4).hit(false)
//                        )),
//                        Dockyard.INSTANCE.build(Ship.TypeEnum.Destroyer, List.of(new Position().x(8).y(6).hit(false)))
//                ))
//                .board(new TwoDimensionalBoard(10))
//                .build();
//    }

}