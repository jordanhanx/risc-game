package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteTerritory {
    public String getName() throws RemoteException;
}
