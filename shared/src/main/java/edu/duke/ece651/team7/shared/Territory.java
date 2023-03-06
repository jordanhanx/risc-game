package edu.duke.ece651.team7.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Territory extends UnicastRemoteObject implements RemoteTerritory {
  private String name;

  @Override
  public String getName() throws RemoteException {
    return name;
  }

  /**
   * Constructs a territory with the specified name
   * 
   * @param name is the name of the newly constructed territory
   */

  public Territory(String name) throws RemoteException {
    super();
    this.name = name;
  }
}
