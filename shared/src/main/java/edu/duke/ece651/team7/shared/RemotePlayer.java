package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Iterator;

public interface RemotePlayer extends Remote{
    public String getName();

    public Iterator<RemoteTerritory> getTerritoriesIterator();

}