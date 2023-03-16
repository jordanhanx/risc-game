package edu.duke.ece651.team7.server;

import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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
  protected Map<RemoteClient, Player> inGameClients;
  /**
   * A set of clients that are watching the game as spectators.
   */
  protected Set<RemoteClient> watchingClients;
  /**
   * The risk game map.
   */
  protected GameMap map;
  /**
   * The order executor used for executing player orders.
   */
  protected OrderExecuter ox;

  protected Registry registry;
  protected CountDownLatch commitSignal;
  protected CountDownLatch returnSignal;

  /**
   * Constructs a new `Server` with the specified output stream, number of
   * players, and initial units.
   *
   * @param out        the output stream to use for logging
   * @param numPlayers the number of players in the game
   * @param initUnits  the initial number of units each player has
   * @throws RemoteException if a remote error occurs
   */
  public Server(PrintStream out, int port, int numPlayers, int initUnits) throws RemoteException {
    this.out = out;
    if (numPlayers < 2) {
      throw new IllegalArgumentException("numPlayers cannot be less than 2");
    }
    if (initUnits < 1) {
      throw new IllegalArgumentException("initUnits cannot be less than 1");
    }
    this.numPlayers = numPlayers;
    this.initialUnit = initUnits;
    initClientsSet();
    initGameMap();
    bindGameOnPort(port);
    setupCountDownLatches(numPlayers);
  }

  /**
   * Initializes all clients map and sets.
   */
  protected void initClientsSet() {
    this.inGameClients = new ConcurrentHashMap<RemoteClient, Player>();
    this.watchingClients = new HashSet<RemoteClient>();
    // this.watchingClients = ConcurrentHashMap.newKeySet();
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
   * Create a new registry and bind the Server on a specific port in
   *
   * @param port the port to listen on
   * @throws RemoteException if a remote error occurs
   */
  protected void bindGameOnPort(int port) throws RemoteException {
    this.registry = LocateRegistry.createRegistry(port);
    registry.rebind("RiscGameServer", this);
    out.println("RiscGameServer is ready to accept connections");
  }

  /**
   * Unbind the Server from registry.
   *
   * @throws RemoteException   if a remote error occurs
   * @throws NotBoundException
   */
  protected void unbindGame() throws RemoteException, NotBoundException {
    registry.unbind("RiscGameServer");
    out.println("RiscGameServer is Down.");
  }

  /**
   * Setup count numbers for each turn.
   * 
   * @param numInGame the number of in-game clients
   */
  protected void setupCountDownLatches(int numInGame) {
    this.commitSignal = new CountDownLatch(numInGame);
    this.returnSignal = new CountDownLatch(1);
  }

  /**
   * Starts the game server on the specified port.
   *
   * @throws RemoteException      if a remote error occurs
   * @throws InterruptedException
   * @throws NotBoundException
   */
  public void start() throws RemoteException, InterruptedException, NotBoundException {
    while (true) {
      commitSignal.await();
      // do all combats here
      removeLostPlayer(); // move lost Clients from inGameClients to watchingClients
      if (isGameOver()) {
        doEndGame();
        break;
      } else {
        notifyAllWatchersDisplay();
      }
      returnSignal.countDown();
      setupCountDownLatches(inGameClients.size());
    }
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
  public synchronized GameMap getGameMap() {
    return map;
  }

  @Override
  public synchronized Player getSelfStatus(RemoteClient client) throws RemoteException {
    return inGameClients.get(client);
  }

  @Override
  public synchronized int getInitUints() throws RemoteException {
    return initialUnit;
  }

  @Override
  public synchronized String tryPickTerritoryGroupByName(RemoteClient client, String groupName) throws RemoteException {
    // TODO to be completed
    return "To be completed";
  }

  @Override
  public synchronized String tryPlaceUnitsOn(RemoteClient client, String territory, int units) throws RemoteException {
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
  public synchronized String tryMoveOrder(RemoteClient client, String src, String dest, int units)
      throws RemoteException {
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
  public synchronized String tryAttackOrder(RemoteClient client, String src, String dest, int units)
      throws RemoteException {
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

  @Override
  public void doCommitOrder(RemoteClient client) throws RemoteException, InterruptedException {
    pingInGameClients();
    commitSignal.countDown();
    returnSignal.await();
  }

  @Override
  public synchronized boolean isGameOver() throws RemoteException {
    return (inGameClients.size() == 1) ? true : false;
  }

  /**
   * Find last in-game Client, the winner.
   * 
   * @return the last RemoteClient otherwise null.
   * @throws RemoteException if a remote error occurs
   */
  RemoteClient getWinClient() throws RemoteException {
    if (isGameOver()) {
      for (RemoteClient c : inGameClients.keySet()) {
        if (!inGameClients.get(c).isLose()) {
          return c;
        }
      }
    }
    return null;
  }

  /**
   * Checks the network connections to all in-game Clients.
   * If an in-game Client is disconnected, the game will exit.
   * 
   * @throws RemoteException if a remote error occurs
   */
  void pingInGameClients() throws RemoteException {
    for (RemoteClient c : inGameClients.keySet()) {
      c.ping();
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
  void notifyAllWatchersDisplay() {
    for (RemoteClient watcher : watchingClients) {
      try {
        watcher.doDisplay(map);
      } catch (RemoteException e) {
        watchingClients.remove(watcher);
      }
    }
  }

  /**
   * End the game.
   * 
   * @throws RemoteException   if a remote error occurs
   * @throws NotBoundException
   */
  void doEndGame() throws RemoteException, NotBoundException {
    for (RemoteClient winner : inGameClients.keySet()) {
      winner.doDisplay(map);
      winner.doDisplay("You Win!");
      for (RemoteClient watcher : watchingClients) {
        try {
          watcher.doDisplay(map);
          watcher.doDisplay("Winner is Player " + inGameClients.get(winner).getName());
        } catch (RemoteException e) {
          watchingClients.remove(watcher);
        }
      }
    }
    inGameClients.clear();
    watchingClients.clear();
    unbindGame();
  }
}
