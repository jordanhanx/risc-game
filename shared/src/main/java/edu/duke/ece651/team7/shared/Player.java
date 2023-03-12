package edu.duke.ece651.team7.shared;

import java.util.HashSet;
import java.util.Iterator;


public class Player implements RemotePlayer{
    private final String name;
    private HashSet<RemoteTerritory> territories;

    public Player(String n){
        name = n;
        territories = new HashSet<RemoteTerritory>();
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterator<RemoteTerritory> getTerritoriesIterator() {
        return territories.iterator();
    }
    
    public void addTerritory(Territory t){
        territories.add(t);
    }

    // public void removeTerritory(Territory t){

    // }
}
