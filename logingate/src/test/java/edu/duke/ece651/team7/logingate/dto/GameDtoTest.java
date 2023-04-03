package edu.duke.ece651.team7.logingate.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameDtoTest {

    @Test
    public void testGameEntity() {
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
}
