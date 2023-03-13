package edu.duke.ece651.team7.client;

import java.rmi.RemoteException;

import edu.duke.ece651.team7.shared.*;

public class MapTextView {
    public static String display(GameMap map) throws RemoteException {
        StringBuilder str = new StringBuilder();
        System.out.println("ready iterate territories");
        for (Territory t : map.getTerritories()) {
            str.append(t.getName() + ": " + t.getOwner().getName()+ ", " + t.getUnits() + "\n");
        }
        return str.toString();
    }
}
