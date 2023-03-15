package edu.duke.ece651.team7.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;
public class OrderTest {
    @Test
    public void test_getMethod(){
        Territory t1 = new Territory("a");
        Territory t2 = new Territory("b");
        Player p1 = new Player("null");
        MoveOrder o1 = new MoveOrder(p1, t1, t2, 10);
        AttackOrder a1 = new AttackOrder(p1, t1, t2, 200);
        assertEquals(p1, o1.getPlayer());
        assertEquals(t1, o1.getSrc());
        assertEquals(t2, o1.getDest());
        assertEquals(10, o1.getUnits());
        assertEquals(t1, a1.getSrc());
        assertEquals(200, a1.getUnits());

    }

    @Test
    public void test_equal(){
        Territory t1 = new Territory("a");
        Territory t2 = new Territory("b");
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        Player p3 = new Player("c");
        Order m1 = new MoveOrder(p1, t1, t2, 10);
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

        Order c1 = new CombatOrder(p1, t2, 10);
        Order c2 = new CombatOrder(p2, t1, 10);
        Order c3 = new CombatOrder(p3, t2, 9);
        assertEquals(AttackOrder.class, a1.getClass());
        assertFalse(c3.equals(null));
        assertFalse(c1.equals(m1));
        assertFalse(c2.equals(c3));
        assertFalse(a1.equals(c3));

    }

    @Test
    public void test_modifyUnit(){
        Territory t1 = new Territory("a");
        Territory t2 = new Territory("b");
        Player p1 = new Player("null");
        MoveOrder o1 = new MoveOrder(p1, t1, t2, 10);
        AttackOrder a1 = new AttackOrder(p1, t1, t2, 200);
        CombatOrder c1 = new CombatOrder(a1);
        CombatOrder c2 = new CombatOrder(p1, t2, 100);
        a1.increaseUnits(4);
        assertEquals(204, a1.getUnits());

        assertThrows(ArithmeticException.class, ()->o1.decreaseUnits(20));

        c1.increaseUnits(15);
        assertEquals(215, c1.getUnits());

        assertThrows(ArithmeticException.class, ()->c2.decreaseUnits(101));

        c1.decreaseUnits(15);
        assertEquals(200, c1.getUnits());
    }



}
