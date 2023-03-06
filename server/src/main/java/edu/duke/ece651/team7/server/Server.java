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

  public Server(int port, int maxPlayerNum, PrintStream out) throws RemoteException {
    this.maxPlayerNum = maxPlayerNum;
    this.players = new HashSet<>();
    this.out = out;
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

  @Override
  public synchronized void requestStart() throws RemoteException {
    out.println("Received start request, players current/max = " + players.size() + "/" + maxPlayerNum);
    if (players.size() == maxPlayerNum) {
      HashSet<Territory> territories = new HashSet<>();
      territories.add(new Territory("Hogwartz"));
      territories.add(new Territory("Gondor"));
      territories.add(new Territory("Oz"));
      map = new GameMap(territories);
      out.println("Created Map including " + territories.size() + " territories");
      for (RemoteClient p : players) {
        p.start();
        out.println("Sent start command to " + p.getName());
      }
    }
  }

  @Override
  public synchronized RemoteGameMap getGameMap() throws RemoteException {
    return map;
  }
}
