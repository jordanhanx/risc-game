package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods of the Client class,
 * only methods defined here can be invoked on remote side.
 */
public interface RemoteClient extends Remote {
    /**
     * Force the client quit from the game.
     * 
     * @param reason is the quit reason
     * @throws RemoteException
     */
    // public void forceQuit(String reason) throws RemoteException;
}
