package edu.duke.ece651.team7.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 3L; // Java recommends to declare this explicitly.
    private Map<Territory, List<Territory>> territoriesAdjacentList;

    class InitGroupOwner extends Player {

        public InitGroupOwner(String name) {
            super(name);
        }
    }

    private List<Player> initGroupOwners;

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
     * Constructs a GameMap object with a specified number of initial group owners.
     *
     * @param initGroupNum the number of initial group owners to be created
     */
    public GameMap(int initGroupNum) {
        territoriesAdjacentList = new LinkedHashMap<>();
        initGroupOwners = new ArrayList<>();
        for (int i = 0; i < initGroupNum; ++i) {
            initGroupOwners.add(new InitGroupOwner("Group" + (char) ('A' + i)));
        }
    }

    /**
     * Return a list of the initial group owners for the game map.
     *
     * @return a list of the initial group owners for the game map
     */
    public List<Player> getInitGroupOwners() {
        return initGroupOwners;
    }

    /**
     * Adds the specified territory and its neighbors to the GameMap.
     *
     * @param t         the territory to add
     * @param neighbors the neighboring territories of the specified territory
     */
    public void addTerritoryAndNeighbors(Territory t, Territory... neighbors) {
        territoriesAdjacentList.put(t, new LinkedList<>());
        for (Territory neighbor : neighbors) {
            territoriesAdjacentList.get(t).add(neighbor);
        }
    }

    /**
     * Returns the InitGroupOwner object which has the specified 'groupName'.
     * 
     * @param groupName the name of the group to retrieve the InitGroupOwner for
     * @return the InitGroupOwner object for the specified group name
     * @throws IllegalArgumentException if the specified group name is not found
     */
    private InitGroupOwner getInitOwner(String groupName) {
        for (Player o : initGroupOwners) {
            if (o.getName().equalsIgnoreCase(groupName)) {
                return (InitGroupOwner) o;
            }
        }
        throw new IllegalArgumentException("Group " + groupName + " not found");
    }

    /**
     * Assigns the specified player to all territories owned by the owners with
     * 'groupName'.
     * 
     * @param groupName the name of the group whose territories will be assigned to
     *                  the player
     * @param player    the player to assign the territories to
     * @throws IllegalArgumentException if the specified group name is found in
     *                                  initGroupOwners, but the territories owned
     *                                  by it
     *                                  have already assigned to another player
     */
    public void assignGroup(String groupName, Player player) {
        int assigenCounter = 0;
        InitGroupOwner o = getInitOwner(groupName);
        for (Territory t : territoriesAdjacentList.keySet()) {
            if (t.getOwner().equals(o)) {
                t.setOwner(player);
                player.addTerritory(t);
                ++assigenCounter;
            }
        }
        if (assigenCounter == 0) {
            throw new IllegalArgumentException("" + o.getName() + " has been occupied");
        }
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
            if (terr.getName().equalsIgnoreCase(name)) {
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
                        && neighbourTerritory.getOwner().equals(source.getOwner())) {
                    return true;
                }
                if (!territoryVisited.contains(neighbourTerritory)
                        && neighbourTerritory.getOwner().equals(source.getOwner())) {
                    territoryVisited.add(neighbourTerritory);
                    queue.add(neighbourTerritory);
                }
            }
        }
        return false;
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
