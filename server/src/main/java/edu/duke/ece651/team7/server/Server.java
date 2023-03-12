package edu.duke.ece651.team7.server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.HashSet;

import edu.duke.ece651.team7.shared.*;

public class Server extends UnicastRemoteObject implements RemoteServer {
  private final PrintStream out;
  private final int numPlayers;
  private HashSet<RemoteClient> clients;
  private MapFactory factory;
  private RemoteGameMap map;
  // private MapFactory factory;

  public Server(int n, PrintStream out, MapFactory f) throws RemoteException {
    this.numPlayers = n;
    this.clients = new HashSet<RemoteClient>();
    this.out = out;
    factory = f;
    map = factory.createThreePlayerMap();
  }

  public void start(int port) throws RemoteException {
    LocateRegistry.createRegistry(port).rebind("GameServer", this);
    out.println("Server ready");
    HashSet<HashSet<Territory> > TerritoryGroups = groupTerritory();
    

  }

  public HashSet<HashSet<Territory> > groupTerritory() throws RemoteException{
    HashSet<HashSet<Territory> > ans = new HashSet<HashSet<Territory> >();
    for(Territory t : map.getTerritoriesSet()){
      HashSet<Territory> elem = new HashSet<Territory>();
      elem.add(t);
      ans.add(elem);
    }
    return ans;
  }

  @Override
  public synchronized boolean tryRegisterClient(RemoteClient client) throws RemoteException {
    if (clients.size() < numPlayers && !clients.contains(client)) {
      clients.add(client);
      out.println("Remote Player:" + client.getName() + " has joined game");

      return true;
    } else {
      return false;
    }
  }

  // public synchronized void initGameMap() throws RemoteException {
  //   HashSet<RemoteTerritory> territories = new HashSet<RemoteTerritory>();
  //   territories.add(new Territory("Hogwartz"));
  //   territories.add(new Territory("Gondor"));
  //   territories.add(new Territory("Oz"));
  //   // map = new GameMap(territories);
  //   out.println("Created a Map including " + map.getTerritoriesSet().size() + " territories");
  // }

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

  @Override
  public String tryMoveOrder(RemoteClient client, RemoteTerritory src, RemoteTerritory dest, int units) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'tryMoveOrder'");
  }

  @Override
  public String tryAttackOrder(RemoteClient client, RemoteTerritory src, RemoteTerritory dest, int units) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'tryAttackOrder'");
  }

  @Override
  public void doCommitOrder(RemoteClient client) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'doCommitOrder'");
  }

  @Override
  public boolean isGameOver() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isGameOver'");
  }
}
