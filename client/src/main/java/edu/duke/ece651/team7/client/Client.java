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

public class Client extends UnicastRemoteObject implements RemoteClient {
  private final BufferedReader inputReader;
  private final PrintStream out;
  private final RemoteServer server;
  private GameMap map;
  private MapTextView view;

  public Client(String host, int port, BufferedReader in, PrintStream out)
      throws RemoteException, NotBoundException {
    super();
    this.inputReader = in;
    this.out = out;
    this.server = (RemoteServer) LocateRegistry.getRegistry(host, port).lookup("RiscGameServer");
    out.println("Connected to " + host + ":" + port + " successfully.");
  }

  public String readUserInput(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if (s == null) {
      throw new EOFException("inputReader.readLine() return null, EOF!");
    }
    return s;
  }

  public void initPlayer() throws RemoteException, IOException {
    out.println("sent GameMap request to server");
    String name = readUserInput("Please name your Player:");
    String msg = server.tryRegisterClient(this, name);
    if (msg != null) {
      throw new RuntimeException("Failed to join the game, reason:" + msg);
    }
    out.println("Joined a RiskGame as Player:" + name);
  }

  public void start() throws RemoteException, InterruptedException, IOException {
    initPlayer();
    out.println("sent GameMap request to server");
    map = server.getGameMap();
    out.println("get GameMap from server");
    view.display(out, map);
    // while (true) {
    playOneTurn();
    // }
  }

  public void playOneTurn() throws RemoteException {
  }

  @Override
  public void forceQuit(String reason) throws RemoteException {
    out.println("Server has ended the game.");
    System.exit(0);
  }
}
