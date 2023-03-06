package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    public boolean tryRegisterPlayer(RemoteClient p) throws RemoteException;

    public RemoteGameMap getGameMap() throws RemoteException, InterruptedException;
}
