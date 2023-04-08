package edu.duke.ece651.team7.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BrokenBarrierException;

import edu.duke.ece651.team7.shared.*;

/**
 * The Client class represents a player in the Risk game. It connects to the
 * server and allows the player to interact with the game.
 */
public class TextClient extends UnicastRemoteObject implements RemoteClient {
  /**
   * The BufferedReader used for reading user input.
   */
  protected final BufferedReader inputReader;
  /**
   * The PrintStream used for outputting information to the user.
   */
  protected final PrintStream out;
  /**
   * The username of the client
   */
  private String username;
  /**
   * The RemoteServer used for communicating with the server.
   */
  // protected RemoteServer server;
  protected RemoteGame server;
  /**
   * The MapTextView used for displaying the game map to the user.
   */
  protected MapTextView view;

  /**
   * Constructs a Client object and connects it to the RemoteServer.
   *
   * @param host the hostname of the server
   * @param port the port number of the server
   * @param in   the BufferedReader used for reading user input
   * @param out  the PrintStream used for outputting information to the user
   * @throws RemoteException   if there is an error with the remote connection
   * @throws NotBoundException if the server is not bound
   */
  public TextClient(String host, int port, String gamename, BufferedReader in, PrintStream out)
      throws RemoteException, NotBoundException {
    super();
    this.inputReader = in;
    this.out = out;
    this.username = "default";
    connectRemoteServer(host, port, gamename);
    this.view = new MapTextView(out);
  }

  /**
   * Connects the Client object to the RemoteServer.
   *
   * @param host the hostname of the server
   * @param port the port number of the server
   * @throws RemoteException   if there is an error with the remote connection
   * @throws NotBoundException if the server is not bound
   */
  protected void connectRemoteServer(String host, int port, String gamename) throws RemoteException, NotBoundException {
    this.server = (RemoteGame) LocateRegistry.getRegistry(host, port).lookup(gamename);
    out.println("Connected to " + host + ":" + port + " successfully.");
  }

  /**
   * Prompts the user for input and returns the input as a String.
   *
   * @param prompt the prompt to display to the user
   * @return the user's input as a String
   * @throws IOException if there is an error reading user input
   */
  public String readUserInput(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if (s == null) {
      throw new EOFException("inputReader.readLine() return null, EOF!");
    }
    return s;
  }

  /**
   * Registers the player with the server.
   *
   * @throws RemoteException if there is an error with the remote connection
   * @throws IOException     if there is an error reading user input
   */
  public void registerPlayer() throws RemoteException, IOException {
    while (true) {
      username = readUserInput("Please name your Player:");
      String msg = server.tryRegisterClient(username, this);
      if (msg == null) {
        out.println("Joined a RiskGame as Player " + username);
        break;
      } else {
        out.println("Failed to register: " + msg);
      }
    }
  }

  /**
   * Picks one group of Territories.
   *
   * @throws RemoteException if there is an error with the remote connection
   * @throws IOException     if there is an error reading user input
   */
  public void pickTerritoryGroup() throws RemoteException, IOException {
    while (true) {
      view.display(server.getGameMap());
      String groupName = readUserInput("Please choose your Territory Group:");
      String msg = server.tryPickTerritoryGroupByName(username, groupName);
      if (msg == null) {
        break;
      } else {
        out.println("Invalid input: " + msg);
      }
    }
  }

  /**
   * Places specific number of units on each Territory.
   *
   * @throws RemoteException if there is an error with the remote connection
   * @throws IOException     if there is an error reading user input
   */
  public void placeUnits() throws RemoteException, IOException {
    for (int remaining = server.getGameInitUnits()
        - server.getSelfStatus(username).getTotalUnits(); remaining > 0; remaining = server.getGameInitUnits()
            - server.getSelfStatus(username).getTotalUnits()) {
      view.display(server.getGameMap(), server.getSelfStatus(username));
      String placement = readUserInput(remaining + " units remaining, please input: <Territory> <units>");
      if (placement.matches("^\\w+\\s+\\d+$")) {
        String[] substr = placement.split("\\s+");
        String msg = server.tryPlaceUnitsOn(username, substr[0], Integer.parseInt(substr[1]));
        if (msg != null) {
          out.println("Invalid input: " + msg);
        }
      } else {
        out.println("Invalid input: " + placement);
      }
    }
    out.println("Placement finished.");

  }

