package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class MapTest {
  @Test
  public void test_constructor() {
    HashSet<Territory> territories = new HashSet<Territory>();
    Territory territory = new Territory("testMap");
    territories.add(territory);
    Map m = new Map(territories);
    assertEquals(territories, m.getTerritoriesSet());
  }

}
