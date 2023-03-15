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
    public void testRemoveTerritory() {
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
    void test_getTerritories() {
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
        Player anonymousPlayer = new Player("orginal name") {
        };
        assertThrows(IllegalArgumentException.class, () -> pGreen.compareTo(anonymousPlayer));
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
