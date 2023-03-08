package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class GameMapTest {
  @Test
  public void test_constructor() {
    List<Territory> territories = new ArrayList<Territory>();
    Territory territory = new Territory("testMap");
    territories.add(territory);
    GameMap m = new GameMap(territories);
    assertEquals(territories,m.getTerritories());
  }

  @Test
  public void test_getTerriesByName(){

    List<Territory> territories = new ArrayList<Territory>();
    List<Territory> territoriesExpected = new ArrayList<Territory>();

    //if don't override the equal and hashcode in territory, GameMap class

    // Territory territory = new Territory("testMap");
    // Territory territory2 = new Territory("testMap2");
    // Territory territory3 = new Territory("testMap");
    // territories.add(territory);
    // territories.add(territory2);
    // territories.add(territory3);
    // territoriesExpected.add(territory);
    // territoriesExpected.add(territory3);

    //if override the equal and hashcode in territory, GameMap class
    territories = Arrays.asList(new Territory("testMap"),new Territory("testMap2"),new Territory("testMap"));
    territoriesExpected = Arrays.asList(new Territory("testMap"),new Territory("testMap"));
   
    GameMap m = new GameMap(territories);
    assertEquals(territoriesExpected, m.getTerritoriesByName("testMap"));


  }


  //test cases for override equals in map
  @Test
  public void test_equals(){
    List<Territory> territories = new ArrayList<Territory>();
    List<Territory> territoriesExpected = new ArrayList<Territory>();
    
    //if don't override the equal and hashcode in territory, GameMap class

    // Territory territory = new Territory("testMap");
    // Territory territory2 = new Territory("testMap2");
    // territoriesExpected.add(territory);
    // territoriesExpected.add(territory2);
    // territories.add(territory);
    // territories.add(territory2);


    //if override the equal and hashcode in territory, GameMap class
    territoriesExpected = Arrays.asList(new Territory("testMap"),new Territory("testMap2"));
    territories = Arrays.asList(new Territory("testMap"),new Territory("testMap2"));
    GameMap m = new GameMap(territories);
    GameMap mExpected = new GameMap(territoriesExpected);
    assertEquals(true,m.equals(mExpected));

  }

}

