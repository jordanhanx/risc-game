package edu.duke.ece651.team7.shared;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * This type represents Player in the game.
 */
public class Player implements Serializable, Comparable {
    private static final long serialVersionUID = 2L; // Java recommends to declare this explicitly.
    private final String name;
    private LinkedList<Territory> territories;

    /**
     * Constructs a Player with the name.
     * 
     * @param name is the Player's name.
     */
    public Player(String name) {
        this.name = name;
        territories = new LinkedList<>();
    }

    /**
     * Get the Player's name.
     * 
     * @return the Player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get all territories belonging to the Player.
     * 
     * @return a linked list containing all territories.
     */
    public LinkedList<Territory> getTerritories() {
        return territories;
    }

    /**
     * Try to add a Territory to the Player.
     * 
     * @param t is the Territory to be added.
     * @throws IllegalArgumentException when the Territory already belongs to the
     *                                  Player.
     */
    public void addTerritory(Territory t) {
        if (territories.contains(t)) {
            throw new IllegalArgumentException("Player: " + name + " has already owned Territory: " + t.getName());
        }
        territories.add(t);
    }

    /**
     * Try to remove a Territory from the Player.
     * 
     * @param t is the Territory to be removed.
     * @throws IllegalArgumentException when the Territory doesn't belong to the
     *                                  Player.
     */
    public void removeTerritory(Territory t) {
        if (!territories.contains(t)) {
            throw new IllegalArgumentException("Player: " + name + " doesn't own Territory: " + t.getName());
        }
        territories.remove(t);
    }

    /**
     * See if the Player has lost the game.
     * 
     * @return true if the Player owns no Territory, otherwise false.
     */
    public boolean isLose() {
        if (territories.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Player: " + name;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(getClass())) {
            Player p = (Player) o;
            return name.equals(p.name);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Object arg0) {
        if (arg0 != null && arg0.getClass().equals(getClass())) {
            Player p = (Player) arg0;
            return name.compareTo(p.name);
        } else if (arg0 == null) {
            throw new IllegalArgumentException("Cannot compare with null");
        } else {
            throw new IllegalArgumentException("Different Class cannot be compared");
        }
    }

}
