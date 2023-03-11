package edu.duke.ece651.team7.shared;

public interface MapFactory {

  // public void createTerritory();

  /**
   * Make a map having two territories
   */
  public GameMap createTwoPlayerMap();

   /**
   * Make a map having three territories
   */
  public GameMap createThreePlayerMap();

}