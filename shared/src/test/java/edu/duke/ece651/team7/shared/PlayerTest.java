package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void test_getName() {
        Player p = new Player("test");
        assertEquals("test", p.getName());
        assertEquals(new LinkedList<Territory>(), p.getTerritories());
        assertNotEquals("TEST", p.getName());
    }

    @Test
    public void test_addTerritory() {
        Player p = new Player("test");
        Territory tA = mock(Territory.class);
        Territory tB = mock(Territory.class);
        assertDoesNotThrow(() -> p.addTerritory(tA));
        assertDoesNotThrow(() -> p.addTerritory(tB));
        assertThrows(IllegalArgumentException.class, () -> p.addTerritory(tA));
    }

    @Test
    public void test_removeTerritory() {
        Player p = new Player("test");
        Territory tA = mock(Territory.class);
        Territory tB = mock(Territory.class);
        assertDoesNotThrow(() -> p.addTerritory(tA));
        assertDoesNotThrow(() -> p.addTerritory(tB));
        assertDoesNotThrow(() -> p.removeTerritory(tA));
        assertThrows(IllegalArgumentException.class, () -> p.removeTerritory(tA));
        assertDoesNotThrow(() -> p.removeTerritory(tB));
    }

    @Test
    public void test_getTerritories() {
        Player p = new Player("test");
        Territory tA = mock(Territory.class);
        Territory tB = mock(Territory.class);
        assertDoesNotThrow(() -> p.addTerritory(tA));
        assertDoesNotThrow(() -> p.addTerritory(tB));
        Collection<Territory> territories = p.getTerritories();
        assertEquals(2, territories.size());
        assertDoesNotThrow(() -> territories.remove(tA));
        assertDoesNotThrow(() -> territories.remove(tB));
        assertEquals(0, territories.size());
    }

    @Test
    public void test_getTotalUnits() {
        Player p = new Player("test");
        assertEquals(0, p.getTotalUnits());
        Territory tA = mock(Territory.class);
        Territory tB = mock(Territory.class);
        when(tA.getUnitsNumber()).thenReturn(1);
        when(tB.getUnitsNumber()).thenReturn(3);
        p.addTerritory(tA);
        assertEquals(1, p.getTotalUnits());
        p.addTerritory(tB);
        assertEquals(4, p.getTotalUnits());
    }

    @Test
    public void test_getCurrentMaxLevel(){
        Player p = new Player("test", Level.AIRBORNE);
        assertEquals(Level.AIRBORNE, p.getCurrentMaxLevel());

        Player p1 = new Player("test");
        assertEquals(Level.INFANTRY, p1.getCurrentMaxLevel());
    }
    @Test
    public void test_isLose() {
        Player p = new Player("test");
        assertTrue(p.isLose());
        Territory tA = mock(Territory.class);
        assertDoesNotThrow(() -> p.addTerritory(tA));
        assertFalse(p.isLose());
        assertDoesNotThrow(() -> p.removeTerritory(tA));
        assertTrue(p.isLose());
    }

    @Test
    public void test_upgradeMaxLevel(){
        Player p = new Player("test", Level.AIRBORNE);
        assertEquals(Level.AIRBORNE, p.getCurrentMaxLevel());
        p.upgradeMaxLevel();
        assertEquals(Level.ULTRON, p.getCurrentMaxLevel());

        assertThrows(IllegalArgumentException.class, ()->p.upgradeMaxLevel());
    }

    @Test
    public void test_getResource(){
        Player p = new Player("test", Level.AIRBORNE);
        assertEquals(0,p.getFood().getAmount());
        assertEquals(0,p.getTech().getAmount());
        p.getFood().addResource(10);
        p.getTech().addResource(10);
        assertEquals(10,p.getFood().getAmount());
        assertEquals(10,p.getTech().getAmount());
    }

    @Test
    public void test_collectResource(){
        Territory t1 = mock(Territory.class);
        Territory t2 = mock(Territory.class);
        Territory t3 = mock(Territory.class);
        when(t1.produceFood()).thenReturn(new FoodResource(3));
        when(t2.produceFood()).thenReturn(new FoodResource(4));
        when(t3.produceFood()).thenReturn(new FoodResource(5));

        when(t1.produceTech()).thenReturn(new TechResource(3));
        when(t2.produceTech()).thenReturn(new TechResource(4));
        when(t3.produceTech()).thenReturn(new TechResource(5));

        Player p = new Player("test", Level.AIRBORNE);
        p.addTerritory(t1);
        p.addTerritory(t2);
        p.addTerritory(t3);
        assertEquals(0,p.getFood().getAmount());
        assertEquals(0,p.getTech().getAmount());

        p.collectResource();
        assertEquals(12,p.getFood().getAmount());
        assertEquals(12,p.getTech().getAmount());
    }


    @Test
    public void test_addAlliance(){
        Player p = new Player("test");
        Player p1 = new Player("test2");
        Player p3 = new Player("test 3");

        p.addAlliance(p1);
        p1.addAlliance(p);

        assertThrows(IllegalArgumentException.class, ()->p1.addAlliance(p));
        assertTrue(p.isAlliance(p1));
        assertTrue(p1.isAlliance(p));
        assertFalse(p.isAlliance(p3));
        assertThrows(IllegalArgumentException.class, ()->p.addAlliance(p3));
    }

    @Test
    public void test_breakAlliance(){
        Player p = new Player("test");
        Player p1 = new Player("test2");
        Player p3 = new Player("test3");

        p.addAlliance(p1);
        p1.addAlliance(p);
        
        assertTrue(p.isAlliance(p1));
        assertTrue(p1.isAlliance(p));
        assertFalse(p.isAlliance(p3));

        p.breakAllianceWith(p1);
        assertFalse(p.isAlliance(p1));
        assertFalse(p1.isAlliance(p));
        assertThrows(IllegalArgumentException.class, ()->p1.breakAllianceWith(p));
    }

    @Test
    public void test_bomb(){
        Player p = new Player("test");
        // Player p1 = new Player("test2");
        // Player p3 = new Player("test3");
        
        assertEquals(0, p.getBomb());
        p.modifyBombAmount(3);
        assertEquals(3, p.getBomb());
        assertThrows(IllegalArgumentException.class, ()->p.modifyBombAmount(-4));
        p.modifyBombAmount(-2);
        assertEquals(1, p.getBomb());
    }

    @Test
    public void test_aircraft(){
        Player p = new Player("test");
        assertEquals(0, p.getAircraft().size());
        p.addAircraft(3);
        assertEquals(3, p.getAircraft().size());
        assertEquals(3, p.getAircraft().get(0));
        assertEquals(3, p.getAircraft().get(1));
        assertEquals(3, p.getAircraft().get(2));

        p.consumeAircraft(2);
        assertEquals(2, p.getAircraft().get(0));
        assertEquals(2, p.getAircraft().get(1));
        assertEquals(3, p.getAircraft().get(2));
        
        assertThrows(IllegalArgumentException.class, ()->p.consumeAircraft(4));
        assertThrows(IllegalArgumentException.class, ()->p.consumeAircraft(-1));
        assertThrows(IllegalArgumentException.class, ()->p.addAircraft(-1));

        p.consumeAircraft(2);
        p.consumeAircraft(2);
        assertEquals(1, p.getAircraft().size());
        assertEquals(3, p.getAircraft().get(0));
    
    }
    @Test
    public void test_toString() {
        Player p = new Player("test");
        assertEquals("Player: test", p.toString());
    }

    @Test
    public void test_hashCode() {
        Player p1 = new Player("test1");
        Player p2 = new Player("test2");
        Player p3 = new Player("test1");
        assertNotEquals(p1.hashCode(), p2.hashCode());
        assertEquals(p1.hashCode(), p3.hashCode());
    }

    @Test
    public void test_equals() {
        Player p1 = new Player("test1");
        Player p2 = new Player("test2");
        Player p3 = new Player("test1");
        assertEquals(p1, p1);
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);
        assertEquals(p1, p3);
        assertEquals(p3, p1);
        assertNotEquals(p1, "test1");
        assertNotEquals(p1, null);
    }

    @Test
    public void test_compareTo() {
        Player pGreen = new Player("Green");
        Player pBlue = new Player("Blue");
        Player pRed = new Player("Red");

        assertTrue(pGreen.compareTo(pGreen) == 0);
        assertTrue(pGreen.compareTo(pBlue) > 0);
        assertTrue(pGreen.compareTo(pRed) < 0);
        assertTrue(pBlue.compareTo(pRed) < 0);
        assertThrows(IllegalArgumentException.class, () -> pGreen.compareTo(null));
        // Create an anonymous subclass
        Player anonymousPlayer = new Player("anonymous name") {
        };
        assertTrue(pGreen.compareTo(anonymousPlayer) < 0);
        assertThrows(IllegalArgumentException.class, () -> pGreen.compareTo(null));
    }

    @Test
    public void test_serializable() throws IOException, ClassNotFoundException {
        Player p = new Player("test");
        Object deserialized1 = deserialize(serialize(p));
        Object deserialized2 = deserialize(serialize(p));
        assertTrue(deserialized1 instanceof Player);
        assertEquals(deserialized1, deserialized2);
        assertEquals(p, deserialized1);
        assertEquals(p, deserialized2);
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
