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
import java.util.Arrays;
import java.util.Collection;

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

    Territory t3 = new Territory("test3", 10);
    assertEquals("test3", t3.getName());
    assertEquals(10, t3.getUnitsNumber());
    assertEquals(null, t3.getOwner());

    assertThrows(IllegalArgumentException.class, ()->new Territory("test3", -10,1,1));
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
  }

  @Test
  public void test_addunit(){
    Player p = mock(Player.class);
    Player p1 = mock(Player.class);
    Territory t = new Territory("test");
    t.setOwner(p);
    t.addUnits(new Unit(p));
    Unit u1 = new Unit(p);
    u1.upgrade(1);
    t.addUnits(u1);
    ArrayList<Unit> units = new ArrayList<>();
    units.add(new Unit(p));
    units.add(new Unit(p));
    units.add(new Unit(p));
    t.addUnits(units);
    assertEquals(5, t.getUnitsNumber());
    assertTrue(t.getUnits().contains(u1));
    assertThrows(IllegalArgumentException.class, ()->t.addUnits(u1));
    assertThrows(IllegalArgumentException.class, ()->t.addUnits(units));

    Unit u2 = new Unit(p1);
    assertThrows(IllegalArgumentException.class, ()->t.addUnits(u2));
    when(p.isAlliance(p1)).thenReturn(true);
    t.addUnits(u2);
    assertTrue(t.getUnits().contains(u2));

  }


  @Test
  public void test_removeunit(){
    Territory t = new Territory("test");
    Player p = mock(Player.class);
    Player p1 = mock(Player.class);
    when(p.isAlliance(p1)).thenReturn(true);
    when(p1.isAlliance(p)).thenReturn(true);
    t.setOwner(p);

    ArrayList<Unit> units = new ArrayList<Unit>(Arrays.asList(new Unit(Level.CAVALRY,p), new Unit(Level.CAVALRY,p), new Unit(Level.CAVALRY,p), 
                                                          new Unit(p), new Unit(p), new Unit(p),
                                                          new Unit(p1), new Unit(p1),new Unit(p1),
                                                          new Unit(Level.CAVALRY,p1), new Unit(Level.CAVALRY,p1)));
    t.addUnits(units);

    t.removeUnits(Level.CIVILIAN, 3);
    assertEquals(8, t.getUnitsNumber());
    assertNull(t.removeUnits(Level.CIVILIAN, 3));
    assertEquals(8, t.getUnitsNumber());
    t.removeUnits(Level.CAVALRY, 2, p1);
    assertEquals(6, t.getUnitsNumber());
    t.removeUnits(Level.CAVALRY, 3);
    assertEquals(3, t.getUnitsNumber());
  }

  @Test
  public void test_removeAllunit(){
    Territory t = new Territory("test");
    Player p = mock(Player.class);
    t.setOwner(p);
    Player p1 = mock(Player.class);
    when(p.isAlliance(p1)).thenReturn(true);
    when(p1.isAlliance(p)).thenReturn(true);
    ArrayList<Unit> units = new ArrayList<Unit>(Arrays.asList(new Unit(Level.CAVALRY,p), new Unit(Level.CAVALRY,p), new Unit(Level.CAVALRY,p), 
                                                              new Unit(p), new Unit(p), new Unit(p),
                                                              new Unit(p1), new Unit(p1),new Unit(p1),
                                                              new Unit(Level.CAVALRY,p1), new Unit(Level.CAVALRY,p1)));
    t.addUnits(units);


    assertEquals(11, t.getUnitsNumber());
    Collection<Unit> tomove = t.removeAllUnits();
    assertEquals(0, t.getUnitsNumber());
    assertEquals(11, tomove.size());
    
    t.addUnits(tomove);
    Collection<Unit> tomove2 = t.removeAllUnitsOfPlayer(p);
    assertTrue(tomove2.contains(units.get(0)));
    assertTrue(tomove2.contains(units.get(1)));
    assertEquals(6, tomove2.size());
    assertEquals(5, t.getUnitsNumber());

  }


  @Test
  public void test_upgradeUnits(){
    Player p = mock(Player.class);
    Territory t = new Territory("test",p,10);
    t.upgradeUnits(Level.CIVILIAN, Level.INFANTRY, 4);
    assertEquals(4, t.getUnitsNumberByLevel(Level.INFANTRY));
    assertEquals(6, t.getUnitsNumberByLevel(Level.CIVILIAN));

    t.upgradeUnits(Level.INFANTRY, Level.ULTRON, 3);
    assertEquals(1, t.getUnitsNumberByLevel(Level.INFANTRY));
    assertEquals(6, t.getUnitsNumberByLevel(Level.CIVILIAN));
    assertEquals(3, t.getUnitsNumberByLevel(Level.ULTRON));

    assertThrows(IllegalArgumentException.class,()->t.upgradeUnits(Level.CIVILIAN, Level.INFANTRY, 20));
  }

  @Test
  public void test_produceResource(){
    Territory t = new Territory("test",10, 3, 3);
    assertEquals(3, t.produceFood().getAmount());
    assertEquals(3, t.produceTech().getAmount());
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
  public void test_getUnits(){
    Player p = mock(Player.class);
    Player p1 = mock(Player.class);
    Territory t = new Territory("test",p,10);
    when(p.isAlliance(p1)).thenReturn(true);
    when(p1.isAlliance(p)).thenReturn(true);

    t.addUnits(new Unit(p1));
    assertEquals(10,t.getUnits(p).size());
    assertEquals(1,t.getUnitsNumber(p1));
  }

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
