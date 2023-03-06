package edu.duke.ece651.team7.shared;

public interface MapFactory {

   // public void createTerritory();

   /**
   * Make a map having two players
   */
    public GameMap createTwoPlayerMap();

   /**
   * Make a map having three players
   */
  public GameMap createThreePlayerMap();

}