package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
    assertEquals(t2, map.getTerritoryByName("Territory2"));

    //test equalIgnoreCase
    assertDoesNotThrow(()->map.getTerritoryByName("territOry2"));
    assertThrows(IllegalArgumentException.class, ()->map.getTerritoryByName("Territory22"));
    assertThrows(IllegalArgumentException.class, () -> map.getTerritoryByName("territory3"));
  }

  @Test
  public void test_isAdjacent() {
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
  public void test_hasPath() {
    Player p1 = new Player("blue");
    Territory t1 = new Territory("territory1",p1,1);
    Territory t2 = new Territory("territory2",p1,1);
    Territory t3 = new Territory("territory3",p1,1);
    Territory t4 = new Territory("territory4",p1,1);
    Territory t5 = new Territory("territory5",p1,1);
    Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
    List<Territory> a1 = new ArrayList<>();
    List<Territory> a2 = new ArrayList<>();
    List<Territory> a3 = new ArrayList<>();
    List<Territory> a4 = new ArrayList<>();
    List<Territory> a5 = new ArrayList<>();
    a1.add(t2);
    a2.add(t1);
    a2.add(t3);
    a2.add(t5);
    a3.add(t2);
    a5.add(t2);
    territoriesAdjacentList.put(t1, a1);
    territoriesAdjacentList.put(t2, a2);
    territoriesAdjacentList.put(t3, a3);
    territoriesAdjacentList.put(t4, a4);
    territoriesAdjacentList.put(t5, a5);
    GameMap map = new GameMap(territoriesAdjacentList);
    assertTrue(map.hasPath("territory1", "territory2"));
    assertTrue(map.hasPath("territory1", "territory5"));
    assertEquals(false, map.hasPath("territory1", "territory4"));

  }

  @Test
  public void test_getTerritories() {
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
  public void test_getNeighbors() {
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
    Collection<Territory> terr = map.getNeighbors("territory2");
    assertEquals(terr.size(), 2);
    assertEquals(true, terr.contains(t1));
    assertEquals(false, terr.contains(t2));
    assertEquals(true, terr.contains(t3));
  }

  @Test
  public void test_equals() {
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

    Territory t1B = new Territory("territory1");
    Territory t2B = new Territory("territory2");
    Territory t3B = new Territory("territory3");
    Map<Territory, List<Territory>> territoriesAdjacentListB = new HashMap<>();
    List<Territory> a1B = new ArrayList<>();
    List<Territory> a2B = new ArrayList<>();
    List<Territory> a3B = new ArrayList<>();
    a1B.add(t2B);
    a2B.add(t1B);
    a2B.add(t3B);
    a3B.add(t2B);
    territoriesAdjacentListB.put(t1B, a1B);
    territoriesAdjacentListB.put(t2B, a2B);
    territoriesAdjacentListB.put(t3B, a3B);
    GameMap mapB = new GameMap(territoriesAdjacentListB);

    assertEquals(map, mapB);
    assertNotEquals(map, "map");
    assertNotEquals(map, null);
  }

  @Test
  public void test_serializable() throws IOException, ClassNotFoundException {
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
    Object deserialized1 = deserialize(serialize(map));
    Object deserialized2 = deserialize(serialize(map));
    assertTrue(deserialized1 instanceof GameMap);
    assertEquals(deserialized1, deserialized2);
    assertEquals(map, deserialized1);
    assertEquals(map, deserialized2);
  }

  private static byte[] serialize(Object obj) throws IOException {
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    ObjectOutputStream o = new ObjectOutputStream(b);
    o.writeObject(obj);
    return b.toByteArray();
  }

  private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    ByteArrayInputStream b = new ByteArrayInputStream(bytes);
    ObjectInputStream o = new ObjectInputStream(b);
    return o.readObject();
  }

  @Test
  public void test_addTerritoryAndNeighbors(){

    GameMap map = new GameMap(new HashMap<>());
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Territory t3 = new Territory("territory3");
    map.addTerritoryAndNeighbors(t1, t2, t3);
    map.addTerritoryAndNeighbors(t2, t1, t3);
    map.addTerritoryAndNeighbors(t3, t1, t2);
    assertTrue(map.isAdjacent("territory1", "territory2"));
    assertEquals(new HashSet<>(Arrays.asList(t1,t3)), new HashSet<>(map.getNeighbors("territory2")));
    // assertEquals(Arrays.asList(t1,t3), map.getNeighbors("territory2"));

    assertEquals(new HashSet<>(Arrays.asList(t1,t2)), map.getNeighbors("territory3"));
    assertEquals(new HashSet<>(Arrays.asList(t2,t3)), map.getNeighbors("territory1"));
  }

  @Test
  public void test_addTerritoryAndNeighborsWithCost(){

    GameMap map = new GameMap(new HashMap<>());
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Territory t3 = new Territory("territory3");
    map.addTerritoryAndNeighbors(t1, t2, 3, t3, 4);
    map.addTerritoryAndNeighbors(t2, t1, 3, t3, 5);
    map.addTerritoryAndNeighbors(t3, t1,4,  t2, 5);
    assertTrue(map.isAdjacent("territory1", "territory2"));
    assertEquals(new HashSet<>(Arrays.asList(t1,t3)), new HashSet<>(map.getNeighbors("territory2")));
    // assertEquals(Arrays.asList(t1,t3), map.getNeighbors("territory2"));

    assertEquals(new HashSet<>(Arrays.asList(t1,t2)), map.getNeighbors("territory3"));
    assertEquals(new HashSet<>(Arrays.asList(t2,t3)), map.getNeighbors("territory1"));

    assertEquals(4,map.getCostBetween("territory1", "territory3") );
    assertEquals(4,map.getCostBetween("territory3", "territory1") );

    assertEquals(5,map.getCostBetween("territory2", "territory3") );
    assertEquals(5,map.getCostBetween("territory3", "territory2") );

    assertEquals(3,map.getCostBetween("territory1", "territory2") );
    assertEquals(3,map.getCostBetween("territory2", "territory1") );

  }
  @Test
  public void test_getInitGroupOwners(){
    GameMap map = new GameMap(3);
    List<Player> expectedList = Arrays.asList(
      map.new InitGroupOwner("GroupA"),
      map.new InitGroupOwner("GroupB"),
      map.new InitGroupOwner("GroupC")
  );
  List<Player> expectedList2 = Arrays.asList(
    new Player("GroupA"),
    new Player("GroupB"),
    new Player("GroupC")
);
  assertEquals(expectedList, map.getInitGroupOwners());
  assertNotEquals(expectedList2, map.getInitGroupOwners());
  }

  @Test
  public void test_assignGroup(){

    GameMap map = new GameMap(3);
    List<Player> initGroupOwners = map.getInitGroupOwners();
    Territory t1 = new Territory("T1", initGroupOwners.get(0),0);
    Territory t2 = new Territory("T2",initGroupOwners.get(1),0);
    map.addTerritoryAndNeighbors(t1, t2);
    map.addTerritoryAndNeighbors(t2, t1);
    Player p = new Player("Player1");
    Player p2 = new Player("Player2");
    Player p3 = new Player("Player3");
    map.assignGroup("GroupA", p);
    assertEquals(p, t1.getOwner());
    assertEquals(map.new InitGroupOwner("GroupB"), t2.getOwner());
    assertThrows(IllegalArgumentException.class, () -> map.assignGroup("GroupA", p2));
    assertThrows(IllegalArgumentException.class, () -> map.assignGroup("GroupD", p2));

    //test equalsIgnoreCase
    assertDoesNotThrow(() -> map.assignGroup("groupb", p2));
    assertThrows(IllegalArgumentException.class, () -> map.assignGroup("groubdd", p3));
    assertEquals(p2, t2.getOwner());
  }

  @Test
  public void test_findShortestPath(){
    MapFactory mf = new GUIMapFactory();
    GameMap map = mf.createPlayerMap(2);
    
    // Map<Territory, Integer> shortestPath = map.findShortestPath(map.getTerritoryByName("Midkemia");

    assertEquals(5,map.findShortestPath(map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Roshar")));
    // assertEquals(10,map.findShortestPath(map.getTerritoryByName("Midkemia"),map.getTerritoryByName("Roshar")));


    // Map<Territory, Integer> shortestPath2 = map.findShortestPath(map.getTerritoryByName("Essos");
    assertEquals(7 , map.findShortestPath(map.getTerritoryByName("Essos"), map.getTerritoryByName("Dorne")));
    //Essos -> Mordor -> Elantris -> Gondor -> Westeros
    assertEquals(18 , map.findShortestPath(map.getTerritoryByName("Essos"),map.getTerritoryByName( "Westeros")));

    // Map<Territory, Integer> shortestPath3 = map.findShortestPath(map.getTerritoryByName("Hogwarts"));
    //Hogwarts -> Elantris -> Gondor - >Westeros
    assertEquals(17,map.findShortestPath(map.getTerritoryByName("Hogwarts"), map.getTerritoryByName( "Westeros")));

    // Map<Territory, Integer> shortestPath4 = map.findShortestPath(map.getTerritoryByName("Dorne");
    //Dorne -> Hogwarts -> Elantris -> Gondor - >Westeros
    assertEquals(11 ,map.findShortestPath(map.getTerritoryByName("Dorne"),map.getTerritoryByName( "Westeros")));
    assertEquals(8,map.findShortestPath(map.getTerritoryByName("Dorne"),map.getTerritoryByName( "Mordor")));
    // assertEquals(11 ,map.findShortestPath(map.getTerritoryByName("Dorne"),map.getTerritoryByName( "Roshar")));
    assertEquals(13 ,map.findShortestPath(map.getTerritoryByName("Dorne"),map.getTerritoryByName( "Scadrial")));

  }

  @Test
  public void test_getCostBetween(){
    MapFactory mf = new GUIMapFactory();
    GameMap map = mf.createPlayerMap(2);

    assertEquals(7,map.getCostBetween(map.getTerritoryByName("Hogwarts"), map.getTerritoryByName("Mordor")));

  }


  @Test
  public void getNearestAllianceTerritory(){
    MapFactory mf = new GUIMapFactory();
    GameMap map = mf.createPlayerMap(3);
    Player p1 = map.getTerritoryByName("Volantis").getOwner();
    Player p2 = map.getTerritoryByName("Drakoria").getOwner();
    Player p3 = map.getTerritoryByName("Narnia").getOwner();
    p1.addAlliance(p2);
    p2.addAlliance(p1);

    assertEquals(6 ,map.findShortestPath(map.getTerritoryByName("Helvoria"),map.getTerritoryByName( "Dragonstone")));
    
    assertEquals(map.getTerritoryByName("Drakoria"), map.getNearestAllianceTerritory(map.getTerritoryByName("Pyke")));

    assertEquals(map.getTerritoryByName("Galadria"), map.getNearestAllianceTerritory(map.getTerritoryByName("Dragonstone")));

    assertEquals(map.getTerritoryByName("Winterfell"), map.getNearestAllianceTerritory(map.getTerritoryByName("Hogwarts")));

    assertThrows(IllegalArgumentException.class, ()->map.getNearestAllianceTerritory(map.getTerritoryByName("Narnia")));
  }


}