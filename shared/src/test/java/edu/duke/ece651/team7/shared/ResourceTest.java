package edu.duke.ece651.team7.shared;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ResourceTest {
    @Test
    public void test_constructor(){
        Resource r1 = new FoodResource(0);
        assertEquals(0, r1.getAmount());

        Resource r2 = new TechResource(3);
        assertEquals(3, r2.getAmount());

        assertThrows(IllegalArgumentException.class, ()->new FoodResource(-3));
    }
    
    @Test
    public void test_addResource(){
        FoodResource r1 = new FoodResource(0);
        r1.addResource(3);
        assertEquals(3, r1.getAmount());
        assertThrows(IllegalArgumentException.class, ()->r1.addResource(-3));

        r1.addResource(new FoodResource(3));
        assertEquals(6, r1.getAmount());


        TechResource r2 = new TechResource(3);
        r2.addResource(4);
        assertEquals(7, r2.getAmount());
        assertThrows(IllegalArgumentException.class, ()->r2.addResource(-8));
        r2.addResource(new TechResource(3));
        assertEquals(10, r2.getAmount());
    }

    @Test
    public void test_consumeResource(){
        FoodResource r1 = new FoodResource(0);
        r1.addResource(3);
        r1.consumeResource(2);
        assertEquals(1, r1.getAmount());
        assertThrows(IllegalArgumentException.class, ()->r1.consumeResource(8));

        r1.consumeResource(new FoodResource(1));
        assertEquals(0, r1.getAmount());



        TechResource r2 = new TechResource(3);
        r2.addResource(4);
        assertEquals(7, r2.getAmount());
        assertThrows(IllegalArgumentException.class, ()->r2.consumeResource(-8));

        r2.consumeResource(new TechResource(6));
        assertEquals(1, r2.getAmount());
    }

    @Test
    public void test_comparetoFood(){
        FoodResource r1 = new FoodResource(0);
        FoodResource r2 = new FoodResource(0);
        FoodResource r3 = new FoodResource(4);
        FoodResource r4 = new FoodResource(5);

        assertTrue(r1.compareTo(r2) == 0);
        assertTrue(r2.compareTo(r3) < 0);
        assertTrue(r3.compareTo(r4) < 0);
        assertTrue(r4.compareTo(r1) > 0);
    }

    @Test
    public void test_comparetoTech(){
        TechResource r1 = new TechResource(0);
        TechResource r2 = new TechResource(0);
        TechResource r3 = new TechResource(4);
        TechResource r4 = new TechResource(5);

        assertTrue(r1.compareTo(r2) == 0);
        assertTrue(r2.compareTo(r3) < 0);
        assertTrue(r3.compareTo(r4) < 0);
        assertTrue(r4.compareTo(r1) > 0);
    }
}
