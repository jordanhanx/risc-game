package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    public boolean tryRegisterClient(RemoteClient client) throws RemoteException;

    public RemoteGameMap getGameMap() throws RemoteException, InterruptedException;
    
    public String tryMoveOrder(RemoteClient client, RemoteTerritory src, RemoteTerritory dest, int units);
    public String tryAttackOrder(RemoteClient client, RemoteTerritory src, RemoteTerritory dest, int units);
    public void doCommitOrder(RemoteClient client);
    public boolean isGameOver(); 
}
