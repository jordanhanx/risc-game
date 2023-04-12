package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods of the Client class,
 * only methods defined here can be invoked on remote side.
 */
public interface RemoteClient extends Remote {

    public void updateGameMap(GameMap gameMap) throws RemoteException;

    public void updatePlayer(Player player) throws RemoteException;

    public void showPopupWindow(String msg) throws RemoteException;
}
