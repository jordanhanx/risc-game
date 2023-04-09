package edu.duke.ece651.team7.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.duke.ece651.team7.server.model.GameEntity;
import edu.duke.ece651.team7.server.repository.InMemoryGameRepo;
import edu.duke.ece651.team7.shared.GameDto;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private InMemoryGameRepo inMemoryGameRepo;

    @InjectMocks
    private GameService gameService;

    @Test
    void testFindAllGames() {
        GameEntity game = mock(GameEntity.class);
        List<GameEntity> allGames = new ArrayList<>();
        when(inMemoryGameRepo.getAllGames()).thenReturn(allGames);
        assertEquals(0, gameService.findAllGames().size());
        allGames.add(game);
        assertEquals(1, gameService.findAllGames().size());
        assertEquals(GameDto.class, gameService.findAllGames().get(0).getClass());
    }

    @Test
    void testFindGamesByUser() {
        GameEntity game = mock(GameEntity.class);
        List<GameEntity> allGames = new ArrayList<>();
        when(inMemoryGameRepo.getGamesByUser("player1")).thenReturn(allGames);
        assertEquals(0, gameService.findGamesByUser("player1").size());
        allGames.add(game);
        assertEquals(1, gameService.findGamesByUser("player1").size());
        assertEquals(0, gameService.findGamesByUser("player2").size());
        assertEquals(GameDto.class, gameService.findGamesByUser("player1").get(0).getClass());
    }

    @Test
    void testCreateNewGame() {
        assertDoesNotThrow(() -> gameService.createNewGame("game1", 2, 10));
    }

    @Test
    void testJoinGame() {
        assertDoesNotThrow(() -> gameService.joinGame("player2", "game1"));
    }
}
