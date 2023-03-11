package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Iterator;

public interface RemotePlayer extends Remote {
    /**
     * Return the name of the Player.
     * 
     * @return a String.
     * @throws RemoteException
     */
    public String getName() throws RemoteException;

    /**
     * Get an Iterator to access all Territory objs belonging to the player.
     * 
     * @return an Iterator to a Territory obj belonging to the player.
     */
    public Iterator<RemoteTerritory> getTerritoriesIterator() throws RemoteException;

    /**
     * See whether the player has lost all territories.
     * 
     * @return true if the player has no territories, otherwise false.
     */
    public boolean isLose() throws RemoteException;
}
