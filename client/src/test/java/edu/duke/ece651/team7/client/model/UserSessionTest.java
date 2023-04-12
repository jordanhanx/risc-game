package edu.duke.ece651.team7.client.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserSessionTest {

    @Test
    public void test_UserSessionTest() {
        assertEquals(UserSession.getInstance(), UserSession.getInstance());
        assertDoesNotThrow(() -> UserSession.getInstance().setHost("host"));
        assertDoesNotThrow(() -> UserSession.getInstance().setPort("0000"));
        assertDoesNotThrow(() -> UserSession.getInstance().setUsername("username"));
        assertDoesNotThrow(() -> UserSession.getInstance().setSession("session"));
        assertEquals("host", UserSession.getInstance().getHost());
        assertEquals("0000", UserSession.getInstance().getPort());
        assertEquals("username", UserSession.getInstance().getUsername());
        assertEquals("session", UserSession.getInstance().getSession());
    }
}
