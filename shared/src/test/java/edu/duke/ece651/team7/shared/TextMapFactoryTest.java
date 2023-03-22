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
        MapFactory mf = new TextMapFactory();
        GameMap threePlayersMap = mf.createPlayerMap(3);
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

        assertThrows(IllegalArgumentException.class, ()->mf.createPlayerMap(5));
        GameMap twoPlayersMap = mf.createPlayerMap(2);
        GameMap fourPlayersMap = mf.createPlayerMap(4);

  
    }

}
