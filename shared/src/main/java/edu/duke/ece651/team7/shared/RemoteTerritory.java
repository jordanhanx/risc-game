package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods of the Territory class,
 * only methods defined here can be invoked on remote side.
 */
public interface RemoteTerritory extends Remote {
    /**
     * Return the name of the Territory.
     * 
     * @return a String
     * @throws RemoteException
     */
    public String getName() throws RemoteException;

    /**
     * Return the units number of the Territory.
     * 
     * @return a int representing how many units on the Territory.
     * @throws RemoteException
     */
    public int getUnits() throws RemoteException;

    /**
     * Return the remote stub of the territory's owner
     * 
     * @return a RemotePlayer reference to the Territory's owner Player.
     * @throws RemoteException
     */
    public RemotePlayer getOwner() throws RemoteException;
}
