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

    }

    @Test
    public void test_modifyUnit(){
        Territory t1 = new Territory("a");
        Territory t2 = new Territory("b");
        Player p1 = new Player("null");
        MoveOrder o1 = new MoveOrder(p1, t1, t2, 10);
        AttackOrder a1 = new AttackOrder(p1, t1, t2, 200);
        a1.increaseUnits(4);
        o1.increaseUnits(3);
        assertEquals(204, a1.getUnits());
        assertEquals(13, o1.getUnits());
        a1.decreaseUnits(4);
        assertEquals(200, a1.getUnits());
        assertThrows(ArithmeticException.class, ()->o1.decreaseUnits(20));

    }



}
