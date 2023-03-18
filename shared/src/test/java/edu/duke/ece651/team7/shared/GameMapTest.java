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
import java.util.LinkedHashMap;
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
    Collection terr = map.getNeighbors("territory2");
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
  public void test_groupTerritories(){
    Player player1 = new Player("player1");
    Player player2 = new Player("player2");
    Player player3 = new Player("player3");
    Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    Territory t3 = new Territory("t3");
    Territory t4 = new Territory("t4");
    Territory t5 = new Territory("t5");
    Territory t6 = new Territory("t6");
    territoriesAdjacentList.put(t1, new ArrayList<>(Arrays.asList(t2)));
    territoriesAdjacentList.put(t2, new ArrayList<>(Arrays.asList(t1, t3)));
    territoriesAdjacentList.put(t3, new ArrayList<>(Arrays.asList(t2, t4, t5)));
    territoriesAdjacentList.put(t4, new ArrayList<>(Arrays.asList(t3)));
    territoriesAdjacentList.put(t5, new ArrayList<>(Arrays.asList(t3, t6)));
    territoriesAdjacentList.put(t6, new ArrayList<>(Arrays.asList(t5)));
    GameMap testMap = new GameMap(territoriesAdjacentList);
    ArrayList<ArrayList<Territory>> territoryGroups = testMap.groupTerritories(3);
    assertEquals(3, territoryGroups.size());
    assertEquals(2, territoryGroups.get(0).size());
    assertEquals(2, territoryGroups.get(1).size());
    assertEquals(2, territoryGroups.get(2).size());
    Set<Territory> assignedTerritories = new HashSet<>();
    for (ArrayList<Territory> group : territoryGroups) {
        for (Territory territory : group) {
            assertTrue(!assignedTerritories.contains(territory));
            assignedTerritories.add(territory);
        }
    }
    assertTrue(assignedTerritories.contains(t1));
    assertTrue(assignedTerritories.contains(t2));
    assertEquals(6, assignedTerritories.size());
  }

  @Test
  public void test_userPickTerritoryGroup(){
    Territory territory1 = new Territory("Narnia");
    Territory territory2 = new Territory("Elantris");
    Territory territory3 = new Territory("Midkemia");
    Territory territory4 = new Territory("Oz");
    Territory territory5 = new Territory("Scadrial");
    Territory territory6 = new Territory("Roshar");
    Territory territory7 = new Territory("Gondor");
    Territory territory8 = new Territory("Mordor");
    Territory territory9 = new Territory("Hogwarts");

    Map<Territory, List<Territory>> territoriesAdjacentList = new LinkedHashMap<>();
    territoriesAdjacentList.put(territory1, new ArrayList<Territory>(Arrays.asList(territory2, territory3)));
    territoriesAdjacentList.put(territory2,
            new ArrayList<Territory>(Arrays.asList(territory1, territory3, territory5, territory6)));
    territoriesAdjacentList.put(territory3,
            new ArrayList<Territory>(Arrays.asList(territory1, territory2, territory4, territory5)));
    territoriesAdjacentList.put(territory4,
            new ArrayList<Territory>(Arrays.asList(territory7, territory3, territory5, territory8)));
    territoriesAdjacentList.put(territory5, new ArrayList<Territory>(
            Arrays.asList(territory2, territory3, territory4, territory6, territory9, territory8)));
    territoriesAdjacentList.put(territory6,
            new ArrayList<Territory>(Arrays.asList(territory9, territory2, territory5)));
    territoriesAdjacentList.put(territory7, new ArrayList<Territory>(Arrays.asList(territory4, territory8)));
    territoriesAdjacentList.put(territory8,
            new ArrayList<Territory>(Arrays.asList(territory4, territory7, territory5, territory9)));
    territoriesAdjacentList.put(territory9,
            new ArrayList<Territory>(Arrays.asList(territory6, territory5, territory8)));

    GameMap newMap = new GameMap(territoriesAdjacentList);
    ArrayList<ArrayList<Territory> > terrgroups = new ArrayList<ArrayList<Territory> >();
    ArrayList<ArrayList<Territory> > terrlists = newMap.userPickTerritoryGroup(terrgroups, "GroupA", "PlayerA");
    for(int j=0; j<terrlists.get(0).size();j++){
          assertEquals(terrlists.get(0).get(j).getOwner().getName(),"PlayerA");
     }
    assertEquals("Narnia", terrlists.get(0).get(0).getName());
    assertEquals("Elantris", terrlists.get(0).get(1).getName());
    assertEquals("Midkemia", terrlists.get(0).get(2).getName());
    assertEquals("Oz", terrlists.get(1).get(0).getName());
    assertEquals("Scadrial", terrlists.get(1).get(1).getName());

    for(int j=0; j<terrlists.get(1).size();j++){
      assertEquals(terrlists.get(1).get(j).getOwner().getName(),"GroupB");
    }
    for(int j=0; j<terrlists.get(2).size();j++){
        assertEquals(terrlists.get(2).get(j).getOwner().getName(),"GroupC");
    }  
    terrlists = newMap.userPickTerritoryGroup(terrlists, "GroupB", "PlayerB");
    for(int j=0; j<terrlists.get(0).size();j++){
      assertEquals(terrlists.get(0).get(j).getOwner().getName(),"PlayerA");
    }
    for(int j=0; j<terrlists.get(1).size();j++){
      assertEquals(terrlists.get(1).get(j).getOwner().getName(),"PlayerB");
    }
    for(int j=0; j<terrlists.get(2).size();j++){
      assertEquals(terrlists.get(2).get(j).getOwner().getName(),"GroupC");
    }  
    terrlists = newMap.userPickTerritoryGroup(terrlists, "GroupC", "PlayerC");
    for(int j=0; j<terrlists.get(0).size();j++){
      assertEquals(terrlists.get(0).get(j).getOwner().getName(),"PlayerA");
    }
    for(int j=0; j<terrlists.get(1).size();j++){
      assertEquals(terrlists.get(1).get(j).getOwner().getName(),"PlayerB");
    }
    for(int j=0; j<terrlists.get(2).size();j++){
      assertEquals(terrlists.get(2).get(j).getOwner().getName(),"PlayerC");
    }



}

}