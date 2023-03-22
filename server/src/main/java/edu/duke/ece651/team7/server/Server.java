package edu.duke.ece651.team7.server;

import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

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
  protected Map<RemoteClient, Player> watchingClients;
  /**
   * The risk game map.
   */
  protected GameMap map;
  /**
   * The order executor used for executing player orders.
   */
  protected OrderExecuter ox;
  /**
   * The countdownlatch used to block the server's main thread until all clients
   * commit.
   */
  protected CountDownLatch commitSignal;
  /**
   * The barrier used to block all clients until server is ready to next turn.
   */
  protected CyclicBarrier returnSignal;
  /**
   * The flag, true if game is bagun.
   */
  protected boolean isGameBegin = false;

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
    this.inGameClients = new HashMap<RemoteClient, Player>();
    this.watchingClients = new HashMap<RemoteClient, Player>();
  }

  /**
   * Initializes the game map for the current game.
   */
  protected void initGameMap() {
    this.map = new TextMapFactory().createPlayerMap(numPlayers);
    this.ox = new OrderExecuter(map);
    out.println("GameMap initialized");
  }

  /**
   * Create a new registry and bind the Server on a specific port in
   *
   * @param port the port to listen on
   * @throws RemoteException if a remote error occurs
   */
  protected void bindGameOnPort(int port) throws RemoteException {
    LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
    out.println("RiscGameServer is ready to accept connections");
  }

  /**
   * Setup count numbers for each turn.
   * 
   * @param numInGame the number of in-game clients
   */
  protected void setupCountDownLatches(int numInGame) {
    this.commitSignal = new CountDownLatch(numInGame);
    this.returnSignal = new CyclicBarrier(numInGame + 1);
  }

  /**
   * Starts the game server on the specified port.
   *
   * @throws RemoteException        if a remote error occurs
   * @throws InterruptedException   if a interrupt error occurs
   * @throws NotBoundException      if the server is not bound
   * @throws BrokenBarrierException if the barrier that is in a broken state
   */
  public void start() throws RemoteException, InterruptedException, NotBoundException, BrokenBarrierException {
    // Placement phase
    commitSignal.await();
    returnSignal.await(); // ready to next state
    setupCountDownLatches(inGameClients.size()); // reset locks
    // GamePlay phase
    while (true) {
      commitSignal.await();
      ox.doAllCombats();
      removeLostPlayer(); // move lost Clients from inGameClients to watchingClients
      returnSignal.await(); // ready to next state
      if (isGameOver()) {
        notifyAllClientsGameResult(); // send game result to all clients
        closeAllClients();
        UnicastRemoteObject.unexportObject(this, true); // close remoteObj
        out.println("RiscGameServer is closed");
        break;
      } else {
        notifyAllWatchers(); // send game status to all watching clients
        setupCountDownLatches(inGameClients.size()); // reset locks
      }
    }
  }

  @Override
  public synchronized String tryRegisterClient(RemoteClient newClient, String name) throws RemoteException {
    if (inGameClients.containsKey(newClient)) {
      return "Already joined, cannot join repeatly";
    } else if (isGameBegin) {
      return "the game is in progress";
    } else {
      Player p = new Player(name);
      if (inGameClients.containsValue(p)) {
        return "the Player " + name + " exists, please retry";
      }
      inGameClients.put(newClient, p);
      if (inGameClients.size() == numPlayers) {
        isGameBegin = true;
      }
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
    return inGameClients.containsKey(client) ? inGameClients.get(client) : watchingClients.get(client);
  }

  @Override
  public synchronized int getInitUints() throws RemoteException {
    return initialUnit;
  }

  @Override
  public synchronized String tryPickTerritoryGroupByName(RemoteClient client, String groupName) throws RemoteException {
    try {
      map.assignGroup(groupName, inGameClients.get(client));
      return null;
    } catch (RuntimeException e) {
      return e.getMessage();
    }
  }

  @Override
  public synchronized String tryPlaceUnitsOn(RemoteClient client, String territory, int units) throws RemoteException {
    try {
      Territory t = map.getTerritoryByName(territory);
      Player p = inGameClients.get(client);
      int remainingUnits = initialUnit - p.getTotalUnits();
      if (!t.getOwner().equals(p)) {
        return "Permission denied";
      } else if (units > remainingUnits) {
        return "Too many units";
      } else {
        t.increaseUnits(units);
        return null;
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
  public void doCommitOrder(RemoteClient client) throws RemoteException, InterruptedException, BrokenBarrierException {
    commitSignal.countDown();
    returnSignal.await(); // ready to next state
  }

  @Override
  public synchronized boolean isGameOver() throws RemoteException {
    return (inGameClients.size() == 1) ? true : false;
  }

  /**
   * Checks the network connections to all in-game Clients.
   * If remote client is alive then do nothing,
   * otherwise a RemoteException will be thrown
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
      Player p = inGameClients.get(c);
      if (p.isLose()) {
        watchingClients.put(c, p);
      }
    }
    for (RemoteClient w : watchingClients.keySet()) {
      inGameClients.remove(w);
    }
  }

  /**
   * Notifies all watching Clients of the current state of the game.
   */
  void notifyAllWatchers() {
    for (RemoteClient watcher : watchingClients.keySet()) {
      try {
        watcher.doDisplay(map);
      } catch (RemoteException e) {
        /*
         * RemoteException because the remote Client has disconnected, can be ignored
         */
      }
    }
  }

  /**
   * Sends game result to all clients.
   * 
   * @throws RemoteException if a remote error occurs
   */
  void notifyAllClientsGameResult() throws RemoteException {
    /* Precondition: isGameOver() == true ,so the outer loop should be run once */
    for (RemoteClient winner : inGameClients.keySet()) {
      winner.doDisplay(map);
      winner.doDisplay("You Win!");
      for (RemoteClient watcher : watchingClients.keySet()) {
        try {
          watcher.doDisplay(map);
          watcher.doDisplay("Winner is Player " + inGameClients.get(winner).getName());
        } catch (RemoteException e) {
          /*
           * RemoteException because the remote Client has disconnected, can be ignored
           */
        }
      }
    }
  }

  /**
   * Closes all clients.
   */
  void closeAllClients() {
    for (RemoteClient c : inGameClients.keySet()) {
      try {
        c.close();
      } catch (RemoteException e) {
        /*
         * RemoteException because the remote Client has disconnected, can be ignored
         */
      }
    }
    for (RemoteClient c : watchingClients.keySet()) {
      try {
        c.close();
      } catch (RemoteException e) {
        /*
         * RemoteException because the remote Client has disconnected, can be ignored
         */
      }
    }
  }

}
