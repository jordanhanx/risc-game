package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods of the Territory class.
 */
public interface RemoteTerritory extends Remote {
    /**
     * Return the name of the Territory.
     * 
     * @return a String
     * @throws RemoteException
     */
    public String getName() throws RemoteException;
}
