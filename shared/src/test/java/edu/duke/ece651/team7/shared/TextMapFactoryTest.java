package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class TextMapFactoryTest {
    

    @Test
    public void test_createMap(){
        TextMapFactory mf = new TextMapFactory();
        GameMap newMap = mf.createMap();
        Collection<Territory> terr = newMap.getTerritories();
        Player p1 = new Player("blue");
        Player p2 = new Player("purple");
        Player p3 = new Player("red");
        Territory territory1 = new Territory("Narnia", p1, 10);
        Territory territory2 = new Territory("Elantris", p2, 6);
        Territory territory3 = new Territory("Midkemia", p1,12);
        Territory territory4 = new Territory("Oz",p1, 8);
        Territory territory5 = new Territory("Scadrial", p2,5);
        Territory territory6 = new Territory("Roshar",p2,3);
        Territory territory7 = new Territory("Gondor",p3,13);
        Territory territory8 = new Territory("Mordor",p3,14);
        Territory territory9 = new Territory("Hogwarts",p3,3);
        assertEquals(true, terr.contains(territory1));
        assertEquals(true, terr.contains(territory2));
        assertEquals(true, terr.contains(territory3));
        assertEquals(true, terr.contains(territory4));
        assertEquals(true, newMap.isAdjacent("Narnia", "Midkemia"));
        assertEquals(true, newMap.isAdjacent("Roshar", "Scadrial"));
        assertEquals(true, newMap.hasPath("Elantris", "Roshar"));
        assertEquals(true, newMap.hasPath("Narnia", "Oz"));
        assertEquals(true, newMap.hasPath("Gondor", "Hogwarts"));
        assertEquals(false, newMap.hasPath("Gondor", "Oz"));
        assertEquals(false, newMap.hasPath("Gondor", "Narnia"));

    }

    @Test
    public void createMapNew(){
        TextMapFactory mf = new TextMapFactory();
        GameMap threePlayersMap = mf.createMapNew(3);
        Player p1 = threePlayersMap.new InitGroupOwner("GroupA");
        Player p2 = threePlayersMap.new InitGroupOwner("GroupB");
        Player p3 = threePlayersMap.new InitGroupOwner("GroupC");
        Territory territory1 = new Territory("Narnia", p1, 10);
        Territory territory2 = new Territory("Elantris", p2, 6);
        Territory territory3 = new Territory("Midkemia", p1,12);
        Territory territory4 = new Territory("Oz",p1, 8);
        Territory territory5 = new Territory("Scadrial", p2,5);
        Territory territory6 = new Territory("Roshar",p2,3);
        Territory territory7 = new Territory("Gondor",p3,13);
        Territory territory8 = new Territory("Mordor",p3,14);
        Territory territory9 = new Territory("Hogwarts",p3,3);
        Collection<Territory> terr = threePlayersMap.getTerritories();
        assertEquals(true, terr.contains(territory1));
        assertEquals(true, terr.contains(territory2));
        assertEquals(true, terr.contains(territory3));
        assertEquals(true, terr.contains(territory4));
        assertEquals(true, terr.contains(territory5));
        assertEquals(true, terr.contains(territory6));
        assertEquals(true, terr.contains(territory7));
        assertEquals(true, terr.contains(territory8));
        assertEquals(true, terr.contains(territory9));

        Territory terr2 = threePlayersMap.getTerritoryByName("Narnia");
        Player p11 = terr2.getOwner();
        Player p12 = new Player("GroupA");
        Player p13 = threePlayersMap.new InitGroupOwner("GroupA");
        assertNotEquals(p11, p12);
        assertEquals(p11, p13);

        assertThrows(IllegalArgumentException.class, ()->mf.createMapNew(5));
        GameMap twoPlayersMap = mf.createMapNew(2);
        GameMap fourPlayersMap = mf.createMapNew(4);

  
    }

}
