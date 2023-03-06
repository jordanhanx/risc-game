package edu.duke.ece651.team7.server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.HashSet;

import edu.duke.ece651.team7.shared.*;

public class Server extends UnicastRemoteObject implements RemoteServer {
  private final PrintStream out;
  private final int clientsCapacity;
  private HashSet<RemoteClient> clients;
  private GameMap map;

  public Server(int clientsCapacity, PrintStream out) throws RemoteException {
    this.clientsCapacity = clientsCapacity;
    this.clients = new HashSet<>();
    this.out = out;
  }

  public void start(int port) throws RemoteException {
    LocateRegistry.createRegistry(port).rebind("RiscGameServer", this);
    out.println("Server ready");
  }

  @Override
  public synchronized boolean tryRegisterClient(RemoteClient client) throws RemoteException {
    if (clients.size() < clientsCapacity && !clients.contains(client)) {
      clients.add(client);
      out.println("Remote Player:" + client.getName() + " has joined game");
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
    out.println("Received game map request, players current/max = " + clients.size() + "/" + clientsCapacity);
    if (clients.size() == clientsCapacity) {
      out.println("enough clients joined, game start");
      initGameMap();
      notifyAll();
      out.println("notified all waiting GameMap Request");
    } else {
      out.println("waiting for other clients");
      while (clients.size() < clientsCapacity) {
        wait();
      }
    }
    out.println("return GameMap");
    return map;
  }
}
