package edu.duke.ece651.team7.shared;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

public class UnitTest {
    @Test
    public void test_upgrade(){
        Unit u1 = new Unit();
        Unit u2 = new Unit();
        Unit u3 = new Unit();

        assertEquals(u1.getLevel(), Level.CIVILIAN);
        assertEquals(u2.getLevel(), Level.CIVILIAN);
        assertEquals(u3.getLevel(), Level.CIVILIAN);

        u1.upgrade(4);
        assertEquals(u1.getLevel(), Level.ARTILLERY);
        assertThrows(IllegalArgumentException.class, ()->u1.upgrade(0));
        assertThrows(IllegalArgumentException.class, ()->u1.upgrade(-1));
        assertThrows(IllegalArgumentException.class, ()->u1.upgrade(6));

        u1.upgrade(2);
        assertEquals(u1.getLevel(), Level.ULTRON);

        u3.upgrade(Level.CAVALRY);
        assertEquals(u3.getLevel(), Level.CAVALRY);
    }

    @Test
    public void test_compare(){
        Player p = mock(Player.class);
        Unit u1 = new Unit();
        Unit u2 = new Unit();
        Unit u3 = new Unit(Level.AIRBORNE, p);
        assertFalse(u1 == u2);
        assertFalse(u1.equals(u2));
        assertTrue(u1.compareTo(u3) < 0);
        assertTrue(u3.compareTo(u1) > 0);
        assertTrue(u1.compareTo(u2) == 0);

        assertFalse(u1.equals(null));
    }

    @Test
    public void test_equipBomb(){
        Unit u4 = new Unit(Level.AIRBORNE);
        assertThrows(IllegalArgumentException.class, ()-> u4.equipBomb());

        Unit u = new Unit(Level.ULTRON);
        u.equipBomb();
        assertTrue(u.hasBomb());
    }
}
