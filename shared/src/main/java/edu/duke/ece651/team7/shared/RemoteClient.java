package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {
    /**
     * Force the client exit from the game when game is end.
     * 
     * @throws RemoteException
     */
    public void doGameOver() throws RemoteException;
}
