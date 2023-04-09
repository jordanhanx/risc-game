package edu.duke.ece651.team7.server.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import edu.duke.ece651.team7.server.service.GameService;
import edu.duke.ece651.team7.shared.GameDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void testRequestAllGames() throws Exception {
        List<GameDto> games = new ArrayList<>();
        Set<String> inGamePlayers = new HashSet<String>();
        inGamePlayers.add("player1");
        games.add(new GameDto("host", 8082, "game1", 2, 10, inGamePlayers));
        when(gameService.findAllGames()).thenReturn(games);

        ResultActions response = mockMvc.perform(get("/api/riscgame/all"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].host", Matchers.is("host")))
                .andExpect(jsonPath("$[0].port", Matchers.is(8082)))
                .andExpect(jsonPath("$[0].name", Matchers.is("game1")))
                .andExpect(jsonPath("$[0].capacity", Matchers.is(2)))
                .andExpect(jsonPath("$[0].initUnits", Matchers.is(10)))
                .andExpect(jsonPath("$[0].inGameUsers[0]", Matchers.is("player1")));
    }

    @Test
    @WithMockUser(username = "player1", password = "pswd", roles = "USER")
    void testRequestGamesByUser() throws Exception {
        List<GameDto> games = new ArrayList<>();
        Set<String> inGamePlayers = new HashSet<String>();
        inGamePlayers.add("player1");
        games.add(new GameDto("host", 8082, "game1", 2, 10, inGamePlayers));
        when(gameService.findGamesByUser("player1")).thenReturn(games);

        ResultActions response = mockMvc.perform(get("/api/riscgame/my"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].host", Matchers.is("host")))
                .andExpect(jsonPath("$[0].port", Matchers.is(8082)))
                .andExpect(jsonPath("$[0].name", Matchers.is("game1")))
                .andExpect(jsonPath("$[0].capacity", Matchers.is(2)))
                .andExpect(jsonPath("$[0].initUnits", Matchers.is(10)))
                .andExpect(jsonPath("$[0].inGameUsers[0]", Matchers.is("player1")));
    }

    @Test
    @WithMockUser(username = "player1", password = "pswd", roles = "USER")
    void testRequestCreateNewGame() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/riscgame/new")
                .param("capacity", "2")
                .param("initUnits", "10"));
        response.andExpectAll(status().isCreated(), content().string("User[player1] created a new game"));
    }

    @Test
    @WithMockUser(username = "player1", password = "pswd", roles = "USER")
    void testRequestJoinGame() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/riscgame/join")
                .param("gamename", "riscgame2"));
        response.andExpectAll(status().isAccepted(), content().string("User[player1] joined game[riscgame2]"));
    }
}
