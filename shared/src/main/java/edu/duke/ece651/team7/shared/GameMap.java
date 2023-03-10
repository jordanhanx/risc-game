package edu.duke.ece651.team7.shared;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
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


 
    //check path between the two territories that belong to the same owner
    public boolean checkPath(Territory source, Territory destination){
        HashMap<String, Boolean> territoryVisited = new HashMap<>();
        for (Territory territory : territories) {
            territoryVisited.put(territory.getName(), false);
        }

        for (Territory territory : territories) {
            if (territory.getName().equals(source.getName())) {
                List<Territory> queue = new ArrayList<>();
                territoryVisited.put(territory.getName(), true);
                queue.add(territory);
                while (queue.size()>0) {
                    Territory curTerritory = queue.remove(0);
                    //add method in territory class
                    for (Territory neighbourTerritory : curTerritory.getNeighbourTerritories()) {
                        if (neighbourTerritory.getName().equals(destination.getName())) {
                            return true;
                        }
                        if(!territoryVisited.get(neighbourTerritory.getName())){
                        // && neighbourTerritory.getOwner().equals(source.getOwner()) 
                            territoryVisited.put(neighbourTerritory.getName(), true);
                            queue.add(neighbourTerritory);
                        }
                    }
                }
            }      
        }
    
        return false;
    }
    

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

