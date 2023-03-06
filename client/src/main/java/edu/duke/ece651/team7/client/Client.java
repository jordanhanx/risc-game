package edu.duke.ece651.team7.client;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import edu.duke.ece651.team7.shared.*;

public class Client {
  private final BufferedReader inputReader;
  private final PrintStream out;
  private Player self;
  private RemoteServer server;

  public Client(String host, int port, String name, BufferedReader in, PrintStream out)
      throws RemoteException, NotBoundException {
    this.inputReader = in;
    this.out = out;
    this.self = new Player(name);
    this.server = (RemoteServer) LocateRegistry.getRegistry(host, port).lookup("RiscGameServer");
    String msg = server.tryRegisterPlayer(this.self);
    if (msg != null) {
      throw new RuntimeException(msg);
    }
  }

  public void run() throws RemoteException {
    out.println("Client joined a RiskGame as Player:" + self.getName());
  }
}
