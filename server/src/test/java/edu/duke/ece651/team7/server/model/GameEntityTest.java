// package edu.duke.ece651.team7.server.model;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import java.rmi.RemoteException;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.Map;
// import java.util.concurrent.CountDownLatch;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Spy;
// import org.mockito.junit.jupiter.MockitoExtension;

// import edu.duke.ece651.team7.shared.*;
// import edu.duke.ece651.team7.shared.RemoteGame.GamePhase;

// @ExtendWith(MockitoExtension.class)
// public class GameEntityTest {

//     @Mock
//     private GameMap gameMap;
//     @Mock
//     private OrderExecuter ox;
//     @Spy
//     private Map<String, Player> playerMap = new HashMap<>();
//     @Spy
//     private Map<String, RemoteClient> clientMap = new HashMap<>();
//     @Spy
//     private Map<String, Boolean> commitMap = new HashMap<>();
//     @Mock
//     private CountDownLatch commitSignal;

//     private static class TestGameEntity extends GameEntity {
//         public TestGameEntity() throws RemoteException {
//             super("host", 12345, "test", 2, 10);
//         }

//         @Override
//         protected void setCountDownLatch(int num) {
//         }
//     }

//     @InjectMocks
//     private TestGameEntity testgame;

//     @Test
//     public void test_constructor() throws RemoteException {
//         GameEntity game = new GameEntity("host", 0, "game1", 2, 10);
//         assertEquals("host", game.getHost());
//         assertEquals(0, game.getPort());
//         assertEquals("game1", game.getName());
//         assertEquals(2, game.getCapacity());
//         assertEquals(10, game.getInitUnits());
//         assertEquals(0, game.getUsers().size());
//         assertThrows(IllegalStateException.class, () -> new GameEntity("host", 0, "game1", 1, 10));
//         assertThrows(IllegalStateException.class, () -> new GameEntity("host", 0, "game1", 2, 0));
//     }

//     @Test
//     public void test_getters() throws RemoteException {
//         assertEquals("host", testgame.getHost());
//         assertEquals(12345, testgame.getPort());
//         assertEquals("test", testgame.getName());
//         assertEquals(2, testgame.getCapacity());
//         assertEquals(10, testgame.getInitUnits());
//         assertEquals(10, testgame.getGameInitUnits());
//         assertEquals(new HashSet<>(), testgame.getUsers());
//         assertEquals(GamePhase.PICK_GROUP, testgame.getGamePhase());
//         assertDoesNotThrow(() -> testgame.setGamePhase(GamePhase.PLAY_GAME));
//         assertEquals(GamePhase.PLAY_GAME, testgame.getGamePhase());
//     }

//     @Test
//     public void test_addUser() {
//         assertDoesNotThrow(() -> testgame.addUser("player1"));
//         assertEquals(1, playerMap.size());
//         assertEquals(1, commitMap.size());
//         assertEquals(false, commitMap.get("player1"));
//         assertThrows(IllegalStateException.class, () -> testgame.addUser("player1"));
//         assertDoesNotThrow(() -> testgame.addUser("player2"));
//         assertEquals(2, playerMap.size());
//         assertEquals(2, commitMap.size());
//         assertEquals(false, commitMap.get("player2"));
//         assertThrows(IllegalStateException.class, () -> testgame.addUser("player3"));
//     }

//     @Test
//     public void test_resetCommitMap() {
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         commitMap.put("Blue", true);
//         commitMap.put("Green", true);
//         when(pBlue.isLose()).thenReturn(false);
//         when(pGreen.isLose()).thenReturn(true);
//         assertDoesNotThrow(() -> testgame.resetCommitMap(false));
//         assertEquals(2, commitMap.size());
//         assertFalse(commitMap.get("Blue"));
//         assertFalse(commitMap.get("Green"));
//         assertDoesNotThrow(() -> testgame.resetCommitMap(true));
//         assertEquals(1, commitMap.size());
//         assertFalse(commitMap.get("Blue"));
//         assertNull(commitMap.get("Green"));
//     }

//     @Test
//     public void test_start() throws InterruptedException, RemoteException {
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         RemoteClient cBlue = mock(RemoteClient.class);
//         RemoteClient cGreen = mock(RemoteClient.class);
//         clientMap.put("Blue", cBlue);
//         clientMap.put("Green", cGreen);
//         when(pBlue.isLose()).thenReturn(false, true);
//         when(pGreen.isLose()).thenReturn(false);

