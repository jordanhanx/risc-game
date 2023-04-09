package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods that can be invoked remotely by the
 * clients of the game.
 */
public interface RemoteGame extends Remote {

    public enum GamePhase {
        PICK_GROUP, PLACE_UNITS, PLAY_GAME
    }

    public GamePhase getGamePhase() throws RemoteException;

    /**
     * Returns the initial number of units each player starts with.
     * 
     * @return the initial number of units
     * @throws RemoteException if there is an issue with remote invocation
     */
    public int getGameInitUnits() throws RemoteException;

    /**
     * Attempts to register a client with the given username.
     * 
     * @param username the username
     * @param client   the client to register
     * @return a message indicating success or failure of the registration attempt
     * @throws RemoteException if there is an issue with remote invocation
     */
    public String tryRegisterClient(String username, RemoteClient client) throws RemoteException;

    /**
     * Returns the current state of the game map.
     * 
     * @return the current game map
     * @throws RemoteException if there is an issue with remote invocation
     */
    public GameMap getGameMap() throws RemoteException;

    /**
     * Returns the current status of the player with the given username.
     * 
     * @param username the username of the player to query
     * @return the current status of the player
     * @throws RemoteException if there is an issue with remote invocation
     */
    public Player getSelfStatus(String username) throws RemoteException;

    /**
     * Attempts to pick the territory group with the given name for the player with
     * the given username.
     * 
     * @param username  the username of the player attempting to pick the territory
     *                  group
     * @param groupName the name of the territory group to pick
     * @return a message indicating success or failure of the pick attempt
     * @throws RemoteException if there is an issue with remote invocation
     */
    public String tryPickTerritoryGroupByName(String username, String groupName) throws RemoteException;

    /**
     * Attempts to place the given number of units on the specified territory for
     * the player with the given username.
     * 
     * @param username  the username of the player attempting to place units
     * @param territory the territory to place units on
     * @param units     the number of units to place
     * @return a message indicating success or failure of the place attempt
     * @throws RemoteException if there is an issue with remote invocation
     */
    public String tryPlaceUnitsOn(String username, String territory, int units) throws RemoteException;

    /**
     * Attempts to move the given number of units from the source territory to the
     * destination territory for the player with the given username.
     * 
     * @param username the username of the player attempting to move units
     * @param src      the source territory to move units from
     * @param dest     the destination territory to move units to
     * @param units    the number of units to move
     * @return a message indicating success or failure of the move attempt
     * @throws RemoteException if there is an issue with remote invocation
     */
    public String tryMoveOrder(String username, String src, String dest, int units) throws RemoteException;

    /**
     * Attempts to attack the destination territory with the given number of units
     * from the source territory for the player with the given username.
     * 
     * @param username the username of the player attempting to attack
     * @param src      the source territory to attack from
     * @param dest     the destination territory to attack
     * @param units    the number of units to attack with
     * @return a message indicating success or failure of the attack attempt
     * @throws RemoteException if there is an issue with remote invocation
     */
    public String tryAttackOrder(String username, String src, String dest, int units) throws RemoteException;

    /**
     * Instructs the server to commit the orders for the specified player.
     * Blocks until all players have submitted their orders or the timeout period
     * expires.
     * 
     * @param username the username of the player to commit the orders for
     * @throws RemoteException      if there is an issue with remote invocation
     * @throws InterruptedException if the current thread is interrupted while
     *                              waiting
     */
    public String doCommitOrder(String username) throws RemoteException, InterruptedException;

    /**
     * Returns whether or not the game is over.
     * 
     * @return true if the game is over, false otherwise
     * @throws RemoteException if there is an issue with remote invocation
     */
    public boolean isGameOver() throws RemoteException;
}
