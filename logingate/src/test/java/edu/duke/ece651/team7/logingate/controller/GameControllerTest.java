package edu.duke.ece651.team7.logingate.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.duke.ece651.team7.logingate.service.GameService;
import edu.duke.ece651.team7.logingate.dto.GameDto;

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

        ResultActions response = mockMvc.perform(get("/game/all"));

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
    void testRequestGamesByUser() throws Exception {

    }

    @Test
    void testRequestCreateNewGame() throws Exception {

    }

    @Test
    void testRequestJoinGame() throws Exception {

    }
}
