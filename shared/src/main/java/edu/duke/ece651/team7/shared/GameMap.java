package edu.duke.ece651.team7.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

public class GameMap extends UnicastRemoteObject implements RemoteGameMap {
    private HashSet<Territory> territories;

    /**
     * Constructs a map with a set of territories
     * 
     * @param territories is the set of territories in this map
     */
    public GameMap(HashSet<Territory> territories) throws RemoteException {
        super();
        this.territories = territories;
    }

    @Override
    public HashSet<Territory> getTerritoriesSet() throws RemoteException {
        return territories;
    }

}
