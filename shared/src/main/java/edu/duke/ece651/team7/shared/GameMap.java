package edu.duke.ece651.team7.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 3L; // Java recommends to declare this explicitly.
    private Map<Territory, List<Territory>> territoriesAdjacentList;

    /**
     * Constructs a GameMap object with a set of territories.
     * 
     * @param territoriesAdjacentList a mapping between a Territory object and a
     *                                list of adjacent territories
     */
    public GameMap(Map<Territory, List<Territory>> territoriesAdjacentList) {
        this.territoriesAdjacentList = territoriesAdjacentList;
    }

    /**
     *
     * Returns a collection of all territories in the game map.
     * 
     * @return a collection of all territories in the game map
     */
    public Collection<Territory> getTerritories() {
        return territoriesAdjacentList.keySet();
    }

    /**
     *
     * Returns a collection of all territories adjacent to the specified territory.
     * 
     * @param name the name of the territory for which adjacent territories are
     *             being retrieved
     * @return a collection of all territories adjacent to the specified territory
     * @throws IllegalArgumentException if no territory with the specified name is
     *                                  found
     */
    public Collection<Territory> getNeighbors(String name) {
        Territory terr = getTerritoryByName(name);
        return territoriesAdjacentList.get(terr);
    }

    /**
     * Returns the territory with the specified name.
     * 
     * @param name the name of the territory to retrieve
     * @return the territory with the specified name
     * @throws IllegalArgumentException if no territory with the specified name is
     *                                  found
     */
    public Territory getTerritoryByName(String name) {
        for (Territory terr : territoriesAdjacentList.keySet()) {
            if (terr.getName().equals(name)) {
                return terr;
            }
        }
        // temporary throw
        throw new IllegalArgumentException("No Territory found with name: " + name);
    }

    /**
     * Checks whether two territories are adjacent to each other.
     *
     * @param from the name of the territory to check adjacency from
     * @param to   the name of the territory to check adjacency to
     * @return true if the territories are adjacent, false otherwise
     * @throws IllegalArgumentException if either the fromTerritory or toTerritory
     *                                  cannot be found
     */
    public boolean isAdjacent(String from, String to) {
        Territory fromTerritory = getTerritoryByName(from);
        Territory toTerritory = getTerritoryByName(to);
        List<Territory> adjacentTerritories = territoriesAdjacentList.get(fromTerritory);
        return adjacentTerritories.contains(toTerritory);
    }


    /**
     * 
     * Determines whether a path exists between two territories which belong to the
     * same owner.
     * 
     * @param from the name of the source territory
     * @param to   the name of the destination territory
     * @return true if a path exists between the source and destination territories,
     *         false otherwise
     * @throws IllegalArgumentException if either the source or destination
     *                                  territory cannot be found
     */

    public boolean hasPath(String from, String to) {
        Territory source = getTerritoryByName(from);
        Territory destination = getTerritoryByName(to);
        Set<Territory> territoryVisited = new HashSet<>();
        LinkedList<Territory> queue = new LinkedList<>();
        queue.add(source);
        territoryVisited.add(source);
        while (!queue.isEmpty()) {
            Territory curTerritory = queue.removeFirst();
            for (Territory neighbourTerritory : territoriesAdjacentList.get(curTerritory)) {
                if (neighbourTerritory.equals(destination) 
                && neighbourTerritory.getOwner().equals(source.getOwner())
                ) {
                    return true;
                }
                if (!territoryVisited.contains(neighbourTerritory)
                && neighbourTerritory.getOwner().equals(source.getOwner())
                ) {
                    territoryVisited.add(neighbourTerritory);
                    queue.add(neighbourTerritory);
                }
            }
        }
        return false;
    }

    /**
    *Replaces the owner of all territories owned by a specific group with a new owner.
    *@param groupName the name of the group to replace the owner
    *@param newOwner the new owner to set for all territories previously owned by the group (with name groupName)
    *@throws IllegalArgumentException if the group name to be replaced is not an initial owner
    */
    public void setPlayer(String groupName, Player newOwner) {
        initPlayer groupSet = new initPlayer("initPlayer");
        if(!groupSet.initgroupName.contains(groupName)){
            throw new IllegalArgumentException("The owner: " + groupName+ " to be replaced is not an initial owner");
        }
        for (Territory terr : territoriesAdjacentList.keySet()) {
            if (terr.getOwner().getName().equals(groupName)) {
                terr.setOwner(newOwner);
                }
        }
    }

    /**
    *A class representing an initial player group for the game.
    *It contains a set of initial group names that are allowed to be replaced as owners of territories.
    */
    class initPlayer extends Player implements Serializable{
        //a set of initial group names that are allowed to be replaced as owners of territories.
        Set<String> initgroupName;

        /**
        * Constructs an `initPlayer` object with a given name.
        * Initializes the `initgroupName` set with three initial group names: "GroupA", "GroupB", and "GroupC".
        * @param name the name of the initial player group
        */
        public initPlayer(String name){
         super(name);
         initgroupName = new HashSet<>();
         initgroupName.add("GroupA");
         initgroupName.add("GroupB");
         initgroupName.add("GroupC");
         }
    }


    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(getClass())) {
            GameMap m = (GameMap) o;
            return territoriesAdjacentList.equals(m.territoriesAdjacentList);
        } else {
            return false;
        }
    }



}