//         assertDoesNotThrow(() -> testgame.start());
//         verify(commitSignal, never()).countDown();
//         verify(commitSignal, times(4)).await();
//     }

//     @Test
//     public void test_tryRegisterClient() throws RemoteException {
//         playerMap.put("player1", null);
//         assertEquals(null, testgame.tryRegisterClient("player1", null));
//         assertEquals(1, clientMap.size());
//         assertEquals("Username didn't match", testgame.tryRegisterClient("player2", null));
//     }

//     @Test
//     public void test_getGameMap() throws RemoteException {
//         assertEquals(gameMap, testgame.getGameMap());
//     }

//     @Test
//     public void test_getSelfStatus() throws RemoteException {
//         Player mockplayer1 = mock(Player.class);
//         playerMap.put("player1", mockplayer1);
//         assertEquals(mockplayer1, testgame.getSelfStatus("player1"));
//     }

//     @Test
//     public void test_tryPickTerritoryGroupByName() throws RemoteException {
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         commitMap.put("Blue", false);
//         commitMap.put("Green", false);
//         commitMap.put("Red", true);
//         doNothing().when(gameMap).assignGroup("GroupA", pBlue);
//         doThrow(new IllegalArgumentException("GroupA has been Occupied")).when(gameMap).assignGroup("GroupA", pGreen);
//         assertEquals(null, testgame.tryPickTerritoryGroupByName("Blue", "GroupA"));
//         assertEquals("GroupA has been Occupied", testgame.tryPickTerritoryGroupByName("Green", "GroupA"));
//         assertEquals("Please wait for other players to commit", testgame.tryPickTerritoryGroupByName("Red", "GroupB"));
//     }

//     @Test
//     public void test_tryPlaceUnitsOn() throws RemoteException {
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         commitMap.put("Blue", false);
//         commitMap.put("Green", false);
//         commitMap.put("Red", true);
//         Territory tMordor = mock(Territory.class);
//         Territory tHogwarts = mock(Territory.class);
//         when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
//         when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
//         when(tMordor.getOwner()).thenReturn(pBlue);
//         when(tHogwarts.getOwner()).thenReturn(pGreen);
//         when(pBlue.getTotalUnits()).thenReturn(0, 0, 5, 10);
//         doThrow(new IllegalArgumentException("units cannot be less than 0")).when(tMordor).increaseUnits(-1);
//         // Test
//         assertEquals("units cannot be less than 0", testgame.tryPlaceUnitsOn("Blue", "Mordor", -1));
//         assertEquals(null, testgame.tryPlaceUnitsOn("Blue", "Mordor", 5));
//         assertEquals(null, testgame.tryPlaceUnitsOn("Blue", "Mordor", 5));
//         assertEquals("Too many units", testgame.tryPlaceUnitsOn("Blue", "Mordor", 1));
//         assertEquals("Permission denied", testgame.tryPlaceUnitsOn("Blue", "Hogwarts", 5));
//         assertEquals("Please wait for other players to commit", testgame.tryPlaceUnitsOn("Red", "Hogwarts", 1));
//     }

//     @Test
//     public void test_tryMoveOrder() throws RemoteException {
//         commitMap.put("Blue", false);
//         commitMap.put("Red", true);
//         Territory tMordor = mock(Territory.class);
//         Territory tHogwarts = mock(Territory.class);
//         when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
//         when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
//         doNothing().doThrow(new IllegalArgumentException("Invalid Input:")).when(ox)
//                 .doOneMove(any(MoveOrder.class));
//         // Test
//         assertEquals(null, testgame.tryMoveOrder("Blue", "Hogwarts", "Mordor", 5));
//         assertEquals("Invalid Input:", testgame.tryMoveOrder("Blue", "Hogwarts", "Mordor", 5));
//         assertEquals("Please wait for other players to commit", testgame.tryMoveOrder("Red", "Hogwarts", "Mordor", 1));
//         // Verify
//         verify(gameMap, atLeastOnce()).getTerritoryByName("Hogwarts");
//         verify(gameMap, atLeastOnce()).getTerritoryByName("Mordor");
//         verify(ox, times(2)).doOneMove(any(MoveOrder.class));
//     }

