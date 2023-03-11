package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

    public boolean tryRegisterClient(RemoteClient client) throws RemoteException;

    public RemotePlayer createPlayer(RemoteClient client, String name) throws RemoteException;

    public RemoteGameMap getGameMap() throws RemoteException, InterruptedException;

    public String tryMoveOrder(RemoteClient client, String from, String to, int units) throws RemoteException;

    public String tryAttackOrder(RemoteClient client, String from, String to, int units) throws RemoteException;

    public void doCommitOrder(RemoteClient client) throws RemoteException;

    public boolean isGameOver() throws RemoteException;
}
