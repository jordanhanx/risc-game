package edu.duke.ece651.team7.server.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.duke.ece651.team7.shared.*;

@ExtendWith(MockitoExtension.class)
public class GameEntityTest {

    @Spy
    private Map<String, Player> playerMap = new HashMap<>();
    @Spy
    private Map<String, RemoteClient> clientMap = new HashMap<>();
    @Mock
    private GameMap gameMap;
    @Mock
    private OrderExecuter ox;
    @Mock
    private CountDownLatch commitSignal;
    @Mock
    private CyclicBarrier returnSignal;

    private static class TestGameEntity extends GameEntity {
        public TestGameEntity() throws RemoteException {
            super("host", 12345, "test", 2, 10);
        }

        @Override
        protected void setupCountDownLatches(int num) {
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
    public void test_addUser() {
        assertDoesNotThrow(() -> testgame.addUser("player1"));
        assertEquals(1, playerMap.size());
        assertThrows(IllegalStateException.class, () -> testgame.addUser("player1"));
        assertDoesNotThrow(() -> testgame.addUser("player2"));
        assertEquals(2, playerMap.size());
        assertThrows(IllegalStateException.class, () -> testgame.addUser("player3"));
    }

    @Test
    public void test_start() throws InterruptedException, BrokenBarrierException, RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        RemoteClient cBlue = mock(RemoteClient.class);
        RemoteClient cGreen = mock(RemoteClient.class);
        clientMap.put("Blue", cBlue);
        clientMap.put("Green", cGreen);
        when(pBlue.isLose()).thenReturn(false, true);
        when(pGreen.isLose()).thenReturn(false);

        assertDoesNotThrow(() -> testgame.start());
        verify(commitSignal, never()).countDown();
        verify(commitSignal, times(4)).await();
        verify(returnSignal, times(4)).await();
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
    }

    @Test
    public void test_tryPlaceUnitsOn() throws RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
        when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
        when(tMordor.getOwner()).thenReturn(pBlue);
        when(tHogwarts.getOwner()).thenReturn(pGreen);
        when(pBlue.getTotalUnits()).thenReturn(0, 0, 5, 10);
        doThrow(new IllegalArgumentException("units cannot be less than 0")).when(tMordor).increaseUnits(-1);
        // Test
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
        doNothing().doThrow(new IllegalArgumentException("Invalid Input:")).when(ox)
                .doOneMove(any(MoveOrder.class));
        // Test
        assertEquals(null, testgame.tryMoveOrder("username", "Hogwarts", "Mordor", 5));
        assertEquals("Invalid Input:", testgame.tryMoveOrder("username", "Hogwarts", "Mordor", 5));
        // Verify
        verify(gameMap, atLeastOnce()).getTerritoryByName("Hogwarts");
        verify(gameMap, atLeastOnce()).getTerritoryByName("Mordor");
        verify(ox, times(2)).doOneMove(any(MoveOrder.class));
    }

    @Test
    public void test_tryAttackOrder() throws RemoteException {
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
        when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
        doNothing().doThrow(new IllegalArgumentException("Invalid Input:")).when(ox)
                .pushCombat(any(AttackOrder.class));
        // Test
        assertEquals(null, testgame.tryAttackOrder("username", "Hogwarts", "Mordor", 5));
        assertEquals("Invalid Input:", testgame.tryAttackOrder("username", "Hogwarts", "Mordor", 5));
        // Verify
        verify(gameMap, atLeastOnce()).getTerritoryByName("Hogwarts");
        verify(gameMap, atLeastOnce()).getTerritoryByName("Mordor");
        verify(ox, times(2)).pushCombat(any(AttackOrder.class));
    }

    @Test
    public void test_doCommitOrder() throws RemoteException, InterruptedException, BrokenBarrierException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        when(pBlue.isLose()).thenReturn(false);
        when(pBlue.isLose()).thenReturn(true);
        // Test
        assertDoesNotThrow(() -> testgame.doCommitOrder("Blue"));
        assertDoesNotThrow(() -> testgame.doCommitOrder("Green"));
        // Verify
        verify(commitSignal, times(1)).countDown();
        verify(commitSignal, never()).await();
        verify(returnSignal, times(1)).await();
    }

    @Test
    public void test_isGameOver() throws RemoteException {
        assertFalse(testgame.isGameOver());
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        when(pBlue.isLose()).thenReturn(true);
        when(pGreen.isLose()).thenReturn(false);
        assertDoesNotThrow(() -> testgame.updateLostCounter());
        assertTrue(testgame.isGameOver());
    }

    @Test
    public void test_notifyGameMapToLostClients() throws RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        Player pRed = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        playerMap.put("Red", pRed);
        RemoteClient cBlue = mock(RemoteClient.class);
        RemoteClient cGreen = mock(RemoteClient.class);
        RemoteClient cRed = mock(RemoteClient.class);
        clientMap.put("Blue", cBlue);
        clientMap.put("Green", cGreen);
        clientMap.put("Red", cRed);
        when(pBlue.isLose()).thenReturn(true);
        when(pGreen.isLose()).thenReturn(true);
        when(pRed.isLose()).thenReturn(false);
        doThrow(RemoteException.class).when(cGreen).doDisplay(gameMap);
        // Test
        assertDoesNotThrow(() -> testgame.notifyGameMapToLostClients());
        // Verify
        verify(cBlue, times(1)).doDisplay(gameMap);
        verify(cGreen, times(1)).doDisplay(gameMap);
        verify(cRed, never()).doDisplay(gameMap);
    }

    @Test
    public void test_notifyWinnerToAllClients() throws RemoteException {
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        playerMap.put("Blue", pBlue);
        playerMap.put("Green", pGreen);
        RemoteClient cBlue = mock(RemoteClient.class);
        RemoteClient cGreen = mock(RemoteClient.class);
        clientMap.put("Blue", cBlue);
        clientMap.put("Green", cGreen);
        when(pBlue.isLose()).thenReturn(true);
        when(pGreen.isLose()).thenReturn(false);
        doThrow(RemoteException.class).when(cGreen).doDisplay(anyString());
        // Test
        assertDoesNotThrow(() -> testgame.notifyWinnerToAllClients());
        // Verify
        verify(cBlue, times(1)).doDisplay(anyString());
        verify(cGreen, times(1)).doDisplay(anyString());
    }

    @Test
    public void test_closeAllClients() throws RemoteException {
        RemoteClient cBlue = mock(RemoteClient.class);
        RemoteClient cGreen = mock(RemoteClient.class);
        clientMap.put("Blue", cBlue);
        clientMap.put("Green", cGreen);
        doThrow(RemoteException.class).when(cGreen).close();
        assertDoesNotThrow(() -> testgame.closeAllClients());
        verify(cBlue, times(1)).close();
        verify(cGreen, times(1)).close();
    }
}
