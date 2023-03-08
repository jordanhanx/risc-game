package edu.duke.ece651.team7.shared;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameMap{

   private List<Territory> territories;

    
   /**
   * Constructs a map with a set of territories
   * 
   * @param territories is the set of territories in this map
   */
    public GameMap(List<Territory> territories){
        this.territories = territories;
    }

    public List<Territory> getTerritories(){
        return territories;
        
    }

   /**
   * Get the Territory set owned by a specific player
   * 
   * teporary @param name is the name of the territory, need to be changed to owner: client
   * @return the Territory set where territories have the same name--->owner: client
   */
    public List<Territory> getTerritoriesByName(String name){
        //changed to owner: client
        List<Territory> terrSet = new ArrayList<Territory>();
        for(Territory t: territories){
            //getOwner()
            if(t.getName().equals(name)){
                terrSet.add(t);
            }
        }
        return terrSet;
    }

    
    // public Territory getTerritoriesByName(String name){

    // }

    

   /**
   * Override equals
   */

    //keep this if override equals in territory
    @Override
    public boolean equals (Object other){
        if(other != null && other.getClass().equals(getClass())){
            GameMap otherMap = (GameMap) other;
            return territories.equals(otherMap.getTerritories());
        }
        return false;
    }


}

