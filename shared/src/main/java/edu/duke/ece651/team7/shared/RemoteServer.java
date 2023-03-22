package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.BrokenBarrierException;

/**
 * This interface defines remote methods of the Server class, and only methods
 * defined here can be invoked on remote side.
 */
public interface RemoteServer extends Remote {
    /**
     * Try to register current Client with the Server as a Player.
     * 
     * @param client is the Client to be registered.
     * @param name   is the Player's name.
     * @return null if the client is registered successfully, otherwise return
     *         the error message.
     * @throws RemoteException if there is an error with the remote connection
     */
    public String tryRegisterClient(RemoteClient client, String name) throws RemoteException;

    /**
     * Get the GameMap copy.
     * 
     * @return the copy of the GameMap.
     * @throws RemoteException if there is an error with the remote connection
     */
    public GameMap getGameMap() throws RemoteException;

    /**
     * Get the self Player
     * 
     * @param client is the requesting Client.
     * @return the copy of the Player.
     * @throws RemoteException if there is an error with the remote connection
     */
    public Player getSelfStatus(RemoteClient client) throws RemoteException;

    /**
     * Get the initial units for each Player.
     * 
     * @return the initial units for each Player.
     * @throws RemoteException if there is an error with the remote connection
     */
    public int getInitUints() throws RemoteException;

    /**
     * Try to pick a group of Territories.
     * 
     * @param client    is the Client to be registered.
     * @param groupName is the group's name.
     * @return null if the client is registered successfully, otherwise return
     *         the error message.
     * @throws RemoteException if there is an error with the remote connection
     */
    public String tryPickTerritoryGroupByName(RemoteClient client, String groupName) throws RemoteException;

    /**
     * Try to place specific number of units on the Territory.
     * 
     * @param client is the Client to be registered.
     * @param name   is the Player's name.
     * @return null if the client is registered successfully, otherwise return
     *         the error message.
     * @throws RemoteException if there is an error with the remote connection
     */
    public String tryPlaceUnitsOn(RemoteClient client, String territory, int units) throws RemoteException;

    /**
     * Try to do a MOVE order.
     * 
     * @param client is the requesting Client.
     * @param from   is the source Territory's name.
     * @param to     is the destination Territory's name.
     * @param units  is how many units to be moved.
     * @return null if the order is legal, otherwise return the error message.
     * @throws RemoteException if there is an error with the remote connection
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
     * @throws RemoteException if there is an error with the remote connection
     */
    public String tryAttackOrder(RemoteClient client, String from, String to, int units) throws RemoteException;

    /**
     * Commit all above orders for current turn.
     * 
     * @param client is the requesting Client.
     * @throws RemoteException      if there is an error with the remote connection
     * @throws InterruptedException (This method will call wait()/notifyAll() and a
     *                              thread may throw the exception)
     */
    public void doCommitOrder(RemoteClient client) throws RemoteException, InterruptedException, BrokenBarrierException;

    /**
     * Check if the game is over.
     * 
     * @return true if there is a winner, otherwise return false.
     * @throws RemoteException if there is an error with the remote connection
     */
    public boolean isGameOver() throws RemoteException;
}
