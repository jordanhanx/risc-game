package edu.duke.ece651.team7.client.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserSessionTest {

    @Test
    public void test_UserSessionTest() {
        assertEquals(UserSession.getInstance(), UserSession.getInstance());
        assertEquals(null, UserSession.getInstance().getUsername());
        assertEquals(null, UserSession.getInstance().getSession());
        assertDoesNotThrow(() -> UserSession.getInstance().setUsername("username"));
        assertDoesNotThrow(() -> UserSession.getInstance().setSession("session"));
        assertEquals("username", UserSession.getInstance().getUsername());
        assertEquals("session", UserSession.getInstance().getSession());
    }
}
