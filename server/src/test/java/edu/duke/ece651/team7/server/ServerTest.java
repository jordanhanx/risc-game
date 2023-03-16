package edu.duke.ece651.team7.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.duke.ece651.team7.shared.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.google.common.cache.AbstractCache;

public class ServerTest {
  public static Server createMockedServer(OutputStream bytes, int numPlayers, int initUnits, GameMap mockMap,
      OrderExecuter mockOX)
      throws RemoteException {
    PrintStream output = new PrintStream(bytes, true);
    Server server = new Server(output, numPlayers, initUnits) {
      @Override
      protected void initGameMap() {
        this.map = mockMap;
        this.ox = mockOX;
        out.println("GameMap initialized");
      }

      @Override
      protected void listenOnPort(int port) throws RemoteException {
        // LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
        out.println("RiscGameServer is ready to accept connections");
      }
    };
    return server;
  }

  @Test
  public void test_constructor_start() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);
    assertDoesNotThrow(() -> server.start(8000));
    StringBuilder outputs = new StringBuilder();
    outputs.append("GameMap initialized\n");
    outputs.append("RiscGameServer is ready to accept connections\n");
    assertEquals(outputs.toString(), bytes.toString());
    assertThrows(IllegalArgumentException.class, () -> createMockedServer(bytes, 1, 20, mockMap, mockOX));
    assertThrows(IllegalArgumentException.class, () -> createMockedServer(bytes, 3, 0, mockMap, mockOX));
  }

  @Test
  public void test_tryRegisterClient() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    RemoteClient cRed = mock(RemoteClient.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createMockedServer(bytes, 2, 20, mockMap, mockOX);

    StringBuilder outputs = new StringBuilder();
    outputs.append("Player Blue joined game. (1/2)\n");
    outputs.append("Player Green joined game. (2/2)\n");

    bytes.reset();
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals("Already joined, cannot join repeatly", server.tryRegisterClient(cBlue, "ImNotBlue"));
    assertEquals(null, server.tryRegisterClient(cGreen, "Green"));
    assertEquals("Clients are full", server.tryRegisterClient(cRed, "Red"));
    assertEquals(outputs.toString(), bytes.toString());
  }

  @Test
  public void test_getGameMap() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);
    assertEquals(mockMap, server.getGameMap());
  }

  @Test
  public void test_getSelfStatus() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);

    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals("Blue", server.getSelfStatus(cBlue).getName());
  }

  @Test
  public void test_getInitUints() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);
    assertEquals(20, server.getInitUints());
  }

  @Test
  public void test_tryPickTerritoryGroupByName() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals("To be completed", server.tryPickTerritoryGroupByName(cBlue, "GroupA"));
  }

  @Test
  public void test_tryPlaceUnitsOn() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals(null, server.tryRegisterClient(cGreen, "Green"));
    // Setup mockMap
    Territory tMordor = mock(Territory.class);
    Territory tHogwarts = mock(Territory.class);
    when(mockMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
    when(mockMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
    when(tMordor.getOwner()).thenReturn(server.getInGameClients().get(cBlue));
    when(tHogwarts.getOwner()).thenReturn(server.getInGameClients().get(cGreen));

    doThrow(new IllegalArgumentException("units cannot be less than 0")).when(tMordor).increaseUnits(-1);
    // Test
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
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));

    // Setup mockMap
    Territory tMordor = mock(Territory.class);
    Territory tHogwarts = mock(Territory.class);
    when(mockMap.getTerritoryByName("Mordor")).thenReturn(tMordor);
    when(mockMap.getTerritoryByName("Hogwarts")).thenReturn(tHogwarts);
    // Setup mockOX
    when(mockOX.doOneMove(any(MoveOrder.class))).thenReturn(null)
        .thenThrow(new IllegalArgumentException("Invalid input"));
    // Test
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
    Server server = createMockedServer(bytes, 3, 20, mockMap, mockOX);

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
  public void test_checkConnectionToClients() throws RemoteException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    GameMap mockMap = mock(GameMap.class);
    OrderExecuter mockOX = mock(OrderExecuter.class);
    RemoteClient cBlue = mock(RemoteClient.class);
    RemoteClient cGreen = mock(RemoteClient.class);
    Server server = createMockedServer(bytes, 2, 20, mockMap, mockOX);
    assertEquals(null, server.tryRegisterClient(cBlue, "Blue"));
    assertEquals(null, server.tryRegisterClient(cGreen, "Green"));

    // Setup mockClient
    when(cBlue.isAlive()).thenReturn(true);
    when(cGreen.isAlive()).thenThrow(RemoteException.class);

    assertThrows(RemoteException.class, () -> server.checkConnectionToClients());
  }

  @Test
  public void test_removeLostPlayer() throws RemoteException {
  }
}
