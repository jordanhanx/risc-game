package edu.duke.ece651.team7.client;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import edu.duke.ece651.team7.shared.*;

public class Client extends UnicastRemoteObject implements RemoteClient {
  private final BufferedReader inputReader;
  private final PrintStream out;
  private final String name;
  private RemoteController server;

  public Client(String host, int port, String name, BufferedReader in, PrintStream out)
      throws RemoteException, NotBoundException,InterruptedException{
    this.inputReader = in;
    this.out = out;
    this.name = name;
    this.server = (RemoteController) LocateRegistry.getRegistry(host, port).lookup("GameServer");
  }

  public void start() throws RemoteException, InterruptedException {
    out.println("sent GameMap request to server");
    if (server.tryRegisterClient(this,name)!=null) {
      throw new RuntimeException("Failed to register");
    }
    out.println("Client joined a RiskGame as Player:" + name);
    GameMap map = server.getGameMap();
    out.print(MapTextView.display(map));
  }

  @Override
  public void forceQuit(String reason) throws RemoteException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'forceQuit'");
  }
}
