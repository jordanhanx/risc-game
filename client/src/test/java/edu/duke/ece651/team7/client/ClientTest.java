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
    public static Client createMockedClient(RemoteServer serv, String inputData, OutputStream bytes)
            throws RemoteException, NotBoundException {
        PrintStream output = new PrintStream(bytes, true);
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        Client client = new Client("MockHost", 0, input, output) {
            @Override
            protected void connectRemoteServer(String host, int port) {
                this.server = serv;
                out.println("Connected to " + host + ":" + port + " successfully.");
            }
        };
        return client;
    }

    @Test
    public void test_constructor() throws RemoteException, NotBoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        createMockedClient(mockServer, "", bytes);
        assertEquals("Connected to MockHost:0 successfully.\n", bytes.toString());
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
        StringBuilder inputs = new StringBuilder();
        inputs.append("Blue\n");
        inputs.append("Green\n");
        StringBuilder outputs = new StringBuilder();
        outputs.append("Please name your Player:\n");
        outputs.append("Failed to register: Player Blue already exists\n");
        outputs.append("Please name your Player:\n");
        outputs.append("Joined a RiskGame as Player Green\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Client client = createMockedClient(mockServer, inputs.toString(), bytes);

        // Setup mockServer
        when(mockServer.tryRegisterClient(client, "Blue")).thenReturn("Player Blue already exists");
        when(mockServer.tryRegisterClient(client, "Green")).thenReturn(null);

        // Test
        bytes.reset();
        assertDoesNotThrow(() -> client.registerPlayer());
        assertEquals(outputs.toString(), bytes.toString());

        // Verify
        verify(mockServer, times(1)).tryRegisterClient(client, "Blue");
        verify(mockServer, times(1)).tryRegisterClient(client, "Green");
    }

    @Test
    public void test_pickTerritoryGroup() throws RemoteException, NotBoundException {
        StringBuilder inputs = new StringBuilder();
        inputs.append("\n");
        inputs.append("GroupA\n");
        inputs.append("GroupB\n");
        StringBuilder outputs = new StringBuilder();
        outputs.append("GroupA player:\n");
        outputs.append("--------------\n");
        outputs.append("  5 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  5 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("GroupB player:\n");
        outputs.append("--------------\n");
        outputs.append("  5 units in Narnia (next to: Midkemia)\n");
        outputs.append("  5 units in Midkemia (next to: Narnia)\n");
        outputs.append("\n");
        outputs.append("Please choose your Territory Group:\n");
        outputs.append("Invalid input: undefined group name\n");
        outputs.append("GroupA player:\n");
        outputs.append("--------------\n");
        outputs.append("  5 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  5 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("GroupB player:\n");
        outputs.append("--------------\n");
        outputs.append("  5 units in Narnia (next to: Midkemia)\n");
        outputs.append("  5 units in Midkemia (next to: Narnia)\n");
        outputs.append("\n");
        outputs.append("Please choose your Territory Group:\n");
        outputs.append("Invalid input: the group has been picked\n");
        outputs.append("GroupB player:\n");
        outputs.append("--------------\n");
        outputs.append("  5 units in Narnia (next to: Midkemia)\n");
        outputs.append("  5 units in Midkemia (next to: Narnia)\n");
        outputs.append("\n");
        outputs.append("Blue player:\n");
        outputs.append("------------\n");
        outputs.append("  5 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  5 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("Please choose your Territory Group:\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player pBlue = mock(Player.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tHogwarts = mock(Territory.class);
        Territory tMordor = mock(Territory.class);
        Territory tNarnia = mock(Territory.class);
        Territory tMidkemia = mock(Territory.class);

        Client client = createMockedClient(mockServer, inputs.toString(), bytes);

        // Setup mockGameMap
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tNarnia.getName()).thenReturn("Narnia");
        when(tMidkemia.getName()).thenReturn("Midkemia");
        when(tMordor.getOwner()).thenReturn(groupA, groupA, pBlue);
        when(tHogwarts.getOwner()).thenReturn(groupA, groupA, pBlue);
        when(tNarnia.getOwner()).thenReturn(groupB, groupB, groupB);
        when(tMidkemia.getOwner()).thenReturn(groupB, groupB, groupB);
        when(tMordor.getUnits()).thenReturn(5);
        when(tHogwarts.getUnits()).thenReturn(5);
        when(tNarnia.getUnits()).thenReturn(5);
        when(tMidkemia.getUnits()).thenReturn(5);
        LinkedList<Territory> allTerritories = new LinkedList<Territory>() {
            {
                add(tNarnia);
                add(tMidkemia);
                add(tHogwarts);
                add(tMordor);
            }
        };
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
        LinkedList<Territory> tNarniaNeighbors = new LinkedList<Territory>() {
            {
                add(tMidkemia);
            }
        };
        LinkedList<Territory> tMidkemiaNeighbors = new LinkedList<Territory>() {
            {
                add(tNarnia);
            }
        };
        when(mockMap.getTerritories()).thenReturn(allTerritories);
        when(mockMap.getNeighbors("Mordor")).thenReturn(tMordorNeighbors);
        when(mockMap.getNeighbors("Hogwarts")).thenReturn(tHogwartsNeighbors);
        when(mockMap.getNeighbors("Narnia")).thenReturn(tNarniaNeighbors);
        when(mockMap.getNeighbors("Midkemia")).thenReturn(tMidkemiaNeighbors);

        // Setup mockPlayers
        when(groupA.getName()).thenReturn("GroupA");
        when(groupB.getName()).thenReturn("GroupB");
        when(pBlue.getName()).thenReturn("Blue");

        when(groupA.compareTo(groupA)).thenReturn(0);
        when(groupA.compareTo(groupB)).thenReturn(-1);
        when(groupA.compareTo(pBlue)).thenReturn(-1);

        when(groupB.compareTo(groupA)).thenReturn(1);
        when(groupB.compareTo(groupB)).thenReturn(0);
        when(groupB.compareTo(pBlue)).thenReturn(-1);

        when(pBlue.compareTo(groupA)).thenReturn(1);
        when(pBlue.compareTo(groupB)).thenReturn(1);
        when(pBlue.compareTo(pBlue)).thenReturn(0);

        LinkedList<Territory> groupATerritories = new LinkedList<Territory>() {
            {
                add(tMordor);
                add(tHogwarts);
            }
        };
        LinkedList<Territory> groupBTerritories = new LinkedList<Territory>() {
            {
                add(tNarnia);
                add(tMidkemia);
            }
        };
        when(groupA.getTerritories()).thenReturn(groupATerritories);
        when(groupB.getTerritories()).thenReturn(groupBTerritories);
        when(pBlue.getTerritories()).thenReturn(groupATerritories);
        // Setup mockServer
        when(mockServer.getGameMap()).thenReturn(mockMap);
        when(mockServer.tryPickTerritoryGroupByName(client, "")).thenReturn("undefined group name");
        when(mockServer.tryPickTerritoryGroupByName(client, "GroupA")).thenReturn("the group has been picked");
        when(mockServer.tryPickTerritoryGroupByName(client, "GroupB")).thenReturn(null);

        // Test
        bytes.reset();
        assertDoesNotThrow(() -> client.pickTerritoryGroup());
        assertEquals(outputs.toString(), bytes.toString());

        // Verify
        verify(mockServer, times(1)).tryPickTerritoryGroupByName(client, "");
        verify(mockServer, times(1)).tryPickTerritoryGroupByName(client, "GroupA");
        verify(mockServer, times(1)).tryPickTerritoryGroupByName(client, "GroupB");
    }

    @Test
    public void test_placeUnits() throws RemoteException, NotBoundException {
        StringBuilder inputs = new StringBuilder();
        inputs.append("\n");
        inputs.append("Gondor all\n");
        inputs.append("Gondor 16\n");
        inputs.append("Mordor 2\n");
        inputs.append("Hogwarts 1\n");
        inputs.append("Mordor 2\n");
        inputs.append("Hogwarts 1\n");
        StringBuilder outputs = new StringBuilder();
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append("  0 units in Gondor (next to: Mordor)\n");
        outputs.append("  0 units in Mordor (next to: Gondor, Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("20 units remaining, please input: <Territory> <units>\n");
        outputs.append("Invalid input: \n");
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append("  0 units in Gondor (next to: Mordor)\n");
        outputs.append("  0 units in Mordor (next to: Gondor, Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("20 units remaining, please input: <Territory> <units>\n");
        outputs.append("Invalid input: Gondor all\n");
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append("  0 units in Gondor (next to: Mordor)\n");
        outputs.append("  0 units in Mordor (next to: Gondor, Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("20 units remaining, please input: <Territory> <units>\n");
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append(" 16 units in Gondor (next to: Mordor)\n");
        outputs.append("  0 units in Mordor (next to: Gondor, Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("4 units remaining, please input: <Territory> <units>\n");
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append(" 16 units in Gondor (next to: Mordor)\n");
        outputs.append("  2 units in Mordor (next to: Gondor, Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("2 units remaining, please input: <Territory> <units>\n");
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append(" 16 units in Gondor (next to: Mordor)\n");
        outputs.append("  2 units in Mordor (next to: Gondor, Hogwarts)\n");
        outputs.append("  1 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("1 units remaining, please input: <Territory> <units>\n");
        outputs.append("Invalid input: remaining units is insufficient\n");
        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append(" 16 units in Gondor (next to: Mordor)\n");
        outputs.append("  2 units in Mordor (next to: Gondor, Hogwarts)\n");
        outputs.append("  1 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("1 units remaining, please input: <Territory> <units>\n");
        outputs.append("Placement finished.\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Player pRed = mock(Player.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tGondor = mock(Territory.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);

        Client client = createMockedClient(mockServer, inputs.toString(), bytes);

        // Setup mockGameMap
        when(tGondor.getName()).thenReturn("Gondor");
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tGondor.getUnits()).thenReturn(0, 0, 0, 16);
        when(tMordor.getUnits()).thenReturn(0, 0, 0, 0, 2);
        when(tHogwarts.getUnits()).thenReturn(0, 0, 0, 0, 0, 1);
        LinkedList<Territory> tGondorNeighbors = new LinkedList<Territory>() {
            {
                add(tMordor);
            }
        };
        LinkedList<Territory> tMordorNeighbors = new LinkedList<Territory>() {
            {
                add(tGondor);
                add(tHogwarts);
            }
        };
        LinkedList<Territory> tHogwartsNeighbors = new LinkedList<Territory>() {
            {
                add(tMordor);
            }
        };
        when(mockMap.getNeighbors(tGondor.getName())).thenReturn(tGondorNeighbors);
        when(mockMap.getNeighbors(tMordor.getName())).thenReturn(tMordorNeighbors);
        when(mockMap.getNeighbors(tHogwarts.getName())).thenReturn(tHogwartsNeighbors);

        // Setup Player
        when(pRed.getName()).thenReturn("Red");
        LinkedList<Territory> pRedTerritories = new LinkedList<Territory>() {
            {
                add(tGondor);
                add(tMordor);
                add(tHogwarts);
            }
        };
        when(pRed.getTerritories()).thenReturn(pRedTerritories);
        when(pRed.getTotalUnits()).thenReturn(0, 0, 0, 16, 18, 19, 19, 20);
        // Setup Server
        when(mockServer.getGameMap()).thenReturn(mockMap);
        when(mockServer.getSelfStatus(client)).thenReturn(pRed);
        when(mockServer.getInitUints()).thenReturn(20);
        when(mockServer.tryPlaceUnitsOn(client, "Gondor", 16)).thenReturn(null);
        when(mockServer.tryPlaceUnitsOn(client, "Mordor", 2)).thenReturn(null, "remaining units is insufficient");
        when(mockServer.tryPlaceUnitsOn(client, "Hogwarts", 1)).thenReturn(null);

        // Test
        bytes.reset();
        assertDoesNotThrow(() -> client.placeUnits());
        assertEquals(outputs.toString(), bytes.toString());

        // Verify
        verify(mockServer, times(1)).tryPlaceUnitsOn(client, "Gondor", 16);
        verify(mockServer, times(2)).tryPlaceUnitsOn(client, "Mordor", 2);
        verify(mockServer, times(2)).tryPlaceUnitsOn(client, "Hogwarts", 1);
        verify(mockServer, times(8)).getInitUints();
        verify(pRed, times(8)).getTotalUnits();
    }

    @Test
    public void test_requestContinueGame() throws RemoteException, NotBoundException, InterruptedException {
        StringBuilder outputs = new StringBuilder();
        outputs.append("Waiting for other players...\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);

        Client client = createMockedClient(mockServer, "", bytes);
        // Setup mockServer
        doNothing().when(mockServer).doCommitOrder(client);
        // Test
        bytes.reset();
        assertDoesNotThrow(() -> client.requestContinueGame());
        assertEquals(outputs.toString(), bytes.toString());
        // Verify
        verify(mockServer, times(1)).doCommitOrder(client);
    }

    @Test
    public void test_parseOrder() throws RemoteException, NotBoundException, InterruptedException {
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
        outputs.append(
                "You are the Red player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append("Invalid input: undefined order\n");
        outputs.append(
                "You are the Red player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append(
                "You are the Red player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
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

        // Tes
        bytes.reset();
        assertDoesNotThrow(() -> client.playOneTurn());
        assertEquals(outputs.toString(), bytes.toString());

        // Verify
        verify(mockServer, atLeastOnce()).getGameMap();
        verify(mockServer, atLeastOnce()).getSelfStatus(client);
        verify(mockServer, atLeastOnce()).tryMoveOrder(client, "Mordor", "Hogwarts", 5);
        verify(mockServer, atLeastOnce()).doCommitOrder(client);
    }

    @Test
    public void test_start_lose() throws RemoteException, NotBoundException, InterruptedException, IOException {
        StringBuilder inputs = new StringBuilder();
        inputs.append("Green\n");
        inputs.append("GroupA\n");
        inputs.append("Hogwarts 20\n");
        inputs.append("Move Hogwarts Mordor 5\n");
        inputs.append("D\n");
        inputs.append("Attack Hogwarts Mordor 5\n");
        inputs.append("D\n");
        StringBuilder outputs = new StringBuilder();
        outputs.append("Please name your Player:\n");
        outputs.append("Joined a RiskGame as Player Green\n");
        outputs.append("GroupA player:\n");
        outputs.append("--------------\n");
        outputs.append("  0 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("Please choose your Territory Group:\n");
        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append("  0 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("20 units remaining, please input: <Territory> <units>\n");
        outputs.append("Placement finished.\n");
        outputs.append("Waiting for other players...\n");
        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append("  0 units in Mordor (next to: Hogwarts)\n");
        outputs.append(" 20 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append("Waiting for other players...\n");
        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append("  5 units in Mordor (next to: Hogwarts)\n");
        outputs.append(" 15 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append("Waiting for other players...\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        Player groupA = mock(Player.class);
        Player pGreen = mock(Player.class);

        Client client = createMockedClient(mockServer, inputs.toString(), bytes);

        // Setup mockMap
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tMordor.getOwner()).thenReturn(groupA, pGreen);
        when(tHogwarts.getOwner()).thenReturn(groupA, pGreen);
        when(tMordor.getUnits()).thenReturn(0, 0, 0, 5);
        when(tHogwarts.getUnits()).thenReturn(0, 0, 20, 15);
        LinkedList<Territory> allTerritories = new LinkedList<Territory>() {
            {
                add(tMordor);
                add(tHogwarts);
            }
        };
        when(mockMap.getTerritories()).thenReturn(allTerritories);
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

        // Setup mockPlayer
        when(groupA.getName()).thenReturn("GroupA");
        when(pGreen.getName()).thenReturn("Green");
        when(groupA.getTerritories()).thenReturn(allTerritories);
        when(pGreen.getTerritories()).thenReturn(allTerritories);
        when(pGreen.getTotalUnits()).thenReturn(0, 20);
        when(pGreen.isLose()).thenReturn(false, false, true);

        // Setup mockServer
        when(mockServer.tryRegisterClient(client, "Green")).thenReturn(null);
        when(mockServer.getGameMap()).thenReturn(mockMap);
        when(mockServer.getSelfStatus(client)).thenReturn(pGreen);
        when(mockServer.getInitUints()).thenReturn(20);
        when(mockServer.isGameOver()).thenReturn(false);

        // Test
        bytes.reset();
        assertDoesNotThrow(() -> client.start());
        assertEquals(outputs.toString(), bytes.toString());

        // Verify
        verify(mockServer, times(1)).tryRegisterClient(client, "Green");
        verify(mockServer, atLeastOnce()).getGameMap();
        verify(mockServer, atLeastOnce()).getSelfStatus(client);
        verify(mockServer, atLeastOnce()).getInitUints();
        verify(mockServer, times(2)).isGameOver();
        verify(pGreen, times(3)).isLose();
    }

    @Test
    public void test_start_win() throws RemoteException, NotBoundException, InterruptedException, IOException {
        StringBuilder inputs = new StringBuilder();
        inputs.append("Green\n");
        inputs.append("GroupA\n");
        inputs.append("Hogwarts 20\n");
        inputs.append("Move Hogwarts Mordor 5\n");
        inputs.append("D\n");
        inputs.append("Attack Hogwarts Mordor 5\n");
        inputs.append("D\n");
        StringBuilder outputs = new StringBuilder();
        outputs.append("Please name your Player:\n");
        outputs.append("Joined a RiskGame as Player Green\n");
        outputs.append("GroupA player:\n");
        outputs.append("--------------\n");
        outputs.append("  0 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("Please choose your Territory Group:\n");
        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append("  0 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append("20 units remaining, please input: <Territory> <units>\n");
        outputs.append("Placement finished.\n");
        outputs.append("Waiting for other players...\n");
        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append("  0 units in Mordor (next to: Hogwarts)\n");
        outputs.append(" 20 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append("Waiting for other players...\n");
        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append("  5 units in Mordor (next to: Hogwarts)\n");
        outputs.append(" 15 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append(
                "You are the Green player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one\n");
        outputs.append("Waiting for other players...\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        Player groupA = mock(Player.class);
        Player pGreen = mock(Player.class);

        Client client = createMockedClient(mockServer, inputs.toString(), bytes);

        // Setup mockMap
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tMordor.getOwner()).thenReturn(groupA, pGreen);
        when(tHogwarts.getOwner()).thenReturn(groupA, pGreen);
        when(tMordor.getUnits()).thenReturn(0, 0, 0, 5);
        when(tHogwarts.getUnits()).thenReturn(0, 0, 20, 15);
        LinkedList<Territory> allTerritories = new LinkedList<Territory>() {
            {
                add(tMordor);
                add(tHogwarts);
            }
        };
        when(mockMap.getTerritories()).thenReturn(allTerritories);
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

        // Setup mockPlayer
        when(groupA.getName()).thenReturn("GroupA");
        when(pGreen.getName()).thenReturn("Green");
        when(groupA.getTerritories()).thenReturn(allTerritories);
        when(pGreen.getTerritories()).thenReturn(allTerritories);
        when(pGreen.getTotalUnits()).thenReturn(0, 20);
        when(pGreen.isLose()).thenReturn(false, false, false);

        // Setup mockServer
        when(mockServer.tryRegisterClient(client, "Green")).thenReturn(null);
        when(mockServer.getGameMap()).thenReturn(mockMap);
        when(mockServer.getSelfStatus(client)).thenReturn(pGreen);
        when(mockServer.getInitUints()).thenReturn(20);
        when(mockServer.isGameOver()).thenReturn(false, false, true);

        // Test
        bytes.reset();
        assertDoesNotThrow(() -> client.start());
        assertEquals(outputs.toString(), bytes.toString());

        // Verify
        verify(mockServer, times(1)).tryRegisterClient(client, "Green");
        verify(mockServer, atLeastOnce()).getGameMap();
        verify(mockServer, atLeastOnce()).getSelfStatus(client);
        verify(mockServer, atLeastOnce()).getInitUints();
        verify(mockServer, times(3)).isGameOver();
        verify(pGreen, times(3)).isLose();
    }

    @Test
    public void test_ping() throws RemoteException, NotBoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Client localClient = createMockedClient(mockServer, "", bytes);
        assertDoesNotThrow(() -> localClient.ping());

        RemoteClient aliveClient = mock(RemoteClient.class);
        RemoteClient lostClient = mock(RemoteClient.class);
        doNothing().when(aliveClient).ping();
        doThrow(RemoteException.class).when(lostClient).ping();
        assertDoesNotThrow(() -> aliveClient.ping());
        assertThrows(RemoteException.class, () -> lostClient.ping());
    }

    @Test
    public void test_doDisplay() throws RemoteException, NotBoundException {
        StringBuilder outputs = new StringBuilder();
        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append("  0 units in Mordor (next to: Hogwarts)\n");
        outputs.append("  0 units in Hogwarts (next to: Mordor)\n");
        outputs.append("\n");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        Player pGreen = mock(Player.class);

        Client client = createMockedClient(mockServer, "", bytes);

        // Setup mockMap
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tMordor.getOwner()).thenReturn(pGreen);
        when(tHogwarts.getOwner()).thenReturn(pGreen);
        when(tMordor.getUnits()).thenReturn(0);
        when(tHogwarts.getUnits()).thenReturn(0);
        LinkedList<Territory> allTerritories = new LinkedList<Territory>() {
            {
                add(tMordor);
                add(tHogwarts);
            }
        };
        when(mockMap.getTerritories()).thenReturn(allTerritories);
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

        // Setup mockPlayer
        when(pGreen.getName()).thenReturn("Green");
        when(pGreen.getTerritories()).thenReturn(allTerritories);

        // Test
        bytes.reset();
        assertDoesNotThrow(() -> client.doDisplay(mockMap));
        assertEquals(outputs.toString(), bytes.toString());
    }

    @Test
    public void test_doDisplay_msg() throws RemoteException, NotBoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        RemoteServer mockServer = mock(RemoteServer.class);
        Client client = createMockedClient(mockServer, "", bytes);
        bytes.reset();
        assertDoesNotThrow(() -> client.doDisplay(""));
        assertEquals("\n", bytes.toString());
    }
}
