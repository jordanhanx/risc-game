package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public interface RemoteGameMap extends Remote {
    public Set<Territory> getTerritoriesSet() throws RemoteException;
}
