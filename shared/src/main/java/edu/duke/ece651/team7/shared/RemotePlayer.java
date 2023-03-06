package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePlayer extends Remote {
    // public void setName(String name) throws RemoteException;

    public String getName() throws RemoteException;

    // public boolean own(Territory t) throws RemoteException;

    // public boolean tryAddTerritory(Territory t) throws RemoteException;

    // public boolean tryRemoveTerritory(Territory t) throws RemoteException;

    // public boolean isLose() throws RemoteException;
}
