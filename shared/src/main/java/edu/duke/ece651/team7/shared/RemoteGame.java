package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.BrokenBarrierException;

/**
 * This interface defines remote methods of the Server class, and only methods
 * defined here can be invoked on remote side.
 */
public interface RemoteGame extends Remote {

    public int remoteGetInitUnits() throws RemoteException;

    public String tryRegisterClient(String username, RemoteClient client) throws RemoteException;

    public GameMap getGameMap() throws RemoteException;

    public Player getSelfStatus(String username) throws RemoteException;

    public String tryPickTerritoryGroupByName(String username, String groupName) throws RemoteException;

    public String tryPlaceUnitsOn(String username, String territory, int units) throws RemoteException;

    public String tryMoveOrder(String username, String src, String dest, int units) throws RemoteException;

    public String tryAttackOrder(String username, String src, String dest, int units) throws RemoteException;

    public void doCommitOrder(String username) throws RemoteException, InterruptedException, BrokenBarrierException;

    public boolean isGameOver() throws RemoteException;
}
