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
  private RemoteServer server;

  public Client(String host, int port, String name, BufferedReader in, PrintStream out)
      throws RemoteException, NotBoundException {
    this.inputReader = in;
    this.out = out;
    this.name = name;
    this.server = (RemoteServer) LocateRegistry.getRegistry(host, port).lookup("RiscGameServer");
    if (!server.tryRegisterPlayer(this)) {
      throw new RuntimeException("Failed to register");
    }
    out.println("Client joined a RiskGame as Player:" + getName());
  }

  @Override
  public String getName() throws RemoteException {
    return name;
  }

  public void start() throws RemoteException, InterruptedException {
    out.println("sent GameMap request to server");
    RemoteGameMap map = server.getGameMap();
    out.print(MapTextView.display(map));
  }
}
