package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RemoteClient is an interface that defines the methods for remote
 * communication between the server and the client.
 * This interface defines remote methods of the Client class,
 * only methods defined here can be invoked on remote side.
 */
public interface RemoteClient extends Remote {

    /**
     * Method to update the GameMap on the client side.
     * 
     * @param gameMap the updated GameMap object received from the server
     * @throws RemoteException if there is a communication error between the server
     *                         and the client
     */
    public void updateGameMap(GameMap gameMap) throws RemoteException;

    /**
     * Method to update the Player on the client side.
     * 
     * @param player the updated Player object received from the server
     * @throws RemoteException if there is a communication error between the server
     *                         and the client
     */
    public void updatePlayer(Player player) throws RemoteException;

    /**
     * Method to display a popup window on the client side with the specified
     * message.
     * 
     * @param msg the message to be displayed in the popup window
     * @throws RemoteException if there is a communication error between the server
     *                         and the client
     */
    public void showPopupWindow(String msg) throws RemoteException;

    public void showChatMessage(String msg) throws RemoteException;
}
