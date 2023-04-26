package edu.duke.ece651.team7.server.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.server.model.AttackOrder;
import edu.duke.ece651.team7.server.model.MoveOrder;
import edu.duke.ece651.team7.server.model.Order;
import edu.duke.ece651.team7.server.model.ResearchOrder;
import edu.duke.ece651.team7.server.model.UpgradeOrder;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;
public class OrderTest {
    @Test
    public void test_constructor(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player groupC = mock(Player.class);
        // GameMap mockMap = mock(GameMap.class);

        Territory tNarnia = mock(Territory.class);
        Territory tElantris = mock(Territory.class);
        Territory tMidkemia = mock(Territory.class);
        Territory tScadrial = mock(Territory.class);
        Territory tRoshar = mock(Territory.class);
        Territory tOz = mock(Territory.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        Territory tGondor = mock(Territory.class);

        HashMap<Level, Integer> units = new HashMap<>();
        units.put(Level.CIVILIAN, 10);
        units.put(Level.INFANTRY, 8);
        units.put(Level.CAVALRY, 5);
        units.put(Level.AIRBORNE, 9);
        MoveOrder m1 = new MoveOrder(groupA, tNarnia, tElantris, Level.CIVILIAN, 10, Level.INFANTRY, 20);
        assertEquals(10, m1.units.get(Level.CIVILIAN));
        assertEquals(20, m1.units.get(Level.INFANTRY));
        assertNull(m1.units.get(Level.AIRBORNE));

        MoveOrder m2 = new MoveOrder(groupB, false, tOz, tRoshar, units);
        assertEquals(10, m2.units.get(Level.CIVILIAN));
        assertEquals(8, m2.units.get(Level.INFANTRY));
        assertEquals(9, m2.units.get(Level.AIRBORNE));

        AttackOrder m3 = new AttackOrder(groupA, false, tMidkemia, tGondor, Level.CIVILIAN, 10,  Level.INFANTRY, 20);
        assertEquals(10, m3.units.get(Level.CIVILIAN));
        assertEquals(20, m3.units.get(Level.INFANTRY));
        assertNull(m3.units.get(Level.AIRBORNE));

        AttackOrder m4 = new AttackOrder(groupA, false,0,tScadrial, tGondor, units);
        assertEquals(10, m4.units.get(Level.CIVILIAN));
        assertEquals(8, m4.units.get(Level.INFANTRY));
        assertEquals(9, m4.units.get(Level.AIRBORNE));

        ResearchOrder m5 = new ResearchOrder(groupA);
        assertEquals(groupA, m5.issuer);

        UpgradeOrder m6 = new UpgradeOrder(groupB, tGondor, Level.CIVILIAN, Level.AIRBORNE, 10);


        AllianceOrder m7 = new AllianceOrder(groupB, groupC);
        assertEquals(groupB, m7.issuer);
        assertEquals(groupC, m7.alliance);

        ManufactureOrder m8 = new ManufactureOrder(groupC, false, 10);
    
    }
    @Test
    public void test_equal(){
        Territory t1 = new Territory("a");
        Territory t2 = new Territory("b");
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        Player p3 = new Player("c");
        Order m1 = new MoveOrder(p1, t1, t2,  10);
        Order m2 = new MoveOrder(p2, t2, t1, 10);
        Order m3 = new MoveOrder(p1, t1, t2, 10);
        assertFalse(m3.equals(null));
        assertTrue(m1.equals(m3));
        assertFalse(m1.equals(m2));
        assertFalse(m3.equals(m2));
        assertEquals(MoveOrder.class, m1.getClass());

        Order a1 = new AttackOrder(p1, t1, t2, 10);
        Order a2 = new AttackOrder(p2, t2, t1, 10);
        Order a3 = new AttackOrder(p3, t1, t2, 9);
        assertEquals(AttackOrder.class, a1.getClass());
        assertFalse(a3.equals(null));
        assertFalse(a1.equals(m1));
        assertFalse(a2.equals(m2));
        assertFalse(a1.equals(a3));


        Order u1 = new UpgradeOrder(p1, t1, Level.CIVILIAN, Level.INFANTRY, 10);
        Order u2 = new UpgradeOrder(p1, t1, Level.CIVILIAN, Level.INFANTRY, 12);
        Order u3 = new UpgradeOrder(p1, t1, Level.CIVILIAN, Level.INFANTRY, 12);
        assertEquals(UpgradeOrder.class, u1.getClass());
        assertFalse(u3.equals(null));
        assertFalse(u1.equals(u2));
        assertTrue(u2.equals(u3));
        assertFalse(a1.equals(u3));

        Order r1 = new ResearchOrder(p1);
        Order r2 = new ResearchOrder(p2);
        Order r3 = new ResearchOrder(p1);
        assertEquals(ResearchOrder.class, r1.getClass());
        assertFalse(r3.equals(null));
        assertFalse(r1.equals(r2));
        assertTrue(r1.equals(r3));
        assertFalse(r1.equals(u3));

        ManufactureOrder m8 = new ManufactureOrder(p1, false, 10);
        ManufactureOrder m9 = new ManufactureOrder(p1, false, 10);
        assertTrue(m8.equals(m9));
        assertFalse(m8.equals(r1));

    }

    @Test
    public void test_manufactureOrder(){

    }
}
