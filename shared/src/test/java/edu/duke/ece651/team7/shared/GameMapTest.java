package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    assertThrows(IllegalArgumentException.class, () -> map.getTerritoryByName("territory3"));
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

  @Test
  public void test_hasPath(){
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Territory t3 = new Territory("territory3");
    Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
    List<Territory> a1 = new ArrayList<>();
    List<Territory> a2 = new ArrayList<>();
    List<Territory> a3 = new ArrayList<>();
    a1.add(t2);
    a2.add(t1);
    a2.add(t3);
    a3.add(t2);
    territoriesAdjacentList.put(t1, a1);
    territoriesAdjacentList.put(t2, a2);
    territoriesAdjacentList.put(t3, a3);
    GameMap map = new GameMap(territoriesAdjacentList);
    assertTrue(map.hasPath("territory1", "territory2"));

    
  }

  @Test
  public void test_getTerritories(){
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Territory t3 = new Territory("territory3");
    Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
    List<Territory> a1 = new ArrayList<>();
    List<Territory> a2 = new ArrayList<>();
    List<Territory> a3 = new ArrayList<>();
    a1.add(t2);
    a2.add(t1);
    a2.add(t3);
    a3.add(t2);
    territoriesAdjacentList.put(t1, a1);
    territoriesAdjacentList.put(t2, a2);
    territoriesAdjacentList.put(t3, a3);
    GameMap map = new GameMap(territoriesAdjacentList);
    Collection terr = map.getTerritories();
    assertEquals(terr.size(), 3);
    assertEquals(true, terr.contains(t1));
    assertEquals(true, terr.contains(t2));
    assertEquals(true, terr.contains(t3));
    
  }

  @Test
  public void test_getNeighbors(){
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Territory t3 = new Territory("territory3");
    Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
    List<Territory> a1 = new ArrayList<>();
    List<Territory> a2 = new ArrayList<>();
    List<Territory> a3 = new ArrayList<>();
    a1.add(t2);
    a2.add(t1);
    a2.add(t3);
    a3.add(t2);
    territoriesAdjacentList.put(t1, a1);
    territoriesAdjacentList.put(t2, a2);
    territoriesAdjacentList.put(t3, a3);
    GameMap map = new GameMap(territoriesAdjacentList);
    Collection terr = map.getNeighbors("territory2");
    assertEquals(terr.size(), 2);
    assertEquals(true, terr.contains(t1));
    assertEquals(false, terr.contains(t2));
    assertEquals(true, terr.contains(t3));
    
  }

}

