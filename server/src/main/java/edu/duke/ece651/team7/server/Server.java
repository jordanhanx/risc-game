package edu.duke.ece651.team7.server;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import edu.duke.ece651.team7.shared.*;

public class Server extends UnicastRemoteObject implements RemoteServer {
  private final BufferedReader inputReader;
  private final PrintStream out;
  private ArrayList<RemotePlayer> players;

  public Server(BufferedReader in, PrintStream out) throws RemoteException {
    this.inputReader = in;
    this.out = out;
    this.players = new ArrayList<RemotePlayer>();
  }

  public synchronized String tryRegisterPlayer(RemotePlayer p) throws RemoteException {
    if (players.add(p)) {
      out.println("Remote Player:" + p.getName() + " has joined game");
      return null;
    } else {
      return "Failed to register";
    }
  }

  public void run() {
    out.println("Server ready");
  }
}
