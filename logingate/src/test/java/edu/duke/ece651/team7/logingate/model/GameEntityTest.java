package edu.duke.ece651.team7.logingate.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameEntityTest {

    @Test
    public void testGameEntity() {
        GameEntity game = new GameEntity("host", 0, "game1", 2, 10, () -> {
        });
        assertEquals("host", game.getHost());
        assertEquals(0, game.getPort());
        assertEquals("game1", game.getName());
        assertEquals(2, game.getCapacity());
        assertEquals(10, game.getInitUnits());
        assertEquals(0, game.getInGameUsers().size());
        assertDoesNotThrow(() -> game.addUser("player1"));
        assertEquals(1, game.getInGameUsers().size());
        assertThrows(IllegalStateException.class, () -> game.addUser("player1"));
        assertDoesNotThrow(() -> game.addUser("player2"));
        assertThrows(IllegalStateException.class, () -> game.addUser("player3"));
    }
}
