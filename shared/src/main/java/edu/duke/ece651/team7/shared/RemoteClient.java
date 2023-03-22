package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods of the Client class,
 * only methods defined here can be invoked on remote side.
 */
public interface RemoteClient extends Remote {
    /**
     * Detect if the remote Client is alive.
     * 
     * @throws RemoteException if there is an error with the remote connection
     */
    public void ping() throws RemoteException;

    /**
     * Force the client to display the map.
     * 
     * @param map is the GameMap
     * @throws RemoteException
     */
    public void doDisplay(GameMap map) throws RemoteException;

    /**
     * Force the client to display the String message.
     * 
     * @param msg is the message
     * @throws RemoteException if there is an error with the remote connection
     */
    public void doDisplay(String msg) throws RemoteException;

    /**
     * Force the close the client.
     * 
     * @throws RemoteException if there is an error with the remote connection
     */
    public void close() throws RemoteException;
}
