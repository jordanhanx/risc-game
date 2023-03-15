package edu.duke.ece651.team7.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.*;

public class ClientTest {
    public static Client createMockedClient(RemoteServer mockServer, String inputData, OutputStream bytes)
            throws RemoteException, NotBoundException {
        PrintStream output = new PrintStream(bytes, true);
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        Client client = new Client("MockHost", 8000, input, output) {
            @Override
            protected void connectRemoteServer(String host, int port) {
                this.server = mockServer;
                out.println("Connected to " + host + ":" + port + " successfully.");
            }
        };
        return client;
    }

    @Test
    public void test_constructor() throws RemoteException, NotBoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        createMockedClient(mockServer, "Red\nGreen\nBlue\n\n", bytes);
        assertEquals("Connected to MockHost:8000 successfully.\n", bytes.toString());
    }

    @Test
    public void test_readUserInput() throws RemoteException, NotBoundException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);

        Client client1 = createMockedClient(mockServer, "Red\nGreen\nBlue\n\n", bytes);
        bytes.reset();
        String prompt = "Please name your Player:";
        String[] expect = new String[4];
        expect[0] = "Red";
        expect[1] = "Green";
        expect[2] = "Blue";
        expect[3] = "";
        for (String s : expect) {
            assertEquals(s, client1.readUserInput(prompt));
            assertEquals(prompt + "\n", bytes.toString());
            bytes.reset();
        }
        // test read EOF
        Client client2 = createMockedClient(mockServer, "", bytes);
        assertThrows(EOFException.class, () -> client2.readUserInput(""));
    }

    @Test
    public void test_registerPlayer() throws RemoteException, NotBoundException, InterruptedException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Client clientA = createMockedClient(mockServer, "Green\nGreen", bytes);
        Client clientB = createMockedClient(mockServer, "Blue", bytes);
        Client clientC = createMockedClient(mockServer, "Blue\nRed", bytes);
        Client clientD = createMockedClient(mockServer, "Cyan", bytes);
        bytes.reset();
        when(mockServer.tryRegisterClient(clientA, "Green")).thenReturn(null, "You has already joined.");
        when(mockServer.tryRegisterClient(clientB, "Blue")).thenReturn(null);
        when(mockServer.tryRegisterClient(clientC, "Blue")).thenReturn("Player Blue has existed.");
        when(mockServer.tryRegisterClient(clientC, "Red")).thenReturn(null);
        when(mockServer.tryRegisterClient(clientD, "Cyan")).thenReturn("The game already has enough players");

        assertDoesNotThrow(() -> clientA.registerPlayer());
        assertEquals("Please name your Player:\nJoined a RiskGame as Player: Green\n", bytes.toString());
        assertThrows(IllegalArgumentException.class, () -> clientA.registerPlayer());
        bytes.reset();

        assertDoesNotThrow(() -> clientB.registerPlayer());
        assertEquals("Please name your Player:\nJoined a RiskGame as Player: Blue\n", bytes.toString());
        bytes.reset();

        assertThrows(IllegalArgumentException.class, () -> clientC.registerPlayer());
        bytes.reset();
        assertDoesNotThrow(() -> clientC.registerPlayer());
        assertEquals("Please name your Player:\nJoined a RiskGame as Player: Red\n", bytes.toString());
        bytes.reset();

        assertThrows(IllegalArgumentException.class, () -> clientD.registerPlayer());
        bytes.reset();

        verify(mockServer, atLeastOnce()).tryRegisterClient(clientA, "Green");
        verify(mockServer, atLeastOnce()).tryRegisterClient(clientB, "Blue");
        verify(mockServer, atLeastOnce()).tryRegisterClient(clientC, "Blue");
        verify(mockServer, atLeastOnce()).tryRegisterClient(clientC, "Red");
        verify(mockServer, atLeastOnce()).tryRegisterClient(clientD, "Cyan");
    }

    @Test
    public void test_parseOrder() throws RemoteException, NotBoundException, InterruptedException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Client client = createMockedClient(mockServer, "", bytes);

        when(mockServer.tryMoveOrder(client, "Mordor", "Hogwarts", 5))
                .thenReturn(null, "Insufficient units.");
        when(mockServer.tryMoveOrder(client, "Duke", "Hogwarts", 5))
                .thenReturn("Duke doesn't exist.");
        when(mockServer.tryMoveOrder(client, "Gondor", "Hogwarts", 5))
                .thenReturn("Path from Gondor to Hogwarts does not exist.");
        when(mockServer.tryAttackOrder(client, "Hogwarts", "Roshar", 5))
                .thenReturn(null, "Insufficient units.");
        when(mockServer.tryAttackOrder(client, "Hogwarts", "Elantris", 5))
                .thenReturn("Hogwarts and Elantris are not adjacent.");

        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("evom Mordor Hogwarts 5"));
        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("kcatta Mordor Hogwarts 5"));
        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("Move Mordor Hogwarts 5 5"));
        assertDoesNotThrow(() -> client.parseOrder("Move Mordor Hogwarts 5"));
        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("Move Mordor Hogwarts 5"));
        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("move Duke Hogwarts 5"));
        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("m Gondor Hogwarts 5"));
        assertDoesNotThrow(() -> client.parseOrder("Attack Hogwarts Roshar 5"));
        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("A Hogwarts Roshar 5"));
        assertThrows(IllegalArgumentException.class, () -> client.parseOrder("A Hogwarts Elantris 5"));

        verify(mockServer, atLeastOnce()).tryMoveOrder(client, "Mordor", "Hogwarts", 5);
        verify(mockServer, atLeastOnce()).tryMoveOrder(client, "Duke", "Hogwarts", 5);
        verify(mockServer, atLeastOnce()).tryMoveOrder(client, "Gondor", "Hogwarts", 5);
        verify(mockServer, atLeastOnce()).tryAttackOrder(client, "Hogwarts", "Roshar", 5);
        verify(mockServer, atLeastOnce()).tryAttackOrder(client, "Hogwarts", "Elantris", 5);
    }

    @Test
    public void test_playOneTurn() throws RemoteException, NotBoundException, InterruptedException, IOException {
        StringBuilder inputs = new StringBuilder();
        inputs.append("evom Mordor Hogwarts 5\n");
        inputs.append("Move Mordor Hogwarts 5\n");
        inputs.append("dOnE\n");
        StringBuilder outputs = new StringBuilder();
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append(" 14 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  3 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("You are the Red player, what would you like to do?\n(M)ove\n(A)ttack\n(D)one\n");
        outputs.append("Invalid input: undefined order\n");
        outputs.append("You are the Red player, what would you like to do?\n(M)ove\n(A)ttack\n(D)one\n");
        outputs.append("You are the Red player, what would you like to do?\n(M)ove\n(A)ttack\n(D)one\n");
        outputs.append("Waiting for other players...\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Player mockPlayer = mock(Player.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tHogwarts = mock(Territory.class);
        Territory tMordor = mock(Territory.class);

        Client client = createMockedClient(mockServer, inputs.toString(), bytes);

        // Setup mockServer response
        when(mockServer.getGameMap()).thenReturn(mockMap);
        when(mockPlayer.getName()).thenReturn("Red");
        when(mockServer.getSelfStatus(client)).thenReturn(mockPlayer);
        when(mockServer.tryMoveOrder(client, "Mordor", "Hogwarts", 5)).thenReturn(null);
        doNothing().when(mockServer).doCommitOrder(client);
        // Setup mockGameMap
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tMordor.getOwner()).thenReturn(mockPlayer);
        when(tHogwarts.getOwner()).thenReturn(mockPlayer);
        LinkedList<Territory> redTerritories = new LinkedList<Territory>() {
            {
                add(tMordor);
                add(tHogwarts);
            }
        };
        when(mockMap.getTerritories()).thenReturn(redTerritories);
        when(mockPlayer.getTerritories()).thenReturn(redTerritories);
        LinkedList<Territory> tMordorNeighbors = new LinkedList<Territory>() {
            {
                add(tHogwarts);
            }
        };
        LinkedList<Territory> tHogwartsNeighbors = new LinkedList<Territory>() {
            {
                add(tMordor);
            }
        };
        when(mockMap.getNeighbors("Mordor")).thenReturn(tMordorNeighbors);
        when(mockMap.getNeighbors("Hogwarts")).thenReturn(tHogwartsNeighbors);
        when(tMordor.getUnits()).thenReturn(14);
        when(tHogwarts.getUnits()).thenReturn(3);

        // Test begin
        bytes.reset();
        assertDoesNotThrow(() -> client.playOneTurn());
        assertEquals(outputs.toString(), bytes.toString());

        verify(mockServer, atLeastOnce()).getGameMap();
        verify(mockServer, atLeastOnce()).getSelfStatus(client);
        verify(mockServer, atLeastOnce()).tryMoveOrder(client, "Mordor", "Hogwarts", 5);
        verify(mockServer, atLeastOnce()).doCommitOrder(client);

        verify(mockPlayer, atLeastOnce()).getName();
        verify(mockPlayer, atLeastOnce()).getTerritories();

        verify(mockMap, atLeastOnce()).getTerritories();
        verify(mockMap, atLeastOnce()).getNeighbors("Mordor");
        verify(mockMap, atLeastOnce()).getNeighbors("Hogwarts");

        verify(tMordor, atLeastOnce()).getUnits();
        verify(tHogwarts, atLeastOnce()).getUnits();
        verify(tMordor, atLeastOnce()).getName();
        verify(tHogwarts, atLeastOnce()).getName();
        verify(tMordor, atLeastOnce()).getOwner();
        verify(tHogwarts, atLeastOnce()).getOwner();

    }

    @Test
    public void test_checkIfGameOver() throws RemoteException, NotBoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Player mockPlayerA = mock(Player.class);
        Player mockPlayerB = mock(Player.class);
        Client clientA = createMockedClient(mockServer, "", bytes);
        Client clientB = createMockedClient(mockServer, "", bytes);

        bytes.reset();
        when(mockPlayerA.getName()).thenReturn("A");
        when(mockPlayerB.getName()).thenReturn("B");
        when(mockServer.isGameOver()).thenReturn(false, true);
        when(mockServer.getSelfStatus(clientA)).thenReturn(mockPlayerA);
        when(mockServer.getSelfStatus(clientB)).thenReturn(mockPlayerB);
        when(mockServer.getWinner()).thenReturn(mockPlayerB);
        assertFalse(clientA.checkIfGameOver());
        assertTrue(clientA.checkIfGameOver());
        assertEquals("Game over! B is the winner.\n", bytes.toString());
        bytes.reset();
        assertTrue(clientB.checkIfGameOver());
        assertEquals("Congrats! YOU WIN!\n", bytes.toString());

        verify(mockPlayerA, never()).getName();
        verify(mockPlayerB, atLeastOnce()).getName();

        verify(mockServer, atLeastOnce()).isGameOver();
        verify(mockServer, atLeastOnce()).getSelfStatus(clientA);
        verify(mockServer, atLeastOnce()).getSelfStatus(clientB);
        verify(mockServer, atLeastOnce()).getWinner();
    }

    @Test
    public void test_start() throws RemoteException, NotBoundException, InterruptedException, IOException {
        StringBuilder inputs = new StringBuilder();
        inputs.append("Red\n");
        inputs.append("evom Mordor Hogwarts 5\n");
        inputs.append("Move Mordor Hogwarts 5\n");
        inputs.append("dOnE\n");
        StringBuilder outputs = new StringBuilder();
        outputs.append("Please name your Player:\n");
        outputs.append("Joined a RiskGame as Player: Red\n");
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append(" 14 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  3 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("You are the Red player, what would you like to do?\n(M)ove\n(A)ttack\n(D)one\n");
        outputs.append("Invalid input: undefined order\n");
        outputs.append("You are the Red player, what would you like to do?\n(M)ove\n(A)ttack\n(D)one\n");
        outputs.append("You are the Red player, what would you like to do?\n(M)ove\n(A)ttack\n(D)one\n");
        outputs.append("Waiting for other players...\n");
        outputs.append("Congrats! YOU WIN!\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Player mockPlayer = mock(Player.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tHogwarts = mock(Territory.class);
        Territory tMordor = mock(Territory.class);

        Client client = createMockedClient(mockServer, inputs.toString(), bytes);

        // Setup mockServer
        when(mockServer.getGameMap()).thenReturn(mockMap);
        when(mockPlayer.getName()).thenReturn("Red");
        when(mockServer.tryRegisterClient(client, "Red")).thenReturn(null);
        when(mockServer.isGameOver()).thenReturn(false, true);
        when(mockServer.getWinner()).thenReturn(mockPlayer);
        when(mockServer.getSelfStatus(client)).thenReturn(mockPlayer);
        when(mockServer.tryMoveOrder(client, "Mordor", "Hogwarts", 5)).thenReturn(null);
        doNothing().when(mockServer).doCommitOrder(client);

        // Setup mockGameMap
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tMordor.getOwner()).thenReturn(mockPlayer);
        when(tHogwarts.getOwner()).thenReturn(mockPlayer);
        LinkedList<Territory> redTerritories = new LinkedList<Territory>() {
            {
                add(tMordor);
                add(tHogwarts);
            }
        };
        when(mockMap.getTerritories()).thenReturn(redTerritories);
        when(mockPlayer.getTerritories()).thenReturn(redTerritories);
        LinkedList<Territory> tMordorNeighbors = new LinkedList<Territory>() {
            {
                add(tHogwarts);
            }
        };
        LinkedList<Territory> tHogwartsNeighbors = new LinkedList<Territory>() {
            {
                add(tMordor);
            }
        };
        when(mockMap.getNeighbors("Mordor")).thenReturn(tMordorNeighbors);
        when(mockMap.getNeighbors("Hogwarts")).thenReturn(tHogwartsNeighbors);
        when(tMordor.getUnits()).thenReturn(14);
        when(tHogwarts.getUnits()).thenReturn(3);

        bytes.reset();
        assertDoesNotThrow(() -> client.start());
        assertEquals(outputs.toString(), bytes.toString());

        verify(mockServer, atLeastOnce()).tryRegisterClient(client, "Red");
        verify(mockServer, atLeastOnce()).isGameOver();
        verify(mockServer, atLeastOnce()).getGameMap();
        verify(mockServer, atLeastOnce()).getWinner();
        verify(mockServer, atLeastOnce()).getSelfStatus(client);
        verify(mockServer, atLeastOnce()).tryMoveOrder(client, "Mordor", "Hogwarts", 5);
        verify(mockServer, atLeastOnce()).doCommitOrder(client);

        verify(mockPlayer, atLeastOnce()).getName();
        verify(mockPlayer, atLeastOnce()).getTerritories();

        verify(mockMap, atLeastOnce()).getTerritories();
        verify(mockMap, atLeastOnce()).getNeighbors("Mordor");
        verify(mockMap, atLeastOnce()).getNeighbors("Hogwarts");

        verify(tMordor, atLeastOnce()).getUnits();
        verify(tHogwarts, atLeastOnce()).getUnits();
        verify(tMordor, atLeastOnce()).getName();
        verify(tHogwarts, atLeastOnce()).getName();
        verify(tMordor, atLeastOnce()).getOwner();
        verify(tHogwarts, atLeastOnce()).getOwner();

    }
}
