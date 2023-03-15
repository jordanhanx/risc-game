package edu.duke.ece651.team7.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import edu.duke.ece651.team7.shared.*;

/**
 * The Client class represents a player in the Risk game. It connects to the
 * server and allows the player to interact with the game.
 */
public class Client extends UnicastRemoteObject implements RemoteClient {
  /**
   * The BufferedReader used for reading user input.
   */
  protected final BufferedReader inputReader;
  /**
   * The PrintStream used for outputting information to the user.
   */
  protected final PrintStream out;
  /**
   * The RemoteServer used for communicating with the server.
   */
  protected RemoteServer server;
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
  public Client(String host, int port, BufferedReader in, PrintStream out)
      throws RemoteException, NotBoundException {
    super();
    this.inputReader = in;
    this.out = out;
    connectRemoteServer(host, port);
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
  protected void connectRemoteServer(String host, int port) throws RemoteException, NotBoundException {
    this.server = (RemoteServer) LocateRegistry.getRegistry(host, port).lookup("RiscGameServer");
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
   * @throws RemoteException      if there is an error with the remote connection
   * @throws InterruptedException if there is an error while waiting for the
   *                              server to respond
   * @throws IOException          if there is an error reading user input
   */
  public void registerPlayer() throws RemoteException, IOException {
    String name = readUserInput("Please name your Player:");
    String msg = server.tryRegisterClient(this, name);
    if (msg != null) {
      throw new IllegalArgumentException("Failed to register: " + msg);
    }
    out.println("Joined a RiskGame as Player: " + name);
  }

  public void pickTerritoryGroup() throws RemoteException, IOException {
    while (true) {
      view.display(server.getGameMap());
      String groupName = readUserInput("Please choose your Territory Group:");
      String msg = server.tryPickTerritoryGroupByName(this, groupName);
      if (msg == null) {
        break;
      } else {
        out.println("Invalid input: " + msg);
      }
    }
  }

  public void placeUnits() throws RemoteException, IOException {
    for (Territory t : server.getSelfStatus(this).getTerritories()) {
      int remainUnits = server.getInitUints() - server.getSelfStatus(this).getTotalUnits();
      while (remainUnits > 0) {
        try {
          int units = Integer.parseInt(readUserInput(
              "How many units do you place on " + t.getName() + " ? (remaining " + remainUnits + " units)"));
          String msg = server.tryPlaceUnitsOn(this, t.getName(), units);
          if (msg == null) {
            break;
          } else {
            out.println("Invalid input: " + msg);
          }
        } catch (RuntimeException e) {
          out.println("Error: " + e.getMessage());
        }
      }
    }
  }

  public void requestContinueGame() throws RemoteException, InterruptedException {
    out.println("Waiting for other players...");
    server.doCommitOrder(this);
  }

  /**
   * Parses the user's input to determine what action to take.
   *
   * @param input the user's input
   * @throws RemoteException      if there is an error with the remote connection
   * @throws InterruptedException if there is an error while waiting for the
   *                              server to respond
   * @throws IOException          if there is an error reading user input
   */
  public void parseOrder(String input) throws RemoteException, InterruptedException, IOException {
    String response = null;
    if (input.matches("^(?i)(M(?:ove)?)\\s+\\w+\\s+\\w+\\s+\\d+$")) {
      String[] substr = input.split("\\s+");
      response = server.tryMoveOrder(this, substr[1], substr[2], Integer.parseInt(substr[3]));
    } else if (input.matches("^(?i)(A(?:ttack)?)\\s+\\w+\\s+\\w+\\s+\\d+$")) {
      String[] substr = input.split("\\s+");
      response = server.tryAttackOrder(this, substr[1], substr[2], Integer.parseInt(substr[3]));
    } else {
      response = "undefined order";
    }
    if (response != null) {
      throw new IllegalArgumentException("Invalid input: " + response);
    }
  }

  /**
   * Plays one turn of the game, allowing the user to enter orders until they
   * choose to end their turn.
   *
   * @throws RemoteException      if there is an error communicating with the
   *                              server
   * @throws InterruptedException if the thread is interrupted while waiting for a
   *                              response from the server
   * @throws IOException          if there is an error reading input from the user
   */
  public void playOneTurn() throws RemoteException, InterruptedException, IOException {
    view.display(server.getGameMap());
    while (true) {
      try {
        String input = readUserInput(
            "You are the " + server.getSelfStatus(this).getName()
                + " player, what would you like to do?\n(M)ove\n(A)ttack\n(D)one");
        if (input.matches("^(?i)(D(?:one)?)$")) {
          requestContinueGame();
          break;
        } else {
          parseOrder(input);
        }
      } catch (RuntimeException e) {
        out.println(e.getMessage());
      }
    }
  }

  public void watchOneTurn() throws RemoteException, InterruptedException {
    view.display(server.getGameMap());
    requestContinueGame();
  }

  /**
   * Checks if the game is over and prints a message to the user if it is. Returns
   * true if the game is over, false otherwise.
   *
   * @return true if the game is over, false otherwise
   * @throws RemoteException if there is an error communicating with the server
   */
  public boolean checkIfGameOver() throws RemoteException {
    if (server.isGameOver()) {
      Player winner = server.getWinner();
      if (winner.equals(server.getSelfStatus(this))) {
        out.println("Congrats! YOU WIN!");
      } else {
        out.println("Game over! " + winner.getName() + " is the winner.");
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Registers the player with the server, plays the game until it is over, and
   * prints a message to the user indicating whether they won or lost.
   *
   * @throws RemoteException      if there is an error communicating with the
   *                              server
   * @throws InterruptedException if the thread is interrupted while waiting for a
   *                              response from the server
   * @throws IOException          if there is an error reading input from the user
   */
  public void start() throws RemoteException, InterruptedException, IOException {
    registerPlayer();
    pickTerritoryGroup();
    placeUnits();
    requestContinueGame();
    while (!checkIfGameOver()) {
      if (!server.getSelfStatus(this).isLose()) {
        playOneTurn();
      } else {
        watchOneTurn();
      }
    }
  }
}
