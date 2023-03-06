package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class GameMapTest {
  @Test
  public void test_constructor() {
    Set<Territory> territories = new HashSet<Territory>();
    Territory territory = new Territory("testMap");
    territories.add(territory);
    GameMap m = new GameMap(territories);
    assertEquals(territories,m.getTerritories());
  }

  @Test
  public void test_getTerriesByName(){
    Set<Territory> territories = new HashSet<Territory>();
    Set<Territory> territoriesExpected = new HashSet<Territory>();
    Territory territory = new Territory("testMap");
    Territory territory2 = new Territory("testMap2");
    Territory territory3 = new Territory("testMap");
    territories.add(territory);
    territories.add(territory2);
    territories.add(territory3);
    territoriesExpected.add(territory);
    territoriesExpected.add(territory3);
    GameMap m = new GameMap(territories);
    assertEquals(m.getTerritoriesByName("testMap"),territoriesExpected);
  }

  @Test
  public void test_equals(){
    Set<Territory> territories = new HashSet<Territory>();
    Territory territory = new Territory("testMap");
    Territory territory2 = new Territory("testMap2");
    territories.add(territory);
    territories.add(territory2);
    GameMap m = new GameMap(territories);
    Set<Territory> territoriesExpected = new HashSet<Territory>();
    territoriesExpected.add(territory);
    territoriesExpected.add(territory2);
    GameMap mExpected = new GameMap(territoriesExpected);
    assertEquals(true,m.equals(mExpected));

  }

}

