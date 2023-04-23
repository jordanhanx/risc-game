package edu.duke.ece651.team7.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This type represents Player in the game.
 */
public class Player implements Serializable, Comparable<Player> {
    protected static final long serialVersionUID = 2L; // Java recommends to declare this explicitly.
    private final String name;
    private LinkedList<Territory> territories;
    private TechResource tech;
    private FoodResource food;
    private Level maxTechLevel;
    private Player alliance;
    private LinkedList<Integer> aircrafts;
    private int numBomb;

    /**
     * Constructs a Player with the name.
     * 
     * @param name is the Player's name.
     */
    public Player(String name) {
        this(name, Level.INFANTRY);
    }

    /**
     * 
     * @param name is the Player's name.
     * @param l Player's maximum unit level
     */
    public Player(String name, Level l) {
        this.name = name;
        territories = new LinkedList<>();
        this.tech = new TechResource(0);
        this.food = new FoodResource(0);
        this.maxTechLevel = l;
        this.alliance = null;
        this.aircrafts = new LinkedList<>();
        this.numBomb = 0;
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
     * @return a Collection containing all territories.
     */
    public Collection<Territory> getTerritories() {
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
     * 
     * @return the sum of units in all territories
     */
    public int getTotalUnits() {
        int totalUnits = 0;
        for (Territory t : territories) {
            totalUnits += t.getUnitsNumber();
        }
        return totalUnits;
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


    /**
     * 
     * @param p the alliance to be added
     */
    public void addAlliance(Player p){
        if(isAlliance(p)){
            throw new IllegalArgumentException("Player: " + name + " is already your Alliance");
        }
        if(this.alliance != null){
            throw new IllegalArgumentException("Player: " + name + " already alliant with another Player");
        }
        this.alliance = p;
        // p.alliance = this;
        // if (this.alliances.contains(p)){
        //     throw new IllegalArgumentException("Player: " + name + " already is alliance of Player " + p.getName());
        // }else{
        //     this.alliances.add(p);
        // }
    }

    /**
     * See if the Player p is alliance of the current player.
     * 
     * @return true if the p is an alliance, false if not
     */
    public boolean isAlliance(Player p) {
        // if (this.alliances.contains(p)){
        //     return true;
        // }else{
        //     return false;
        // }
        return p.equals(this.alliance) && this.equals(p.alliance);
    }

    public Player getAlliance(){
        return this.alliance;
    }

    public void breakAllianceWith(Player p){
        if(!isAlliance(p)){
            throw new IllegalArgumentException("Player: " + name + " and Player " + p.getName() + " are not alliances ");
        }
        this.alliance = null;
        p.alliance = null;
        // this.alliances.remove(p);
    }

    /**
     * 
     * @return player's current maximum unit level
     */
    public Level getCurrentMaxLevel(){
        return this.maxTechLevel;
    }
    
    /**
     * upgrade the players's current maximun unit level
     */
    public void upgradeMaxLevel(){
        if(this.maxTechLevel == Level.ULTRON){
            throw new IllegalArgumentException("Upgrade MaxLevel Error: Already the highest level");
        }
        this.maxTechLevel = Level.valueOfLabel(this.maxTechLevel.label+1);
    }


    public FoodResource getFood(){
        return food;
    }

    public TechResource getTech(){
        return tech;
    }

    public void modifyBombAmount(int num){
        if(this.numBomb + num < 0){
            throw new IllegalArgumentException("Does not have enough Bomb");
        }else{
            numBomb += num;
        }
    }

    public void addAircraft(int num){
        if(num <= 0){
            throw new IllegalArgumentException("Cannot add negative amount of Aircrafts");
        }
        for(int i = 0; i < num; i++){
            this.aircrafts.add(3);
        }
    }

    public void consumeAircraft(int num){
        if(num <= 0){
            throw new IllegalArgumentException("Cannot consume negative amount of Aircraft");
        }
        if(this.aircrafts.size() - num < 0){
            throw new IllegalArgumentException("Does not have enough Aircraft");
        }
        for(int i = 0; i < num; i++){
            int cur_value = this.aircrafts.get(i);
            this.aircrafts.set(i, cur_value-1);
        }
        Iterator<Integer> iterator = this.aircrafts.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == 0) {
                iterator.remove();
            }
        }
    }

    public int getBomb(){
        return this.numBomb;
    }

    public LinkedList<Integer> getAircraft(){
        return this.aircrafts;
    }

    /**
     * collect food and tech resource from each territory the player owns
     */
    public void collectResource(){
        for(Territory t: this.territories){
            food.addResource(t.produceFood());
            tech.addResource(t.produceTech());
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
    public int compareTo(Player arg0) {
        if (arg0 != null) {
            return name.compareTo(arg0.name);
        } else {
            throw new IllegalArgumentException("Cannot compare with null");
        }
    }
}
