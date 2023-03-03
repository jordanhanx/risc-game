package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.MyName;

public class MyNameTest {
  @Test
  public void test_getName() {
    assertEquals("team7", MyName.getName());
  }

}
