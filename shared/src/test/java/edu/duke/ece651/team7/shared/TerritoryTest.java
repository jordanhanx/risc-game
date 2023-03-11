
package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class TerritoryTest {
  @Test
  public void test_default_constructor() {
    Territory t = new Territory();
    assertEquals("Default", t.getName());
    assertEquals(0, t.getUnits());
  }

  @Test
  public void test_constructor() {
    HashSet<String> neighbors = new HashSet<String>();
    neighbors.add("Oz"); neighbors.add("Hogwarts"); neighbors.add("Mordor");
    Territory t = new Territory("test", 3, neighbors);
    assertEquals("test", t.getName());
    assertEquals(3, t.getUnits());
    for (String x : neighbors) {
      assertEquals(true, t.isAdjacent(x));
    }
  }

  @Test
  public void test_increase_decrease_units() {
    HashSet<String> neighbors = new HashSet<String>();
    neighbors.add("Oz"); neighbors.add("Hogwarts"); neighbors.add("Mordor");
    Territory t = new Territory("test", 3, neighbors);
    assertEquals(3, t.getUnits());
    t.decreaseUnits();
    t.decreaseUnits();
    assertEquals(1, t.getUnits());
    t.decreaseUnits();
    // should still return 0 even as we continue to decrease
    t.decreaseUnits();
    t.decreaseUnits();
    assertEquals(0, t.getUnits());
    t.increaseUnits();
    t.increaseUnits();
    assertEquals(2, t.getUnits());
  }
    

  @Test
  public void test_increase_decrease_units() {
    HashSet<String> neighbors = new HashSet<String>();
    neighbors.add("Oz"); neighbors.add("Hogwarts"); neighbors.add("Mordor");
    Territory t = new Territory("test", 3, neighbors);
    assertEquals(3, t.getUnits());
    t.decreaseUnits();
    t.decreaseUnits();
    assertEquals(1, t.getUnits());
    t.decreaseUnits();
    // should still return 0 even as we continue to decrease
    t.decreaseUnits();
    t.decreaseUnits();
    assertEquals(0, t.getUnits());
    t.increaseUnits();
    t.increaseUnits();
    assertEquals(2, t.getUnits());
  }
    

}

