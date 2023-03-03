package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TerritoryTest {
  @Test
  public void test_constructor() {
    Territory t = new Territory("test");
    assertEquals("test", t.getName());
  }

}