  /**
   * Requests Server to continue the Game until all clients are ready.
   *
   * @throws RemoteException        if there is an error with the remote
   *                                connection
   * @throws InterruptedException   if there is an error while waiting for the
   *                                server to respond
   * @throws BrokenBarrierException if the barrier that is in a broken state
   */
  public void requestContinueGame() throws RemoteException, InterruptedException, BrokenBarrierException {
    out.println("Waiting for other players...");
    server.doCommitOrder(username);
  }

  /**
   * Parses the user's input to determine what action to take.
   *
   * @param input the user's input
   * @throws RemoteException if there is an error with the remote connection
   */
  public void parseOrder(String input) throws RemoteException {
    String response = null;
    if (input.matches("^(?i)(M(?:ove)?)\\s+\\w+\\s+\\w+\\s+\\d+$")) {
      String[] substr = input.split("\\s+");
      response = server.tryMoveOrder(username, substr[1], substr[2], Integer.parseInt(substr[3]));
    } else if (input.matches("^(?i)(A(?:ttack)?)\\s+\\w+\\s+\\w+\\s+\\d+$")) {
      String[] substr = input.split("\\s+");
      response = server.tryAttackOrder(username, substr[1], substr[2], Integer.parseInt(substr[3]));
    } else {
      response = "undefined order";
    }
    if (response != null) {
      throw new IllegalArgumentException(response);
    }
  }

  /**
   * Plays one turn of the game, allowing the user to enter orders until they
   * choose to end their turn.
   *
   * @throws RemoteException        if there is an error communicating with the
   *                                server
   * @throws InterruptedException   if the thread is interrupted while waiting for
   *                                a
   *                                response from the server
   * @throws IOException            if there is an error reading input from the
   *                                user
   * @throws BrokenBarrierException if the barrier that is in a broken state
   */
  public void playOneTurn() throws RemoteException, InterruptedException, IOException, BrokenBarrierException {
    view.display(server.getGameMap());
    while (true) {
      String input = readUserInput(
          "You are the " + server.getSelfStatus(username).getName()
              + " player, what would you like to do?\n(M)ove <from> <to> <units>\n(A)ttack <from> <to> <units>\n(D)one");
      if (input.matches("^(?i)(D(?:one)?)$")) {
        requestContinueGame();
        break;
      } else {
        try {
          parseOrder(input);
        } catch (IllegalArgumentException e) {
          out.println("Invalid input: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Registers the player with the server, plays the game until it is over, and
   * prints a message to the user indicating whether they won or lost.
   *
   * @throws RemoteException        if there is an error communicating with the
   *                                server
   * @throws InterruptedException   if the thread is interrupted while waiting for
   *                                a
   *                                response from the server
   * @throws IOException            if there is an error reading input from the
   *                                user
   * @throws BrokenBarrierException if the barrier that is in a broken state
   */
  public void start() throws RemoteException, InterruptedException, IOException, BrokenBarrierException {
    registerPlayer(); // registers self on the game server
    pickTerritoryGroup(); // picks a group of territories
    requestContinueGame(); // waits for other players
    placeUnits(); // places units on self territories
    requestContinueGame(); // waits for other players
    while (!server.isGameOver() && !server.getSelfStatus(username).isLose()) {
      playOneTurn();
    }
  }

  @Override
  public void ping() throws RemoteException {
  }

  @Override
  public void doDisplay(GameMap map) throws RemoteException {
    view.display(map);
  }

  @Override
  public void doDisplay(String msg) throws RemoteException {
    view.display(msg);
  }

  @Override
  public void close() throws RemoteException {
    UnicastRemoteObject.unexportObject(this, true);
  }
}
