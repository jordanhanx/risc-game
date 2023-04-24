package edu.duke.ece651.team7.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This type represents Territory in the game.
 */
public class Territory implements Serializable {
  private static final long serialVersionUID = 1L; // Java recommends to declare this explicitly.
  private final String name;
  private Player owner;
  // private int units;
  private ArrayList<Unit> units;
  private final int foodProductionRate;
  private final int techProductionRate;


  /**
   * Constucts a Territory with just inputted name
   * 
   * @param name name of territory
   */
  public Territory(String name) {
    this(name, 0, 1,1);
  }

  /**
   * Constructs a territory with inputted values
   * @param name
   * @param numUnits
   */
  public Territory(String name, int numUnits){
    this(name, numUnits, 1, 1);
  }

  /**
   * Constructs a territory with inputted values
   * @param name
   * @param numUnits
   * @param foodProductionRate
   * @param techProductionRate
   */
  public Territory(String name, int numUnits, int foodProductionRate, int techProductionRate){
    this.name = name;
    this.owner = null;
    if (numUnits < 0) {
      throw new IllegalArgumentException("units cannot be less than 0");
    }
    this.units = new ArrayList<Unit>();
    for(int i = 0; i < numUnits; i++){
      this.units.add(new Unit());
    }
    this.foodProductionRate = foodProductionRate;
    this.techProductionRate = techProductionRate;

  }
  /**
   * Constructs a territory with inputted values
   * 
   * @param name  name of territory
   * @param owner player that owns the territory
   * @param units number of player's units present in territory
   */
  public Territory(String name, Player owner, int numUnits) {
    this.name = name;
    this.owner = owner;
    if (numUnits < 0) {
      throw new IllegalArgumentException("units cannot be less than 0");
    }
    this.units = new ArrayList<Unit>();
    for(int i = 0; i < numUnits; i++){
      this.units.add(new Unit(owner));
    }
    this.foodProductionRate = 1;
    this.techProductionRate = 1;
  }


  public String getName() {
    return name;
  }

  /**
   * 
   * @return the number of total units
   */
  public int getUnitsNumber() {
    return units.size();
  }

  /**
   * get the number of units on the territory owner by P 
   * @param p player who owns some units
   * @return the number of units on the territory owner by P 
   */
  public int getUnitsNumber(Player p) {
    int num = 0;
    for(int i = 0; i < units.size(); i++){
      if(p.equals(units.get(i).getOwner())){
        num ++;
      }
    }
    return num;
  }

  /**
   * 
   * @param l
   * @return the number of units with Level l on the territory owner by territory owner
   */
  public int getUnitsNumberByLevel(Level l) {
    return getUnitsNumberByLevel(l, owner);
  }

  /**
   * 
   * @param l
   * @param p
   * @return the number of units with Level l on the territory owner by Player p
   */
  public int getUnitsNumberByLevel(Level l, Player p) {
    int num = 0;
    for(int i = 0; i < units.size(); i++){
      if(units.get(i).getLevel() == l && p.equals(units.get(i).getOwner())){
        num ++;
      }
    }
    return num;
  }
  
  /**
   * 
   * @return the list of whole units on the territory
   */
  public Collection<Unit> getUnits(){
    return units;
  }

  /**
   * 
   * @param p
   * @return the list of units owned by p on the territory
   */
  public Collection<Unit> getUnits(Player p){
    ArrayList<Unit> pUnits = new ArrayList<>();
    for(Unit u : units){
      if(p.equals(u.getOwner())){
        pUnits.add(u);
      }
    }
    return pUnits;
  }

  public Player getOwner() {
    return owner;
  }

  public void setOwner(Player p) {
    // p.addTerritory(this);
    
    owner = p;
  }

  public void setUnits(ArrayList<Unit> newUnits) {
    units = newUnits;
  }

  /**
   * 
   * @param us one unit to be added
   */
  public void addUnits(Unit u){
    if(units.contains(u)){
      throw new IllegalArgumentException("Cannot repeatedly add the same unit");
    }
    if(!owner.equals(u.getOwner()) && !owner.isAlliance(u.getOwner())){
      throw new IllegalArgumentException("Cannot add unit that does not belong to owner/alliance of the territory");
    }
      units.add(u);
  }

  /**
   * 
   * @param us collection of units to be added
   */
  public void addUnits(Collection<Unit> us){
    for(Unit u: us){
      addUnits(u);
    }
  }

  /**
   * 
   * @param l the level of unit that want to be removed
   * @param num the number of units want to remove
   * @param p the player to move units
   * @return null if no enough unit with level l, else return the collection of units to be moved
   */
  public Collection<Unit> removeUnits(Level l, int num, Player p){
    ArrayList<Unit> tomove = new ArrayList<>();
    for(int i = 0; i < units.size(); i++){
      if(units.get(i).getLevel() == l && p.equals(units.get(i).getOwner())){
        tomove.add(units.get(i));
        // units.remove(units.get(i));
        num--;
        if(num == 0){
          break;
        }
      }
    }
    if(num!=0){
      return null;
    }else{
      for(Unit u: tomove){
        units.remove(u);
      }
      return tomove;
    }
  }

  /**
   * 
   * @param l the level of unit that want to be removed
   * @param num the number of units want to remove
   * @return null if no enough unit with level l, else return the collection of units to be moved
   */
  public Collection<Unit> removeUnits(Level l, int num){
    return removeUnits(l, num, owner);
  }


  public Collection<Unit> removeAllUnitsOfPlayer(Player p){
    ArrayList<Unit> tomove = new ArrayList<>();
    for(int i = 0; i < units.size(); i++){
      if(p.equals(units.get(i).getOwner())){
        tomove.add(units.get(i));
      }
    }
    for(Unit u: tomove){
      units.remove(u);
    }
    return tomove;
  }

  /**
   * remove all units of the territory
   * @return the units that has been moved
   */
  public Collection<Unit> removeAllUnits(){
    ArrayList<Unit> tomove = this.units;
    this.units = new ArrayList<>();
    return tomove;
  }

  public FoodResource produceFood(){
    return new FoodResource(foodProductionRate);
  }

  public TechResource produceTech(){
    return new TechResource(techProductionRate);
  }

  /**
   * 
   * @param from the level of unit upgrade from
   * @param to the level of unit upgrade to
   * @param num number of units to be upgraded
   */
  public void upgradeUnits(Level from, Level to, int num){
    ArrayList<Unit> toupgrade = new ArrayList<>();
    for(int i = 0; i < units.size(); i++){
      if(units.get(i).getLevel() == from){
        // units.get(i).upgrade(to);
        toupgrade.add(units.get(i));
        num--;
        if(num == 0){
          break;
        }
      }
    }
    if(num > 0){
      throw new IllegalArgumentException("No enough unit of level " + from);
    }
    for(Unit u: toupgrade){
      u.upgrade(to);
    }
  }
  
  @Override
  public boolean equals(Object other) {
    if (other != null && other.getClass().equals(getClass())) {
      Territory otherTerritory = (Territory) other;
      return name.equals(otherTerritory.name);
    }
    return false;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
