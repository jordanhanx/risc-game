package edu.duke.ece651.team7.shared;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class GameMap{

private Map<Territory, List<Territory>>  territoriesAdjacentList;


/**
* Constructs a GameMap object with a set of territories.
* @param territoriesAdjacentList a mapping between a Territory object and a list of adjacent territories
*/
public GameMap(Map<Territory, List<Territory>>  territoriesAdjacentList){
     this.territoriesAdjacentList = territoriesAdjacentList;
}

    // public HashMap<Territory, List<Territory>> getTerritories(){
    //     return territoriesAdjacentList;
        
    // }

/**
* Returns the territory with the specified name.
* @param name the name of the territory to retrieve
* @return the territory with the specified name
* @throws IllegalArgumentException if no territory with the specified name is found
*/
public Territory getTerritoryByName(String name){
    for(Territory terr: territoriesAdjacentList.keySet()){
        if(terr.getName().equals(name)){
            return terr;         
        }      
    }
    //temporary throw       
    throw new IllegalArgumentException("No Territory found with name: " + name); 
}


/**
* Checks whether two territories are adjacent to each other.
*
* @param from the name of the territory to check adjacency from
* @param to the name of the territory to check adjacency to
* @return true if the territories are adjacent, false otherwise
* @throws IllegalArgumentException if either the fromTerritory or toTerritory cannot be found
*/
public boolean isAdjacent(String from, String to){    
    Territory fromTerritory = getTerritoryByName(from);
    Territory toTerritory = getTerritoryByName(to);
    List<Territory> adjacentTerritories = territoriesAdjacentList.get(fromTerritory);
    return adjacentTerritories.contains(toTerritory);
}


/**
* Get the Territory set owned by a specific player
* teporary @param name is the name of the territory, need to be changed to owner: client
* @return the Territory set where territories have the same name--->owner: client
*/
public List<Territory> getTerritoriesByOwner(String name){
    //changed to owner: client
    List<Territory> terrSet = new ArrayList<Territory>();
    for(Territory terr: territoriesAdjacentList.keySet()){
        //getOwner()
        if(terr.getName().equals(name)){
            terrSet.add(terr);
        }
    }
    return terrSet;   
}

/**

Determines whether a path exists between two territories which belong to the same owner.
@param from the name of the source territory
@param to the name of the destination territory
@return true if a path exists between the source and destination territories, false otherwise
@throws IllegalArgumentException if either the source or destination territory cannot be found
*/
    
public boolean hasPath(String from, String to){
    Territory source = getTerritoryByName(from);
    Territory destination = getTerritoryByName(to);
    Set<Territory> territoryVisited = new HashSet<>();
    LinkedList<Territory> queue = new LinkedList<>();
    queue.add(source);
    territoryVisited.add(source);
    while (!queue.isEmpty()) {
        Territory curTerritory = queue.removeFirst();
        for (Territory neighbourTerritory : territoriesAdjacentList.get(curTerritory)) {
            if (neighbourTerritory.equals(destination)) {
                return true;
            }
            if (!territoryVisited.contains(neighbourTerritory) 
                // && neighbourTerritory.getOwner().equals(source.getOwner())
                ) {
                territoryVisited.add(neighbourTerritory);
                queue.add(neighbourTerritory);
            }
        }
    }
    return false;
}




}

