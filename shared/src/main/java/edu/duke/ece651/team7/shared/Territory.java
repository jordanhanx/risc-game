package edu.duke.ece651.team7.shared;

import java.io.Serializable;

/**
 * This type represents Territory in the game.
 */
public class Territory implements Serializable {
  private static final long serialVersionUID = 1L; // Java recommends to declare this explicitly.
  private final String name;
  private Player owner;
  private int units;

  /**
   * Constucts a Territory with just inputted name
   * 
   * @param name name of territory
   */
  public Territory(String name) {
    this.name = name;
    this.owner = null;
    this.units = 0;
  }

  /**
   * Constructs a territory with inputted values
   * 
   * @param name  name of territory
   * @param owner player that owns the territory
   * @param units number of player's units present in territory
   */
  public Territory(String name, Player owner, int units) {
    this.name = name;
    this.owner = owner;
    if (units < 0) {
      throw new ArithmeticException("units cannot be less than 0");
    }
    this.units = units;
  }

  public String getName() {
    return name;
  }

  public int getUnits() {
    return units;
  }

  public Player getOwner() {
    return owner;
  }

  public void setOwner(Player p) {
    owner = p;
  }

  public void increaseUnits() {
    units ++;
  }

  public void increaseUnits(int num) {
    units += num;
  }

  public void decreaseUnits() {
    if (units> 0) {
      units--;
    } else {
      throw new ArithmeticException("units cannot be less than 0");
    }
  }

  public void decreaseUnits(int num) {
    if (units >= num) {
      units -= num;
    } else {
      throw new ArithmeticException("units cannot be less than 0");
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
