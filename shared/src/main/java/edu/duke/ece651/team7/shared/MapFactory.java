package edu.duke.ece651.team7.shared;

public interface MapFactory {
  /**
   * This method creates a GameMap object with 24 territories evevenly assigned to
   * each player
   * 
   * @param initGroupNum the number of players
   * @return a GameMap object with 24 territories assigned to the number of
   *         players chosen
   */
  public GameMap createPlayerMap(int initGroupNum);

}