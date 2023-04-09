package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class GameDtoTest {

    @Test
    public void test_default_constructor() {
        GameDto game = new GameDto();
        assertEquals(null, game.getHost());
        assertEquals(0, game.getPort());
        assertEquals(null, game.getName());
        assertEquals(0, game.getCapacity());
        assertEquals(0, game.getInitUnits());
        assertEquals(new HashSet<>(), game.getInGameUsers());
    }

    @Test
    public void test_constructor() {
        HashSet<String> inGameUsers = new HashSet<>();
        GameDto game = new GameDto("host", 0, "game1", 2, 10, inGameUsers);
        assertEquals("host", game.getHost());
        assertEquals(0, game.getPort());
        assertEquals("game1", game.getName());
        assertEquals(2, game.getCapacity());
        assertEquals(10, game.getInitUnits());
        assertEquals(0, game.getInGameUsers().size());
        inGameUsers.add("player1");
        assertEquals(1, game.getInGameUsers().size());
    }

    @Test
    public void test_getter_setter() {
        GameDto game = new GameDto();
        assertDoesNotThrow(() -> game.setHost("host"));
        assertEquals("host", game.getHost());
        assertDoesNotThrow(() -> game.setPort(12345));
        assertEquals(12345, game.getPort());
        assertDoesNotThrow(() -> game.setName("username"));
        assertEquals("username", game.getName());
        assertDoesNotThrow(() -> game.setCapacity(3));
        assertEquals(3, game.getCapacity());
        assertDoesNotThrow(() -> game.setInitUnits(20));
        assertEquals(20, game.getInitUnits());
        HashSet<String> inGameUsers = new HashSet<>();
        inGameUsers.add("player1");
        assertDoesNotThrow(() -> game.setInGameUsers(inGameUsers));
        assertEquals(inGameUsers, game.getInGameUsers());
    }

    @Test
    public void test_toString() {
        HashSet<String> inGameUsers = new HashSet<>();
        inGameUsers.add("player1");
        inGameUsers.add("player2");
        GameDto game = new GameDto("host", 0, "game1", 2, 10, inGameUsers);
        StringBuilder outputs = new StringBuilder();
        outputs.append(
                "host: host, port: 0, name: game1, capacity: 2, initUnits: 10, inGameUsers: [ player1, player2, ]");
        assertEquals(outputs.toString(), game.toString());
    }
}
