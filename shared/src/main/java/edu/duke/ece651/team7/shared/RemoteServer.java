package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    public String tryRegisterPlayer(RemotePlayer p) throws RemoteException;

    // public boolean isGameOver() throws RemoteException;
}
