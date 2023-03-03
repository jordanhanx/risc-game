package edu.duke.ece651.team7.shared;

import java.util.HashSet;

public class Map{

    private HashSet<Territory> territories;

    
   /**
   * Constructs a map with a set of territories
   * 
   * @param territories is the set of territories in this map
   */
    public Map(HashSet<Territory> territories){
        this.territories = territories;
    }

    public HashSet<Territory> getTerritoriesSet(){
        return territories;
        
    }

}

