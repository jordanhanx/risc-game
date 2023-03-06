package edu.duke.ece651.team7.server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.HashSet;

import edu.duke.ece651.team7.shared.*;

public class Server extends UnicastRemoteObject implements RemoteServer {
  private final PrintStream out;
  private final int maxPlayerNum;
  private HashSet<RemoteClient> players;
  private GameMap map;

  public Server(int maxPlayerNum, PrintStream out) throws RemoteException {
    this.maxPlayerNum = maxPlayerNum;
    this.players = new HashSet<>();
    this.out = out;
  }

  public void start(int port) throws RemoteException {
    LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
    out.println("Server ready");
  }

  @Override
  public synchronized boolean tryRegisterPlayer(RemoteClient p) throws RemoteException {
    if (players.size() < maxPlayerNum && !players.contains(p)) {
      players.add(p);
      out.println("Remote Player:" + p.getName() + " has joined game");
      return true;
    } else {
      return false;
    }
  }

  public synchronized void initGameMap() throws RemoteException {
    HashSet<Territory> territories = new HashSet<>();
    territories.add(new Territory("Hogwartz"));
    territories.add(new Territory("Gondor"));
    territories.add(new Territory("Oz"));
    map = new GameMap(territories);
    out.println("Created a Map including " + map.getTerritoriesSet().size() + " territories");
  }

  @Override
  public synchronized RemoteGameMap getGameMap() throws RemoteException, InterruptedException {
    out.println("Received game map request, players current/max = " + players.size() + "/" + maxPlayerNum);
    if (players.size() == maxPlayerNum) {
      out.println("enough clients joined, game start");
      initGameMap();
      notifyAll();
      out.println("notified all waiting GameMap Request");
    } else {
      out.println("waiting for other clients");
      while (players.size() < maxPlayerNum) {
        wait();
      }
    }
    out.println("return GameMap");
    return map;
  }
}
