package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TerritoryTest {
  @Test
  public void test_constructor() {
    Territory t1 = new Territory("test1");
    assertEquals("test1", t1.getName());
    assertEquals(0, t1.getUnitsNumber());
    assertEquals(null, t1.getOwner());

    Player p = mock(Player.class);
    Territory t2 = new Territory("test2", p, 3);
    assertEquals("test2", t2.getName());
    assertEquals(3, t2.getUnitsNumber());
    assertEquals(p, t2.getOwner());

    assertThrows(IllegalArgumentException.class, () -> new Territory("", p, -1));
  }

  @Test
  public void test_setOwner() {
    Territory t1 = new Territory("test1");
    assertEquals(null, t1.getOwner());
    Player p = mock(Player.class);
    t1.setOwner(p);
    assertEquals(p, t1.getOwner());
  }

  @Test
  public void test_setUnits() {
    Territory t = new Territory("test");
    ArrayList<Unit> units = new ArrayList<>();
    units.add(new Unit());
    units.add(new Unit());
    units.add(new Unit());
    units.add(new Unit());
    units.add(new Unit());
    t.setUnits(units);
    assertEquals(5, t.getUnitsNumber());

    // assertThrows(IllegalArgumentException.class, () -> t.setUnits(-1));
    // assertDoesNotThrow(() -> t.setUnits(0));

  }

  @Test
  public void test_addunit(){
    Territory t = new Territory("test");
    t.addUnits(new Unit());
    Unit u1 = new Unit();
    u1.upgrade(1);
    t.addUnits(u1);
    ArrayList<Unit> units = new ArrayList<>();
    units.add(new Unit());
    units.add(new Unit());
    units.add(new Unit());
    t.addUnits(units);
    assertEquals(5, t.getUnitsNumber());
    assertTrue(t.getUnits().contains(u1));
  }


  @Test
  public void test_removeunit(){
    Territory t = new Territory("test");
    t.addUnits(new Unit());
    Unit u1 = new Unit();
    u1.upgrade(1);
    t.addUnits(u1);
    ArrayList<Unit> units = new ArrayList<>();
    units.add(new Unit());
    units.add(new Unit());
    units.add(new Unit());
    t.addUnits(units);

    t.removeUnits(Level.CIVILIAN, 3);
    assertEquals(2, t.getUnitsNumber());
    assertTrue(t.getUnits().contains(u1));
    assertNull(t.removeUnits(Level.CIVILIAN, 3));
    assertTrue(t.removeUnits(Level.INFANTRY, 1).contains(u1));
  }

  // @Test
  // public void test_increaseUnits() {
  //   Territory t = new Territory("test");
  //   assertEquals(0, t.getUnits().size());
  //   t.increaseUnits(1);
  //   t.increaseUnits(1);
  //   assertEquals(2, t.getUnits().size());
  //   t.increaseUnits(3);
  //   assertEquals(5, t.getUnits().size());
  //   // assertThrows(IllegalArgumentException.class, () -> t.increaseUnits(0));
  //   // assertThrows(IllegalArgumentException.class, () -> t.increaseUnits(-1));
  // }

  // @Test
  // public void test_decreaseUnits() {
  //   Territory t = new Territory("test", null, 5);
  //   assertEquals(5, t.getUnits());
  //   t.decreaseUnits(1);
  //   assertEquals(4, t.getUnits());
  //   t.decreaseUnits(2);
  //   assertEquals(2, t.getUnits());
  //   assertThrows(IllegalArgumentException.class, () -> t.decreaseUnits(0));
  //   assertThrows(IllegalArgumentException.class, () -> t.decreaseUnits(-1));
  //   assertThrows(ArithmeticException.class, () -> t.decreaseUnits(3));
  //   assertDoesNotThrow(() -> t.decreaseUnits(2));
  //   assertThrows(ArithmeticException.class, () -> t.decreaseUnits(1));
  // }

  @Test
  public void test_toString() {
    Territory t = new Territory("test");
    assertEquals("test", t.toString());
  }

  @Test
  public void test_hashCode() {
    Territory t = new Territory("test");
    Territory t2 = new Territory("test");
    assertEquals(t.hashCode(), t2.hashCode());
  }

  @Test
  public void test_equals() {
    Territory t = new Territory("test");
    Territory t2 = new Territory("test");
    Territory t3 = new Territory("test2");
    assertEquals(t, t2);
    assertNotEquals(t, t3);
    assertNotEquals(t, "a1");
    assertNotEquals(t, null);
  }

  @Test
  public void test_serializable() throws IOException, ClassNotFoundException {
    Territory t = new Territory("test");
    Object deserialized1 = deserialize(serialize(t));
    Object deserialized2 = deserialize(serialize(t));
    assertTrue(deserialized1 instanceof Territory);
    assertEquals(deserialized1, deserialized2);
    assertEquals(t, deserialized1);
    assertEquals(t, deserialized2);
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
