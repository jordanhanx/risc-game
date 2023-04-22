package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LevelTest {
    @Test
    public void test_valueOfLabel(){
        assertEquals(Level.CIVILIAN, Level.valueOfLabel(0));
        assertEquals(Level.INFANTRY, Level.valueOfLabel(1));
        assertEquals(Level.CAVALRY, Level.valueOfLabel(2));
        assertEquals(Level.TROOPER, Level.valueOfLabel(3));
        assertEquals(Level.ARTILLERY, Level.valueOfLabel(4));
        assertEquals(Level.AIRBORNE, Level.valueOfLabel(5));
        assertEquals(Level.ULTRON, Level.valueOfLabel(6));
    }
    @Test
    public void test_compare(){
        Level l1 = Level.INFANTRY;
        Level l2 = Level.INFANTRY;
        Level l3 = Level.INFANTRY;
        Level l4 = Level.CAVALRY;
        Level l5 = Level.TROOPER;
        Level l6 = Level.ARTILLERY;
        Level l7 = Level.AIRBORNE;
        Level l8 = Level.ULTRON;

        assertEquals(l1, l2);
        assertEquals(l1, l3);
        assertEquals(l2, l3);
        assertEquals(l1, l1);
        assertTrue(l1==l2);
        assertTrue(l4.compareTo(l1) > 0);
        assertTrue(l1.compareTo(l4) < 0);
        assertTrue(l5.compareTo(l4) > 0);
        assertTrue(l4.compareTo(l5) < 0);

        assertTrue(l5.compareTo(l4) > 0);
        assertTrue(l4.compareTo(l5) < 0);

        assertTrue(l6.compareTo(l5) > 0);
        assertTrue(l5.compareTo(l6) < 0);

        assertTrue(l8.compareTo(l7) > 0);
        assertTrue(l7.compareTo(l8) < 0);

        assertTrue(l8.compareTo(l1) > 0);
        assertTrue(l1.compareTo(l8) < 0);

        assertTrue(l1.compareTo(l2) == 0);

    }
}
