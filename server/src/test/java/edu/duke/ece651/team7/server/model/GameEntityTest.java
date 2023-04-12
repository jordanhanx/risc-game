package edu.duke.ece651.team7.server.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.duke.ece651.team7.shared.*;

@ExtendWith(MockitoExtension.class)
public class GameEntityTest {

    @Mock
    private GameMap gameMap;
    @Mock
    private OrderExecuteVisitor ox;
    @Spy
    private Map<String, Player> playerMap = new HashMap<>();
    @Spy
    private Map<String, RemoteGame.GamePhase> phaseMap = new HashMap<>();
    @Spy
    private Map<String, RemoteClient> clientMap = new HashMap<>();
    // @Spy
    @Mock
    private Set<String> commitSet = new HashSet<>();
    @Mock
    private CountDownLatch commitSignal;

    private static class TestGameEntity extends GameEntity {
        public TestGameEntity() throws RemoteException {
            super("host", 12345, "test", 2, 10);
        }

        @Override
        protected void setCountDownLatch(int num) {
        }
    }

    @InjectMocks
    private TestGameEntity testgame;

    @Test
    public void test_constructor() throws RemoteException {
        GameEntity game = new GameEntity("host", 0, "game1", 2, 10);
        assertEquals("host", game.getHost());
        assertEquals(0, game.getPort());
        assertEquals("game1", game.getName());
        assertEquals(2, game.getCapacity());
        assertEquals(10, game.getInitUnits());
        assertEquals(0, game.getUsers().size());
        assertThrows(IllegalStateException.class, () -> new GameEntity("host", 0, "game1", 1, 10));
        assertThrows(IllegalStateException.class, () -> new GameEntity("host", 0, "game1", 2, 0));
    }

    @Test
    public void test_getters() throws RemoteException {
        assertEquals("host", testgame.getHost());
        assertEquals(12345, testgame.getPort());
        assertEquals("test", testgame.getName());
        assertEquals(2, testgame.getCapacity());
        assertEquals(10, testgame.getInitUnits());
        assertEquals(10, testgame.getGameInitUnits());
        assertEquals(new HashSet<>(), testgame.getUsers());
    }

    @Test
    public void test_getGamePhase() throws RemoteException {
        assertEquals(0, phaseMap.size());
        phaseMap.put("username", RemoteGame.GamePhase.PICK_GROUP);
        assertEquals(RemoteGame.GamePhase.PICK_GROUP, testgame.getGamePhase("username"));
    }

    @Test
    public void test_addUser() throws RemoteException {
        assertDoesNotThrow(() -> testgame.addUser("player1"));
        assertEquals(1, playerMap.size());
        assertEquals(RemoteGame.GamePhase.PICK_GROUP, testgame.getGamePhase("player1"));
        assertThrows(IllegalStateException.class, () -> testgame.addUser("player1"));
        assertDoesNotThrow(() -> testgame.addUser("player2"));
        assertEquals(2, playerMap.size());
        assertEquals(RemoteGame.GamePhase.PICK_GROUP, testgame.getGamePhase("player2"));
        assertThrows(IllegalStateException.class, () -> testgame.addUser("player3"));
    }

    @Test
    public void test_start() throws InterruptedException, RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        RemoteClient cBlue = mock(RemoteClient.class);
        RemoteClient cGreen = mock(RemoteClient.class);
        clientMap.put("Blue", cBlue);
        clientMap.put("Green", cGreen);
        when(pBlue.isLose()).thenReturn(false, false, true);
        when(pGreen.isLose()).thenReturn(false);

