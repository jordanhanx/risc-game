package edu.duke.ece651.team7.server;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;


import edu.duke.ece651.team7.shared.*;

public class Server extends UnicastRemoteObject implements RemoteServer{
  private final PrintStream out;
  private final int numPlayers;
  private HashMap<RemoteClient, Player> clients;
  private GameMap map;
  private ArrayList<ArrayList<Territory> > territoryGroups;
  private int ID;
  private final int initialUnit;
  private boolean clientsReady = false;
  private OrderExecuter ox;

  /**
   * 
   * @param n numer of players
   * @param out output stream
   * @param units number of initial units in each territory
   * @param m game map
   * @throws RemoteException
   */
  public Server(int n, PrintStream out, int units, GameMap m) throws RemoteException {
    this.numPlayers = n;
    this.clients = new HashMap<RemoteClient, Player>();
    this.out = out;
    map = m;
    territoryGroups = new ArrayList<ArrayList<Territory> >();
    ID = 0; 
    initialUnit = units;
    groupTerritories();
    ox = new OrderExecuter(map.getTerritories());
  }

  /**
   * Split territorries in map into numPlayers groups
   * @return the groups of territories
   * @throws RemoteException
   */
  private void groupTerritories(){
    // ArrayList<ArrayList<Territory> > ans = new ArrayList<ArrayList<Territory> >();
    List<Territory> tList = new ArrayList<Territory>(map.getTerritories());
    int numGroup = tList.size() / numPlayers;
    
    Collections.shuffle(tList);
    // out.println("numGroups is: "+ numGroup);
    for(int i = 0; i < numPlayers; i++){
      ArrayList<Territory> elem = new ArrayList<Territory>();
      for(int j = i*numGroup; j < (i+1)*numGroup; j++){
        tList.get(j).increaseUnits(initialUnit);
        elem.add(tList.get(j));
      }
      territoryGroups.add(elem);
    }
  }

  /**
   * testing the above function
   * @return a arraylist of grouped territories
   */
  public ArrayList<ArrayList<Territory> > getTerritoryGroups(){
    return territoryGroups;
  }

  /**
   * Start the server
   * @param port port number
   * @throws RemoteException
   */
  public synchronized void start(int port) throws InterruptedException,RemoteException {
    LocateRegistry.createRegistry(port).rebind("GameServer", this);
    out.println("Server ready");
    //block waiting for client register
    while(!clientsReady){
      wait();
    }
    clientsReady = false;
    for(RemoteClient c: clients.keySet()){
      out.println(clients.get(c).getName());
      for (Territory t:clients.get(c).getTerritories()){
        out.println(t.getName() + ": " + t.getUnits());
      }
    }
    //game start
    int turn = 0;
    while(true){
      //play one turn
      //wait for all clients to commit
      while(!clientsReady){
        wait();
      }
      System.out.println("Turn " + turn);
      clientsReady = false;
      ox.doAllCombats();
      if(isGameOver()){
        break;
      }
      turn++;
    }

  }

  @Override
  public synchronized String tryRegisterClient(RemoteClient client, String name) throws InterruptedException, RemoteException {
    out.println("Received rejister request, players current/max = " + clients.size() + "/" + numPlayers);
    if (clients.size() < numPlayers && !clients.containsKey(client)) {
      Player p = new Player(name);
      for(Territory t: territoryGroups.get(ID)){
        p.addTerritory(t);
        t.setOwner(p);
      }
      clients.put(client, p);
      ID++;
      if (clients.size() != numPlayers) {
        wait();
      }else{
        out.println("All player joined!");
        clientsReady = true;
        ID = 0;
        notifyAll();
      }
      return null;
    } else {
      return "Cannot register Client";
    }
  }

  @Override
  public GameMap getGameMap(){
    return map;
  }

  @Override
  public String tryUnRegisterClient(RemoteClient client) throws RemoteException {
    if(clients.containsKey(client)){
      clients.remove(client);
      return null;
    }
    return "The client is not registered";
  }

  @Override
  public String tryMoveOrder(RemoteClient client, String src, String dest, int units) throws RemoteException {
    Player p = clients.get(client);
    if(!p.isLose()){
      MoveOrder o = new MoveOrder(p, map.getTerritoryByName(src), map.getTerritoryByName(dest), units);
      //validate order,
      String err = ox.doOneMove(o);
      return err;
    }else{
      return "You Losed";
    }
  }

  @Override
  public String tryAttackOrder(RemoteClient client, String src, String dest, int units) throws RemoteException {
    Player p = clients.get(client);
    if(!p.isLose()){
      AttackOrder o = new AttackOrder(p, map.getTerritoryByName(src), map.getTerritoryByName(dest), units);
      //validate order
      String err = ox.pushCombat(o);
      return err;
    }else{
      return "You losed";
    }
  }


  @Override
  public synchronized void doCommitOrder(RemoteClient client) throws InterruptedException {
    if(ID != numPlayers-1){
      wait();
      ID++;
    }else{
      clientsReady = true;
      ID = 0;
      notifyAll();
    }
  }



  @Override
  public boolean isGameOver() throws RemoteException {
    int numLose = 0;
    for(RemoteClient c: clients.keySet()){
      if(clients.get(c).isLose()){
        numLose++;
      }
    }
    if(numLose == numPlayers-1){
      return true;
    }
    return false;
  }

  @Override
  public Player getSelfStatus(RemoteClient client) throws RemoteException {
    return clients.get(client);
  }

  @Override
  public Player getWinner() throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getWinner'");
  }


}
