package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;

public interface RemoteGameMap extends Remote {
    public HashSet<Territory> getTerritoriesSet() throws RemoteException;
}
