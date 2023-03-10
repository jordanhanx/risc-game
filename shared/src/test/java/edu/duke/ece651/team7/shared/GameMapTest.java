package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class GameMapTest {


  
  @Test
  public void test_getTerritoryByName() {
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
    territoriesAdjacentList.put(t1, new ArrayList<>(List.of(t2)));
    territoriesAdjacentList.put(t2, new ArrayList<>(List.of(t1)));
    GameMap map = new GameMap(territoriesAdjacentList);
    assertEquals(t1, map.getTerritoryByName("territory1"));
    assertEquals(t2, map.getTerritoryByName("territory2"));
    assertThrows(IllegalArgumentException.class, () -> {map.getTerritoryByName("territory3");});
  }

  @Test
  public void test_isAdjacent(){
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
    territoriesAdjacentList.put(t1, new ArrayList<>(List.of(t2)));
    territoriesAdjacentList.put(t2, new ArrayList<>(List.of(t1)));
    GameMap map = new GameMap(territoriesAdjacentList);
    assertTrue(map.isAdjacent("territory1", "territory2"));
    assertTrue(map.isAdjacent("territory2", "territory1"));
    assertFalse(map.isAdjacent("territory1", "territory1"));
  }


}

