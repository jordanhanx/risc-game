package edu.duke.ece651.team7.shared;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
public class GameMap implements RemoteGameMap{

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
    
    // public Territory getTerritoriesByName(String name){

    // }


 
    //check path between the two territories that belong to the same owner
    // public boolean checkPath(Territory source, Territory destination){
    //     HashMap<String, Boolean> territoryVisited = new HashMap<>();
    //     for (Territory territory : territories) {
    //         territoryVisited.put(territory.getName(), false);
    //     }

    //     for (Territory territory : territories) {
    //         if (territory.getName().equals(source.getName())) {
    //             List<Territory> queue = new ArrayList<>();
    //             territoryVisited.put(territory.getName(), true);
    //             queue.add(territory);
    //             while (queue.size()>0) {
    //                 Territory curTerritory = queue.remove(0);
    //                 //add method in territory class
    //                 for (Territory neighbourTerritory : curTerritory.getNeighbourTerritories()) {
    //                     if (neighbourTerritory.getName().equals(destination.getName())) {
    //                         return true;
    //                     }
    //                     if(!territoryVisited.get(neighbourTerritory.getName())){
    //                     // && neighbourTerritory.getOwner().equals(source.getOwner()) 
    //                         territoryVisited.put(neighbourTerritory.getName(), true);
    //                         queue.add(neighbourTerritory);
    //                     }
    //                 }
    //             }
    //         }      
    //     }
    
    //     return false;
    // }
    



}

