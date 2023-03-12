package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods of the Server class, and only methods
 * defined here can be invoked on remote side.
 */
public interface RemoteServer extends Remote {
    /**
     * Try to register current Client with the Server.
     * 
     * @param client is the Client to be registered.
     * @param name   is the Player's name.
     * @return null if the client is registered successfully, otherwise return
     *         the error message.
     * @throws RemoteException
     */
    public String tryRegisterClient(RemoteClient client, String name) throws RemoteException;

    /**
     * Greacefully end the game:
     * Try to unregister current Client with the Server when the Client is going to
     * quit, and delete the associated Player.
     * 
     * @param client is the client to be unregistered.
     * @return null if the client is registered successfully, otherwise return
     *         the error message.
     * @throws RemoteException
     */
    public String tryUnRegisterClient(RemoteClient client) throws RemoteException;

    /**
     * Get the remote stub for the GameMap.
     * 
     * @return the remote stub for the GameMap.
     * @throws RemoteException
     * @throws InterruptedException (a thread may throw this exception)
     */
    public GameMap getGameMap() throws RemoteException, InterruptedException;

    /**
     * Try to do a MOVE order.
     * 
     * @param client is the requesting Client.
     * @param from   is the source Territory's name.
     * @param to     is the destination Territory's name.
     * @param units  is how many units to be moved.
     * @return null if the order is legal, otherwise return the error message.
     * @throws RemoteException
     */
    public String tryMoveOrder(RemoteClient client, String from, String to, int units) throws RemoteException;

    /**
     * Try to do an ATTACK order.
     * 
     * @param client is the requesting Client.
     * @param from   is the source Territory's name.
     * @param to     is the destination Territory's name.
     * @param units  is how many units to combat.
     * @return null if the order is legal, otherwise return the error message.
     * @throws RemoteException
     */
    public String tryAttackOrder(RemoteClient client, String from, String to, int units) throws RemoteException;

    /**
     * Commit all above orders for current turn.
     * 
     * @param client is the requesting Client.
     * @throws RemoteException
     */
    public void doCommitOrder(RemoteClient client) throws RemoteException;

    /**
     * Check if the game is over.
     * 
     * @return true if there is a winner, otherwise return false.
     * @throws RemoteException
     */
    public boolean isGameOver() throws RemoteException;
}
