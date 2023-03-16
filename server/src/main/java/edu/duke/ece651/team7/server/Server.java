package edu.duke.ece651.team7.server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.HashSet;

import edu.duke.ece651.team7.shared.*;

/**
 * The `Server` class implements a game server for a Risk-like game.
 */
public class Server extends UnicastRemoteObject implements RemoteServer {
  /**
   * The output stream used for logging.
   */
  protected final PrintStream out;
  /**
   * The number of players in the game.
   */
  private final int numPlayers;
  /**
   * The initial number of units each player has.
   */
  private final int initialUnit;
  /**
   * A table of connected clients and their corresponding players in the game.
   */
  protected HashMap<RemoteClient, Player> inGameClients;
  /**
   * A set of clients that are ready in one turn.
   */
  protected HashSet<RemoteClient> readyClients;
  /**
   * A set of clients that are watching the game as spectators.
   */
  protected HashSet<RemoteClient> watchingClients;
  /**
   * The risk game map.
   */
  protected GameMap map;
  /**
   * The order executor used for executing player orders.
   */
  protected OrderExecuter ox;

  /**
   * Constructs a new `Server` with the specified output stream, number of
   * players, and initial units.
   *
   * @param out        the output stream to use for logging
   * @param numPlayers the number of players in the game
   * @param initUnits  the initial number of units each player has
   * @throws RemoteException if a remote error occurs
   */
  public Server(PrintStream out, int numPlayers, int initUnits) throws RemoteException {
    this.out = out;
    if (numPlayers < 2) {
      throw new IllegalArgumentException("numPlayers cannot be less than 2");
    }
    if (initUnits < 1) {
      throw new IllegalArgumentException("initUnits cannot be less than 1");
    }
    this.numPlayers = numPlayers;
    this.initialUnit = initUnits;
    this.inGameClients = new HashMap<RemoteClient, Player>();
    this.watchingClients = new HashSet<RemoteClient>();
    this.watchingClients = new HashSet<RemoteClient>();
    initGameMap();
  }

  /**
   * Initializes the game map for the current game.
   */
  protected void initGameMap() {
    this.map = new TextMapFactory().createMap();
    this.ox = new OrderExecuter(map.getTerritories());
    out.println("GameMap initialized");
  }

  /**
   * Listens on the specified port for incoming connections.
   *
   * @param port the port to listen on
   * @throws RemoteException if a remote error occurs
   */
  protected void listenOnPort(int port) throws RemoteException {
    LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
    out.println("RiscGameServer is ready to accept connections");
  }

  /**
   * Starts the game server on the specified port.
   *
   * @param port the port to listen on
   * @throws RemoteException if a remote error occurs
   */
  public void start(int port) throws RemoteException {
    listenOnPort(port);
  }

  @Override
  public synchronized String tryRegisterClient(RemoteClient newClient, String name) throws RemoteException {
    if (inGameClients.containsKey(newClient)) {
      return "Already joined, cannot join repeatly";
    } else if (inGameClients.size() >= numPlayers) {
      return "Clients are full";
    } else {
      Player p = new Player(name);
      inGameClients.put(newClient, p);
      out.println("Player " + p.getName() + " joined game. (" + inGameClients.size() + "/" + numPlayers + ")");
      return null;
    }
  }

  @Override
  public GameMap getGameMap() {
    return map;
  }

  @Override
  public Player getSelfStatus(RemoteClient client) throws RemoteException {
    return inGameClients.get(client);
  }

  @Override
  public int getInitUints() throws RemoteException {
    return initialUnit;
  }

  @Override
  public String tryPickTerritoryGroupByName(RemoteClient client, String groupName) throws RemoteException {
    // TODO to be completed
    return "To be completed";
  }

  @Override
  public String tryPlaceUnitsOn(RemoteClient client, String territory, int units) throws RemoteException {
    try {
      if (map.getTerritoryByName(territory).getOwner().equals(inGameClients.get(client))) {
        map.getTerritoryByName(territory).increaseUnits(units);
        return null;
      } else {
        return "Permission denied";
      }
    } catch (RuntimeException e) {
      return e.getMessage();
    }
  }

  @Override
  public String tryMoveOrder(RemoteClient client, String src, String dest, int units) throws RemoteException {
    String response = null;
    try {
      MoveOrder mo = new MoveOrder(inGameClients.get(client), map.getTerritoryByName(src),
          map.getTerritoryByName(dest), units);
      ox.doOneMove(mo);
    } catch (RuntimeException e) {
      response = e.getMessage();
    }
    return response;
  }

  @Override
  public String tryAttackOrder(RemoteClient client, String src, String dest, int units) throws RemoteException {
    String response = null;
    try {
      AttackOrder ao = new AttackOrder(inGameClients.get(client), map.getTerritoryByName(src),
          map.getTerritoryByName(dest), units);
      ox.pushCombat(ao);
    } catch (RuntimeException e) {
      response = e.getMessage();
    }
    return response;
  }

  /**
   * Checks the network connection to all in-game Clients.
   * If an in-game Client is disconnected, the game will exit.
   */
  void checkConnectionToClients() throws RemoteException {
    for (RemoteClient c : inGameClients.keySet()) {
      c.isAlive();
    }
  }

  /**
   * Removes any players who have lost from in-game set into watcher set.
   */
  void removeLostPlayer() {
    for (RemoteClient c : inGameClients.keySet()) {
      if (inGameClients.get(c).isLose()) {
        watchingClients.add(c);
        inGameClients.remove(c);
      }
    }
  }

  /**
   * Notifies all watching Clients of the current state of the game.
   */
  void notifyWatchingClients() {
    for (RemoteClient c : watchingClients) {
      try {
        c.doDisplay(map);
      } catch (RemoteException e) {
        watchingClients.remove(c);
      }
    }
  }

  @Override
  public synchronized void doCommitOrder(RemoteClient client) throws RemoteException, InterruptedException {
    checkConnectionToClients();
    readyClients.add(client);
    while (!readyClients.equals(inGameClients.keySet())) {
      wait();
    }
    notifyAll();
    readyClients.clear();
    removeLostPlayer();
    notifyWatchingClients();
  }

  @Override
  public boolean isGameOver() throws RemoteException {
    if (inGameClients.size() == 1) {
      return true;
    }
    return false;
  }

  /**
   * local method for testing
   */
  HashMap<RemoteClient, Player> getInGameClients() {
    return inGameClients;
  }

  /**
   * local method for testing
   */
  HashSet<RemoteClient> readyClients() {
    return readyClients;
  }

  /**
   * local method for testing
   */
  HashSet<RemoteClient> watchingClients() {
    return watchingClients;
  }
}
