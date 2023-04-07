package edu.duke.ece651.team7.server.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import edu.duke.ece651.team7.server.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testRequestIndex() throws Exception {
        ResultActions response = mockMvc.perform(get("/"));
        response.andExpectAll(status().isOk(), content().string("Welcome this endpoint is not secure"));
    }

    @Test
    void testRequestAll() throws Exception {
        ResultActions response = mockMvc.perform(get("/admin"));
        response.andExpectAll(status().isOk(), content().string("Admin page"));
    }

    @Test
    void testRequestCreateUser() throws Exception {
        doNothing().doThrow(IllegalStateException.class).when(userService).createUser("player", "pswd");
        ResultActions response1 = mockMvc
                .perform(post("/api/signup").param("username", "player").param("password", "pswd"));
        response1.andExpectAll(status().isCreated());
        ResultActions response2 = mockMvc
                .perform(post("/api/signup").param("username", "player").param("password", "pswd"));
        response2.andExpectAll(status().isInternalServerError());
    }

}
