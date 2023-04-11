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

    public int getGameInitUnits() throws RemoteException;

    public String tryRegisterClient(String username, RemoteClient client) throws RemoteException;

    public Player getSelfStatus(String username) throws RemoteException;

    public String tryPickTerritoryGroupByName(String username, String groupName) throws RemoteException;

    public String tryPlaceUnitsOn(String username, String territory, int units) throws RemoteException;

    public String tryMoveOrder(String username, String src, String dest, String level, int units)
            throws RemoteException;

    public String tryAttackOrder(String username, String src, String dest, String level, int units)
            throws RemoteException;

    public String tryResearchOrder(String usernamem, String src, String dest, String fromLev, String toLev, int units)
            throws RemoteException;

    public String tryUpgradeOrder(String username) throws RemoteException;

    public String doCommitOrder(String username) throws RemoteException, InterruptedException;
}
