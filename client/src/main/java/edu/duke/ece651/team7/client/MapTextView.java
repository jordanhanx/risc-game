package edu.duke.ece651.team7.client;

import java.rmi.RemoteException;

import edu.duke.ece651.team7.shared.*;

public class MapTextView {
    public static String display(RemoteGameMap map) throws RemoteException {
        StringBuilder str = new StringBuilder();
        for (RemoteTerritory t : map.getTerritoriesSet()) {
            str.append(t.getName() + "\n");
        }
        return str.toString();
    }
}
