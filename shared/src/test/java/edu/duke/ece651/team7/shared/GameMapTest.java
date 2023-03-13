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
    Territory t1 = new Territory("territory1");
    Territory t2 = new Territory("territory2");
    Territory t3 = new Territory("territory3");
    Territory t4 = new Territory("territory4");
    Territory t5 = new Territory("territory5");
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

}
