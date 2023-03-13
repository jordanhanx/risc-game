package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    void test_getName() {
        Player p = new Player("test");
        assertEquals("test", p.getName());
        assertNotEquals("TEST", p.getName());
    }

    @Test
    void test_addTerritory() {
        Player p = new Player("test");
        Territory tA = mock(Territory.class);
        Territory tB = mock(Territory.class);
        assertDoesNotThrow(() -> p.addTerritory(tA));
        assertDoesNotThrow(() -> p.addTerritory(tB));
        assertThrows(IllegalArgumentException.class, () -> p.addTerritory(tA));
    }

    @Test
    void testRemoveTerritory() {
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
    void test_isLose() {
        Player p = new Player("test");
        assertTrue(p.isLose());
        Territory tA = mock(Territory.class);
        assertDoesNotThrow(() -> p.addTerritory(tA));
        assertFalse(p.isLose());
        assertDoesNotThrow(() -> p.removeTerritory(tA));
        assertTrue(p.isLose());
    }

    @Test
    void test_toString() {
        Player p = new Player("test");
        assertEquals("Player: test", p.toString());
    }

    @Test
    void test_hashCode() {
        Player p1 = new Player("test1");
        Player p2 = new Player("test2");
        Player p3 = new Player("test1");
        assertNotEquals(p1.hashCode(), p2.hashCode());
        assertEquals(p1.hashCode(), p3.hashCode());
    }

    @Test
    void test_equals() {
        Player p1 = new Player("test1");
        Player p2 = new Player("test2");
        Player p3 = new Player("test1");
        Territory tA = mock(Territory.class);
        assertEquals(p1, p1);
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);
        assertEquals(p1, p3);
        assertEquals(p3, p1);
        assertNotEquals(p1, tA);
        assertNotEquals(p1, null);
    }
}
