package edu.duke.ece651.team7.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.duke.ece651.team7.shared.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.Test;

public class ServerTest {
  public static Server createServer(OutputStream bytes, int numPlayers, int initUnits, GameMap mockMap,
      OrderExecuter mockOX) throws RemoteException {
    PrintStream output = new PrintStream(bytes, true);
    Server server = new Server(output, 0, numPlayers, initUnits) {
      @Override
      protected void initGameMap() {
        this.map = mockMap;
        this.ox = mockOX;
        out.println("GameMap initialized");
      }

      @Override
      protected void bindGameOnPort(int port) throws RemoteException {
        // LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
        out.println("RiscGameServer is ready to accept connections");
      }
    };
    return server;
  }

  public static Server createMockedServer(OutputStream bytes, int numPlayers, int initUnits, GameMap mockMap,
      OrderExecuter mockOX, Map<RemoteClient, Player> mockInGameClients,
      Map<RemoteClient, Player> mockWatchingClients)
      throws RemoteException {
    PrintStream output = new PrintStream(bytes, true);
    Server server = new Server(output, 0, numPlayers, initUnits) {
      @Override
      protected void initClientsSet() {
        this.inGameClients = mockInGameClients;
        this.watchingClients = mockWatchingClients;
      }

      @Override
      protected void initGameMap() {
        this.map = mockMap;
        this.ox = mockOX;
        out.println("GameMap initialized");
      }

      @Override
      protected void bindGameOnPort(int port) throws RemoteException {
        // LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
        out.println("RiscGameServer is ready to accept connections");
      }
    };
    return server;
  }

  public static Server createMockedCountDownLatchServer(OutputStream bytes, int numPlayers, int initUnits,
      GameMap mockMap, OrderExecuter mockOX, Map<RemoteClient, Player> mockInGameClients,
      Map<RemoteClient, Player> mockWatchingClients, CountDownLatch commitLatch, CyclicBarrier returnBarrier)
      throws RemoteException {
    PrintStream output = new PrintStream(bytes, true);
    Server server = new Server(output, 0, numPlayers, initUnits) {
      @Override
      protected void initClientsSet() {
        this.inGameClients = mockInGameClients;
        this.watchingClients = mockWatchingClients;
      }

      @Override
      protected void initGameMap() {
        this.map = mockMap;
        this.ox = mockOX;
        out.println("GameMap initialized");
      }

      @Override
      protected void bindGameOnPort(int port) throws RemoteException {
        // LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
        out.println("RiscGameServer is ready to accept connections");
      }

      @Override
      protected void setupCountDownLatches(int numInGame) {
        this.commitSignal = commitLatch;
        this.returnSignal = returnBarrier;
      }
    };
    return server;
  }

