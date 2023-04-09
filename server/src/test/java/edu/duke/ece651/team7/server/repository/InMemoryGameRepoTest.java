package edu.duke.ece651.team7.server.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.duke.ece651.team7.server.model.GameEntity;

@ExtendWith(MockitoExtension.class)
public class InMemoryGameRepoTest {

    @Mock
    private Registry registry;
    @Spy
    private HashMap<String, GameEntity> allGames = new HashMap<>();

    @InjectMocks
    private InMemoryGameRepo inMemoryGameRepo;

    @Test
    public void testGetAllGames() throws AccessException, RemoteException {
        // doNothing().doThrow(AccessException.class).when(registry).rebind(anyString(),
        // any(Remote.class));
        assertEquals(0, inMemoryGameRepo.getAllGames().size());
        allGames.put("game1", new GameEntity("host", 0, "game1", 2, 10));
        assertEquals(1, inMemoryGameRepo.getAllGames().size());
        allGames.put("game2", new GameEntity("host", 0, "game2", 2, 10));
        assertEquals(2, inMemoryGameRepo.getAllGames().size());
    }

    @Test
    public void testGetGamesByUser() throws RemoteException {
        assertEquals(0, inMemoryGameRepo.getGamesByUser("player1").size());
        assertEquals(0, inMemoryGameRepo.getGamesByUser("player2").size());
        GameEntity game1 = new GameEntity("host", 0, "game1", 2, 10);
        game1.addUser("player1");
        allGames.put("game1", game1);
        assertEquals(1, inMemoryGameRepo.getGamesByUser("player1").size());
        assertEquals(0, inMemoryGameRepo.getGamesByUser("player2").size());
    }

    @Test
    public void testCreateNewGame() throws InterruptedException {
        assertEquals(0, inMemoryGameRepo.getAllGames().size());
        assertDoesNotThrow(() -> inMemoryGameRepo.createNewGame("host", 0, "player1", 2, 10));
        assertEquals(1, inMemoryGameRepo.getAllGames().size());
    }

    @Test
    public void testJoinGame() throws RemoteException {
        allGames.put("game1", new GameEntity("host", 0, "game1", 2, 10));
        assertDoesNotThrow(() -> inMemoryGameRepo.joinGame("player1", "game1"));
        assertThrows(IllegalStateException.class, () -> inMemoryGameRepo.joinGame("player1", "game1"));
        assertThrows(IllegalStateException.class, () -> inMemoryGameRepo.joinGame("player1", "game2"));
    }
}
