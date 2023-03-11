package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * This interface defines the remote methods of the GameMap class.
 */
public interface RemoteGameMap extends Remote {
    /**
     * Get an Iterator to access all Territory objs in the GameMap.
     * 
     * @return an Iterator to a Territory obj in the GameMap.
     * @throws RemoteException
     */
    public Iterator<RemoteTerritory> getTerritoriesIterator() throws RemoteException;

    /**
     * Get an Iterator to access all neighbors of the given Territory obj.
     * 
     * @return an Iterator to access all neighbors of the given Territory obj.
     * @throws RemoteException
     */
    public Iterator<RemoteTerritory> getNeighborsIterator(String name) throws RemoteException;

}
