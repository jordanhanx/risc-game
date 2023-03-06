package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class GameMapTest {
  @Test
  public void test_constructor() throws RemoteException {
    HashSet<Territory> territories = new HashSet<Territory>();
    Territory territory = new Territory("testMap");
    territories.add(territory);
    GameMap m = new GameMap(territories);
    assertEquals(territories, m.getTerritoriesSet());
  }

}