        assertDoesNotThrow(() -> testgame.start());
        verify(commitSignal, never()).countDown();
        verify(commitSignal, times(3)).await();
    }

    @Test
    public void test_tryRegisterClient() throws RemoteException {
        playerMap.put("player1", null);
        assertEquals(null, testgame.tryRegisterClient("player1", null));
        assertEquals(1, clientMap.size());
        assertEquals("Username didn't match", testgame.tryRegisterClient("player2", null));
    }

    @Test
    public void test_getGameMap() throws RemoteException {
        assertEquals(gameMap, testgame.getGameMap());
    }

    @Test
    public void test_getSelfStatus() throws RemoteException {
        Player mockplayer1 = mock(Player.class);
        playerMap.put("player1", mockplayer1);
        assertEquals(mockplayer1, testgame.getSelfStatus("player1"));
    }

    @Test
    public void test_tryPickTerritoryGroupByName() throws RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        doNothing().when(gameMap).assignGroup("GroupA", pBlue);
        doThrow(new IllegalArgumentException("GroupA has been Occupied")).when(gameMap).assignGroup("GroupA", pGreen);
        assertEquals(null, testgame.tryPickTerritoryGroupByName("Blue", "GroupA"));
        assertEquals("GroupA has been Occupied", testgame.tryPickTerritoryGroupByName("Green", "GroupA"));
        assertEquals(RemoteGame.GamePhase.PLACE_UNITS, testgame.getGamePhase("Blue"));
        assertEquals(null, testgame.getGamePhase("Green"));
    }

    @Test
    public void test_tryPlaceUnitsOn() throws RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        when(gameMap.getTerritoryByName("404NotFound"))
                .thenThrow(new IllegalArgumentException("Not Found the Territory"));
        when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
        when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
        when(tMordor.getOwner()).thenReturn(pBlue);
        when(tHogwarts.getOwner()).thenReturn(pGreen);
        when(pBlue.getTotalUnits()).thenReturn(0, 0, 5, 10);
        // Test
        assertEquals("Not Found the Territory", testgame.tryPlaceUnitsOn("Blue", "404NotFound", 1));
        assertEquals("units cannot be less than 0", testgame.tryPlaceUnitsOn("Blue", "Mordor", -1));
        assertEquals(null, testgame.tryPlaceUnitsOn("Blue", "Mordor", 5));
        assertEquals(null, testgame.tryPlaceUnitsOn("Blue", "Mordor", 5));
        assertEquals("Too many units", testgame.tryPlaceUnitsOn("Blue", "Mordor", 1));
        assertEquals("Permission denied", testgame.tryPlaceUnitsOn("Blue", "Hogwarts", 5));
    }

    @Test
    public void test_tryMoveOrder() throws RemoteException {
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
        when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
        when(ox.visit(any(MoveOrder.class))).thenReturn(null).thenThrow(new IllegalArgumentException("Invalid Input:"));
        when(commitSet.contains("Blue")).thenReturn(false);
        when(commitSet.contains("Red")).thenReturn(true);
        // Test
        assertEquals(null, testgame.tryMoveOrder("Blue", "Hogwarts", "Mordor", 0, 5));
        assertEquals("Invalid Input:", testgame.tryMoveOrder("Blue", "Hogwarts", "Mordor", 0, 5));
        assertEquals("Please wait for other players to commit",
                testgame.tryMoveOrder("Red", "Hogwarts", "Mordor", 0, 1));
        // Verify
        verify(gameMap, atLeastOnce()).getTerritoryByName("Hogwarts");
        verify(gameMap, atLeastOnce()).getTerritoryByName("Mordor");
        // verify(ox, times(2)).doOneMove(any(MoveOrder.class));
        verify(ox, times(2)).visit(any(MoveOrder.class));
    }

    @Test
    public void test_tryAttackOrder() throws RemoteException {
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
        when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
        when(ox.visit(any(AttackOrder.class))).thenReturn(null)
                .thenThrow(new IllegalArgumentException("Invalid Input:"));
        when(commitSet.contains("Blue")).thenReturn(false);
        when(commitSet.contains("Red")).thenReturn(true);
        // Test
        assertEquals(null, testgame.tryAttackOrder("Blue", "Hogwarts", "Mordor", 0, 5));
        assertEquals("Invalid Input:", testgame.tryAttackOrder("Blue", "Hogwarts", "Mordor", 0, 5));
        assertEquals("Please wait for other players to commit",
                testgame.tryAttackOrder("Red", "Hogwarts", "Mordor", 0, 1));
        // Verify
        verify(gameMap, atLeastOnce()).getTerritoryByName("Hogwarts");
        verify(gameMap, atLeastOnce()).getTerritoryByName("Mordor");
        verify(ox, times(2)).visit(any(AttackOrder.class));
    }

    @Test
    public void test_tryUpgradeOrder() throws RemoteException {

        Territory tMordor = mock(Territory.class);
        when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
        when(ox.visit(any(UpgradeOrder.class))).thenReturn(null)
                .thenThrow(new IllegalArgumentException("Invalid Input:"));
        when(commitSet.contains("Blue")).thenReturn(false);
        when(commitSet.contains("Red")).thenReturn(true);
        assertEquals(null, testgame.tryUpgradeOrder("Blue", "Mordor", 0, 1, 5));
        assertEquals("Invalid Input:", testgame.tryUpgradeOrder("Blue", "Mordor", 0, 1, 5));
        assertEquals("Please wait for other players to commit",
                testgame.tryUpgradeOrder("Red", "Mordor", 0, 1, 5));
        verify(ox, times(2)).visit(any(UpgradeOrder.class));
    }

    @Test
    public void test_tryResearchOrder() throws RemoteException {
        when(ox.visit(any(ResearchOrder.class))).thenReturn(null)
                .thenThrow(new IllegalArgumentException("Invalid Input:"));
        when(commitSet.contains("Blue")).thenReturn(false);
        when(commitSet.contains("Red")).thenReturn(true);
        assertEquals(null, testgame.tryResearchOrder("Blue"));
        assertEquals("Invalid Input:", testgame.tryResearchOrder("Blue"));
        assertEquals("Please wait for other players to commit",
                testgame.tryResearchOrder("Red"));
        verify(ox, times(2)).visit(any(ResearchOrder.class));
    }

    @Test
    public void test_doCommitOrder() throws RemoteException, InterruptedException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        when(pBlue.isLose()).thenReturn(false);
        when(pGreen.isLose()).thenReturn(true);
        when(commitSet.contains("Blue")).thenReturn(false, true);
        // Test
        assertEquals(null, testgame.doCommitOrder("Blue"));
        assertEquals(RemoteGame.GamePhase.PLAY_GAME, testgame.getGamePhase("Blue"));
        assertEquals("Please wait for other players to commit", testgame.doCommitOrder("Blue"));
        assertEquals("Lost user cannot commit", testgame.doCommitOrder("Green"));
        assertEquals(null, testgame.getGamePhase("Green"));

        // Verify
        verify(commitSignal, times(1)).countDown();
        verify(commitSignal, never()).await();
        verify(commitSet, times(2)).contains("Blue");
        verify(commitSet, never()).contains("Green");
        verify(commitSet, times(1)).add("Blue");
    }

    @Test
    public void test_countNotLostPlayers() {
        assertEquals(0, testgame.countNotLostPlayers());
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        when(pBlue.isLose()).thenReturn(false);
        when(pGreen.isLose()).thenReturn(false, true);
        assertEquals(2, testgame.countNotLostPlayers());
        assertEquals(1, testgame.countNotLostPlayers());
    }

    @Test
    public void test_findWinner() {
        assertEquals(null, testgame.findWinner());
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        when(pBlue.isLose()).thenReturn(false);
        when(pGreen.isLose()).thenReturn(false, true);
        assertEquals(null, testgame.findWinner());
        assertEquals("Blue", testgame.findWinner());
    }

    @Test
    public void test_notifyUpdatesToClients() throws RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        RemoteClient cBlue = mock(RemoteClient.class);
        RemoteClient cGreen = mock(RemoteClient.class);
        clientMap.put("Blue", cBlue);
        clientMap.put("Green", cGreen);
        doThrow(RemoteException.class).when(cGreen).updateGameMap(gameMap);
        // Test
        assertDoesNotThrow(() -> testgame.notifyUpdatesToClients());
        // Verify
        verify(cBlue, times(1)).updateGameMap(gameMap);
        verify(cGreen, times(1)).updateGameMap(gameMap);
        verify(cBlue, times(1)).updatePlayer(pBlue);
        verify(cGreen, never()).updatePlayer(pGreen);
        verify(cBlue, times(1)).showPopupWindow(anyString());
        verify(cGreen, never()).showPopupWindow(anyString());
    }

    @Test
    public void test_notifyWinnerToClients() throws RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        RemoteClient cBlue = mock(RemoteClient.class);
        RemoteClient cGreen = mock(RemoteClient.class);
        clientMap.put("Blue", cBlue);
        clientMap.put("Green", cGreen);
        doThrow(RemoteException.class).when(cGreen).showPopupWindow(anyString());
        // Test
        assertDoesNotThrow(() -> testgame.notifyWinnerToClients());
        // Verify
        verify(cBlue, times(1)).showPopupWindow(anyString());
        verify(cGreen, times(1)).showPopupWindow(anyString());
    }
}
