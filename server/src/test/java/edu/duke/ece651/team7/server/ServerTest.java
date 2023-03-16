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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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
      Set<RemoteClient> mockWatchingClients)
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
      Set<RemoteClient> mockWatchingClients, CountDownLatch commitLatch, CountDownLatch returnLatch)
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
        this.returnSignal = returnLatch;
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
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

    Server server = createMockedServer(bytes, 2, 20, mockMap, mockOX, inGameClients, watchingClients);

    StringBuilder outputs = new StringBuilder();
    outputs.append("Player Blue joined game. (1/2)\n");
    outputs.append("Player Green joined game. (2/2)\n");

    bytes.reset();
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals("Already joined, cannot join repeatly", server.tryRegisterClient(cBlue, "ImNotBlue"));
    assertEquals(null, server.tryRegisterClient(cGreen, "Green"));
    assertEquals("Clients are full", server.tryRegisterClient(cRed, "Red"));
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
    Player pBlue = mock(Player.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);

    inGameClients.put(cBlue, pBlue);
    assertEquals(pBlue, server.getSelfStatus(cBlue));
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
    Player pBlue = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);

    inGameClients.put(cBlue, pBlue);
    assertEquals("To be completed", server.tryPickTerritoryGroupByName(cBlue, "GroupA"));
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
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

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
    // Test
    doThrow(new IllegalArgumentException("units cannot be less than 0")).when(tMordor).increaseUnits(-1);
    assertEquals(null, server.tryPlaceUnitsOn(cBlue, "Mordor", 5));
    assertEquals("Permission denied", server.tryPlaceUnitsOn(cBlue, "Hogwarts", 5));
    assertEquals("units cannot be less than 0", server.tryPlaceUnitsOn(cBlue, "Mordor", -1));
  }

  @Test
  public void test_tryMoveOrder() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    Server server = createServer(bytes, 3, 20, mockMap, mockOX);
    // Setup mockMap
    Territory tMordor = mock(Territory.class);
    Territory tHogwarts = mock(Territory.class);
    when(mockMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
    when(mockMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
    // Setup mockOX
    when(mockOX.doOneMove(any(MoveOrder.class))).thenReturn(null)
        .thenThrow(new IllegalArgumentException("Invalid input"));
    // Test
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals(null, server.tryMoveOrder(cBlue, "Hogwarts", "Mordor", 5));
    assertEquals("Invalid input", server.tryMoveOrder(cBlue, "Hogwarts", "Mordor", 5));
    // Verify
    verify(mockOX, times(2)).doOneMove(any(MoveOrder.class));
    verify(mockMap, atLeastOnce()).getTerritoryByName("Hogwarts");
    verify(mockMap, atLeastOnce()).getTerritoryByName("Mordor");
  }

  @Test
  public void test_tryAttackOrder() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    Server server = createServer(bytes, 3, 20, mockMap, mockOX);
    // Setup mockMap
    Territory tMordor = mock(Territory.class);
    Territory tHogwarts = mock(Territory.class);
    when(mockMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
    when(mockMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
    // Setup mockOX
    when(mockOX.pushCombat(any(AttackOrder.class))).thenReturn(null)
        .thenThrow(new IllegalArgumentException("Invalid input"));
    // Test
    assertEquals(null, server.tryAttackOrder(cBlue, "Hogwarts", "Mordor", 5));
    assertEquals("Invalid input", server.tryAttackOrder(cBlue, "Hogwarts", "Mordor", 5));
    // Verify
    verify(mockOX, times(2)).pushCombat(any(AttackOrder.class));
    verify(mockMap, atLeastOnce()).getTerritoryByName("Hogwarts");
    verify(mockMap, atLeastOnce()).getTerritoryByName("Mordor");
  }

  @Test
  public void test_doCommitOrder() throws RemoteException, InterruptedException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Player pGreen = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();
    CountDownLatch commitLatch = mock(CountDownLatch.class);
    CountDownLatch returnLatch = mock(CountDownLatch.class);

    Server server = createMockedCountDownLatchServer(bytes, 3, 20, mockMap,
        mockOX, inGameClients, watchingClients,
        commitLatch, returnLatch);
    // Setup mock
    inGameClients.put(cBlue, pBlue);
    inGameClients.put(cGreen, pGreen);
    // Test
    assertDoesNotThrow(() -> server.doCommitOrder(cBlue));
    // Verify
    verify(cBlue, times(1)).ping();
    verify(cGreen, times(1)).ping();
    verify(commitLatch, times(1)).countDown();
    verify(commitLatch, never()).await();
    verify(returnLatch, never()).countDown();
    verify(returnLatch, times(1)).await();
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
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

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
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

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
    assertTrue(watchingClients.contains(cGreen));
    // verify
    verify(pBlue, times(1)).isLose();
    verify(pGreen, times(1)).isLose();
  }

  @Test
  public void test_notifyAllWatchersDisplay() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);
    // Setup mockClient
    doNothing().when(cBlue).doDisplay(any(GameMap.class));
    doThrow(RemoteException.class).when(cGreen).doDisplay(any(GameMap.class));
    watchingClients.add(cBlue);
    watchingClients.add(cGreen);
    // Test
    // before notify
    assertEquals(2, watchingClients.size());
    assertTrue(watchingClients.contains(cBlue));
    assertTrue(watchingClients.contains(cGreen));
    // notify
    assertDoesNotThrow(() -> server.notifyAllWatchersDisplay());
    // after notify
    assertEquals(1, watchingClients.size());
    assertTrue(watchingClients.contains(cBlue));
    assertFalse(watchingClients.contains(cGreen));
    // Verify
    verify(cBlue, times(1)).doDisplay(any(GameMap.class));
    verify(cGreen, times(1)).doDisplay(any(GameMap.class));
  }

  @Test
  public void test_doEndGame() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    RemoteClient cRed = mock(RemoteClient.class);
    Player pBlue = mock(Player.class);
    Map<RemoteClient, Player> inGameClients = new HashMap<RemoteClient, Player>();
    Set<RemoteClient> watchingClients = new HashSet<RemoteClient>();

    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX, inGameClients, watchingClients);
    // Setup mock
    inGameClients.put(cBlue, pBlue);
    watchingClients.add(cGreen);
    watchingClients.add(cRed);
    doThrow(RemoteException.class).when(cRed).doDisplay(anyString());
    // Test
    assertDoesNotThrow(() -> server.doEndGame());
    assertTrue(inGameClients.isEmpty());
    assertTrue(watchingClients.isEmpty());
  }

  @Test
  public void test_start() throws RemoteException, InterruptedException, NotBoundException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    Map<RemoteClient, Player> mockInGameClients = mock(HashMap.class);

    HashSet<RemoteClient> watchingClients = new HashSet<RemoteClient>();
    CountDownLatch commitLatch = mock(CountDownLatch.class);
    CountDownLatch returnLatch = mock(CountDownLatch.class);
    Server server = createMockedCountDownLatchServer(bytes, 3, 20, mockMap,
        mockOX, mockInGameClients, watchingClients,
        commitLatch, returnLatch);

    // Setup mocks
    when(mockInGameClients.size()).thenReturn(2, 1, 1).thenThrow(RuntimeException.class);
    when(mockInGameClients.keySet()).thenReturn(new HashMap<RemoteClient, Player>().keySet());
    // Test
    assertThrows(RuntimeException.class, () -> server.start());
    verify(mockInGameClients, times(4)).size();
    verify(mockInGameClients, times(4)).keySet();
    verify(commitLatch, never()).countDown();
    verify(commitLatch, times(3)).await();
    verify(returnLatch, times(1)).countDown();
    verify(returnLatch, never()).await();
  }
}