  @Test
  public void test_constructor() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);

    Server server = createServer(bytes, 3, 20, mockMap, mockOX);

    StringBuilder outputs = new StringBuilder();
    outputs.append("GameMap initialized\n");
    outputs.append("RiscGameServer is ready to accept connections\n");
    assertEquals(outputs.toString(), bytes.toString());
    assertThrows(IllegalArgumentException.class, () -> createServer(bytes, 1, 20, mockMap, mockOX));
    assertThrows(IllegalArgumentException.class, () -> createServer(bytes, 3, 0, mockMap, mockOX));

    // Test real constructor
    bytes.reset();
    PrintStream output = new PrintStream(bytes, true);
    assertDoesNotThrow(() -> new Server(output, 8082, 2, 20));
    assertEquals(outputs.toString(), bytes.toString());
  }

  @Test
  public void test_tryRegisterClient() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    RemoteClient cRed = mock(RemoteClient.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 2, 20, mockMap, mockOX, inGameClients, watchingClients);

    StringBuilder outputs = new StringBuilder();
    outputs.append("Player Blue joined game. (1/2)\n");
    outputs.append("Player Green joined game. (2/2)\n");

    bytes.reset();
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals("Already joined, cannot join repeatly", server.tryRegisterClient(cBlue, "ImNotBlue"));
    assertEquals("the Player Blue exists, please retry", server.tryRegisterClient(cGreen, "Blue"));
    assertEquals(null, server.tryRegisterClient(cGreen, "Green"));
    assertEquals("the game is in progress", server.tryRegisterClient(cRed, "Red"));
    assertEquals(outputs.toString(), bytes.toString());

    assertEquals(2, inGameClients.size());
    assertTrue(inGameClients.containsKey(cBlue));
    assertTrue(inGameClients.containsKey(cGreen));
    assertFalse(inGameClients.containsKey(cRed));
  }

  @Test
  public void test_getGameMap() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createServer(bytes, 3, 20, mockMap, mockOX);
    assertEquals(mockMap, server.getGameMap());
  }

  @Test
  public void test_getSelfStatus() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    RemoteClient cRed = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Player pGreen = mock(Player.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);

    inGameClients.put(cBlue, pBlue);
    watchingClients.put(cGreen, pGreen);
    assertEquals(pBlue, server.getSelfStatus(cBlue));
    assertEquals(pGreen, server.getSelfStatus(cGreen));
    assertEquals(null, server.getSelfStatus(cRed));
  }

  @Test
  public void test_getInitUints() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createServer(bytes, 3, 20, mockMap, mockOX);
    assertEquals(20, server.getInitUints());
  }

  @Test
  public void test_tryPickTerritoryGroupByName() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Player pGreen = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);

    // Setup mock
    inGameClients.put(cBlue, pBlue);
    inGameClients.put(cGreen, pGreen);

    doNothing().when(mockMap).assignGroup("GroupA", pBlue);
    doThrow(new IllegalArgumentException("GroupA has been Occupied")).when(mockMap).assignGroup("GroupA",
        pGreen);
    doThrow(new IllegalArgumentException("Blue is not a Group")).when(mockMap).assignGroup("Blue",
        pGreen);
    doNothing().when(mockMap).assignGroup("GroupB", pGreen);
    // Test
    assertEquals(null, server.tryPickTerritoryGroupByName(cBlue, "GroupA"));
    assertEquals("GroupA has been Occupied", server.tryPickTerritoryGroupByName(cGreen, "GroupA"));
    assertEquals("Blue is not a Group", server.tryPickTerritoryGroupByName(cGreen, "Blue"));
    assertEquals(null, server.tryPickTerritoryGroupByName(cGreen, "GroupB"));
    // Verify
    verify(mockMap, times(1)).assignGroup("GroupA", pBlue);
    verify(mockMap, times(1)).assignGroup("GroupA", pGreen);
    verify(mockMap, times(1)).assignGroup("Blue", pGreen);
    verify(mockMap, times(1)).assignGroup("GroupB", pGreen);
  }

  @Test
  public void test_tryPlaceUnitsOn() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Player pGreen = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);

    // Setup mockMap
    Territory tMordor = mock(Territory.class);
    Territory tHogwarts = mock(Territory.class);
    when(mockMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
    when(mockMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
    inGameClients.put(cBlue, pBlue);
    inGameClients.put(cGreen, pGreen);
    when(tMordor.getOwner()).thenReturn(pBlue);
    when(tHogwarts.getOwner()).thenReturn(pGreen);
    when(pBlue.getTotalUnits()).thenReturn(0, 0, 5, 20);
    doThrow(new IllegalArgumentException("units cannot be less than 0")).when(tMordor).increaseUnits(-1);
    // Test
    assertEquals("units cannot be less than 0", server.tryPlaceUnitsOn(cBlue, "Mordor", -1));
    assertEquals(null, server.tryPlaceUnitsOn(cBlue, "Mordor", 5));
    assertEquals(null, server.tryPlaceUnitsOn(cBlue, "Mordor", 15));
    assertEquals("Too many units", server.tryPlaceUnitsOn(cBlue, "Mordor", 1));
    assertEquals("Permission denied", server.tryPlaceUnitsOn(cBlue, "Hogwarts", 5));

  }

  @Test
  public void test_tryMoveOrder() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createServer(bytes, 3, 20, mockMap, mockOX);

    // Setup mockMap
    Territory tMordor = mock(Territory.class);
    Territory tHogwarts = mock(Territory.class);
    when(mockMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
    when(mockMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
    // Setup mockOX
    doNothing().doThrow(new IllegalArgumentException("Invalid Input:")).when(mockOX).doOneMove(any(MoveOrder.class));
    // Test
    assertEquals(null, server.tryMoveOrder(any(RemoteClient.class), "Hogwarts", "Mordor", 5));
    assertEquals("Invalid Input:", server.tryMoveOrder(any(RemoteClient.class), "Hogwarts", "Mordor", 5));

    // Verify
    verify(mockMap, atLeastOnce()).getTerritoryByName("Hogwarts");
    verify(mockMap, atLeastOnce()).getTerritoryByName("Mordor");
    verify(mockOX, times(2)).doOneMove(any(MoveOrder.class));
  }

  @Test
  public void test_tryAttackOrder() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createServer(bytes, 3, 20, mockMap, mockOX);
    // Setup mockMap
    Territory tMordor = mock(Territory.class);
    Territory tHogwarts = mock(Territory.class);
    when(mockMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
    when(mockMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
    // Setup mockOX
    doNothing().doThrow(new IllegalArgumentException("Invalid Input:")).when(mockOX).pushCombat(any(AttackOrder.class));
    // Test
    assertEquals(null, server.tryAttackOrder(any(RemoteClient.class), "Hogwarts", "Mordor", 5));
    assertEquals("Invalid Input:", server.tryAttackOrder(any(RemoteClient.class), "Hogwarts", "Mordor", 5));

    // Verify
    verify(mockMap, atLeastOnce()).getTerritoryByName("Hogwarts");
    verify(mockMap, atLeastOnce()).getTerritoryByName("Mordor");
    verify(mockOX, times(2)).pushCombat(any(AttackOrder.class));
  }

  @Test
  public void test_doCommitOrder() throws RemoteException, InterruptedException, BrokenBarrierException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Player pGreen = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();
    CountDownLatch commitLatch = mock(CountDownLatch.class);
    CyclicBarrier returnBarrier = mock(CyclicBarrier.class);

    Server server = createMockedCountDownLatchServer(bytes, 3, 20, mockMap,
        mockOX, inGameClients, watchingClients,
        commitLatch, returnBarrier);
    // Setup mock
    inGameClients.put(cBlue, pBlue);
    inGameClients.put(cGreen, pGreen);
    // Test
    assertDoesNotThrow(() -> server.doCommitOrder(cBlue));
    // Verify
    verify(commitLatch, times(1)).countDown();
    verify(commitLatch, never()).await();
    verify(returnBarrier, times(1)).await();
  }

  @Test
  public void test_isGameOver() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Player pGreen = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new ConcurrentHashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);
    // Test
    assertFalse(server.isGameOver());
    // Setup mockPlayer
    when(pBlue.isLose()).thenReturn(false);
    when(pGreen.isLose()).thenReturn(true);
    inGameClients.put(cBlue, pBlue);
    inGameClients.put(cGreen, pGreen);
    // Test again
    assertFalse(server.isGameOver());
    assertDoesNotThrow(() -> server.removeLostPlayer());
    assertTrue(server.isGameOver());
  }

  @Test
  public void test_pingInGameClients() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Server server = createServer(bytes, 2, 20, mockMap, mockOX);
    // Setup mockClient
    doNothing().when(cBlue).ping();
    doThrow(RemoteException.class).when(cGreen).ping();
    // Test
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals(null, server.tryRegisterClient(cGreen, "Green"));
    assertThrows(RemoteException.class, () -> server.pingInGameClients());
  }

  @Test
  public void test_removeLostPlayer() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Player pGreen = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);
    // Setup mockPlayer
    when(pBlue.isLose()).thenReturn(false);
    when(pGreen.isLose()).thenReturn(true);
    inGameClients.put(cBlue, pBlue);
    inGameClients.put(cGreen, pGreen);
    // before remove
    assertEquals(2, inGameClients.size());
    assertTrue(inGameClients.containsKey(cBlue));
    assertTrue(inGameClients.containsKey(cGreen));
    assertTrue(watchingClients.isEmpty());
    // do remove
    assertDoesNotThrow(() -> server.removeLostPlayer());
    // after remove
    assertEquals(1, inGameClients.size());
    assertTrue(inGameClients.containsKey(cBlue));
    assertFalse(inGameClients.containsKey(cGreen));
    assertEquals(1, watchingClients.size());
    assertTrue(watchingClients.containsKey(cGreen));
    // verify
    verify(pBlue, times(1)).isLose();
    verify(pGreen, times(1)).isLose();
  }

  @Test
  public void test_notifyAllWatchers() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);
    // Setup mockClient
    doNothing().when(cBlue).doDisplay(any(GameMap.class));
    doThrow(RemoteException.class).when(cGreen).doDisplay(any(GameMap.class));
    watchingClients.put(cBlue, null);
    watchingClients.put(cGreen, null);
    // Test
    // before notify
    assertEquals(2, watchingClients.size());
    assertTrue(watchingClients.containsKey(cBlue));
    assertTrue(watchingClients.containsKey(cGreen));
    // notify
    assertDoesNotThrow(() -> server.notifyAllWatchers());
    // after notify
    assertEquals(2, watchingClients.size());
    assertTrue(watchingClients.containsKey(cBlue));
    assertTrue(watchingClients.containsKey(cGreen));
    // Verify
    verify(cBlue, times(1)).doDisplay(any(GameMap.class));
    verify(cGreen, times(1)).doDisplay(any(GameMap.class));
  }

  @Test
  public void test_notifyAllClientsGameResult() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    RemoteClient cRed = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);
    // Setup mock
    inGameClients.put(cBlue, pBlue);
    watchingClients.put(cGreen, null);
    watchingClients.put(cRed, null);
    doThrow(RemoteException.class).when(cRed).doDisplay(anyString());
    // Test
    assertDoesNotThrow(() -> server.notifyAllClientsGameResult());
  }

  @Test
  public void test_start() throws RemoteException, InterruptedException, NotBoundException, BrokenBarrierException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Map<RemoteClient, Player> mockInGameClients = mock(HashMap.class);

    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();
    CountDownLatch commitLatch = mock(CountDownLatch.class);
    CyclicBarrier returnBarrier = mock(CyclicBarrier.class);
    Server server = createMockedCountDownLatchServer(bytes, 3, 20, mockMap,
        mockOX, mockInGameClients, watchingClients,
        commitLatch, returnBarrier);

    // Setup mocks
    when(mockInGameClients.size()).thenReturn(3, 3, 2, 2, 1);
    // Test
    bytes.reset();
    assertDoesNotThrow(() -> server.start());
    assertEquals("RiscGameServer is closed\n", bytes.toString());

    verify(mockInGameClients, times(6)).size();
    verify(mockInGameClients, times(5)).keySet();
    verify(commitLatch, never()).countDown();
    verify(commitLatch, times(4)).await();
    verify(returnBarrier, times(4)).await();
  }

  @Test
  public void test_closeAllClients() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    RemoteClient cRed = mock(RemoteClient.class);
    RemoteClient cCyan = mock(RemoteClient.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Map<RemoteClient, Player> watchingClients = new HashMap<RemoteClient, Player>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);

    // Setup mockClient
    inGameClients.put(cBlue, null);
    inGameClients.put(cGreen, null);
    watchingClients.put(cRed, null);
    watchingClients.put(cCyan, null);
    doThrow(RemoteException.class).when(cGreen).close();
    doThrow(RemoteException.class).when(cCyan).close();
    // Test
    assertDoesNotThrow(() -> server.closeAllClients());
    // Verify
    verify(cBlue, times(1)).close();
    verify(cGreen, times(1)).close();
    verify(cRed, times(1)).close();
    verify(cCyan, times(1)).close();
  }
}