//     @Test
//     public void test_tryAttackOrder() throws RemoteException {
//         commitMap.put("Blue", false);
//         commitMap.put("Red", true);
//         Territory tMordor = mock(Territory.class);
//         Territory tHogwarts = mock(Territory.class);
//         when(gameMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
//         when(gameMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
//         doNothing().doThrow(new IllegalArgumentException("Invalid Input:")).when(ox)
//                 .pushCombat(any(AttackOrder.class));
//         // Test
//         assertEquals(null, testgame.tryAttackOrder("Blue", "Hogwarts", "Mordor", 5));
//         assertEquals("Invalid Input:", testgame.tryAttackOrder("Blue", "Hogwarts", "Mordor", 5));
//         assertEquals("Please wait for other players to commit",
//                 testgame.tryAttackOrder("Red", "Hogwarts", "Mordor", 1));
//         // Verify
//         verify(gameMap, atLeastOnce()).getTerritoryByName("Hogwarts");
//         verify(gameMap, atLeastOnce()).getTerritoryByName("Mordor");
//         verify(ox, times(2)).pushCombat(any(AttackOrder.class));
//     }

//     @Test
//     public void test_doCommitOrder() throws RemoteException, InterruptedException {
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         commitMap.put("Blue", false);
//         commitMap.put("Green", false);
//         when(pBlue.isLose()).thenReturn(false);
//         when(pGreen.isLose()).thenReturn(true);
//         // Test
//         assertEquals(null, testgame.doCommitOrder("Blue"));
//         assertEquals("Please wait for other players to commit", testgame.doCommitOrder("Blue"));
//         assertEquals("Lost user cannot commit", testgame.doCommitOrder("Green"));

//         // Verify
//         verify(commitSignal, times(1)).countDown();
//         verify(commitSignal, never()).await();
//     }

//     @Test
//     public void test_isGameOver() throws RemoteException {
//         assertFalse(testgame.isGameOver());
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         when(pBlue.isLose()).thenReturn(true);
//         when(pGreen.isLose()).thenReturn(false);
//         assertTrue(testgame.isGameOver());
//     }

//     @Test
//     public void test_findWinner() {
//         assertEquals(null, testgame.findWinner());
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         when(pBlue.isLose()).thenReturn(false);
//         when(pGreen.isLose()).thenReturn(false, true);
//         assertEquals(null, testgame.findWinner());
//         assertEquals("Blue", testgame.findWinner());
//     }

//     @Test
//     public void test_notifyGameMapToClients() throws RemoteException {
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         RemoteClient cBlue = mock(RemoteClient.class);
//         RemoteClient cGreen = mock(RemoteClient.class);
//         clientMap.put("Blue", cBlue);
//         clientMap.put("Green", cGreen);
//         doThrow(RemoteException.class).when(cGreen).doDisplay(gameMap);
//         // Test
//         assertDoesNotThrow(() -> testgame.notifyGameMapToClients());
//         // Verify
//         verify(cBlue, times(1)).doDisplay(gameMap);
//         verify(cGreen, times(1)).doDisplay(gameMap);
//     }

//     @Test
//     public void test_notifyWinnerToClients() throws RemoteException {
//         Player pBlue = mock(Player.class);
//         Player pGreen = mock(Player.class);
//         playerMap.put("Blue", pBlue);
//         playerMap.put("Green", pGreen);
//         RemoteClient cBlue = mock(RemoteClient.class);
//         RemoteClient cGreen = mock(RemoteClient.class);
//         clientMap.put("Blue", cBlue);
//         clientMap.put("Green", cGreen);
//         doThrow(RemoteException.class).when(cGreen).doDisplay(anyString());
//         // Test
//         assertDoesNotThrow(() -> testgame.notifyWinnerToClients());
//         // Verify
//         verify(cBlue, times(1)).doDisplay(anyString());
//         verify(cGreen, times(1)).doDisplay(anyString());
//     }

//     @Test
//     public void test_closeAllClients() throws RemoteException {
//         RemoteClient cBlue = mock(RemoteClient.class);
//         RemoteClient cGreen = mock(RemoteClient.class);
//         clientMap.put("Blue", cBlue);
//         clientMap.put("Green", cGreen);
//         doThrow(RemoteException.class).when(cGreen).close();
//         assertDoesNotThrow(() -> testgame.closeAllClients());
//         verify(cBlue, times(1)).close();
//         verify(cGreen, times(1)).close();
//     }
// }
