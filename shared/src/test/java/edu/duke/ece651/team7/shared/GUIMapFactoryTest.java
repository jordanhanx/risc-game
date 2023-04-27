package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class GUIMapFactoryTest  {
    @Test
    public void test_invalidGameMap(){
        MapFactory mf = new GUIMapFactory();
        assertThrows(IllegalArgumentException.class, ()->mf.createPlayerMap(5));
        assertThrows(IllegalArgumentException.class, ()->mf.createPlayerMap(1));

    }
    @Test
    public void test_create3playerMap(){
        MapFactory mf = new GUIMapFactory();
        GameMap threePlayersMap = mf.createPlayerMap(3);
        List<Player> initGroupOwners = threePlayersMap.getInitGroupOwners();
       assertTrue(threePlayersMap.isAdjacent("Narnia", "Midkemia"));
       assertTrue(threePlayersMap.isAdjacent("Westeros", "Aranthia"));
       assertTrue(threePlayersMap.isAdjacent("Aranthia", "Westeros"));
       assertTrue(threePlayersMap.isAdjacent("Galadria", "Helvoria"));
       assertTrue(threePlayersMap.isAdjacent("Gondor", "Elantris"));
       assertTrue(threePlayersMap.isAdjacent("Mordor", "Hogwarts"));
       assertFalse(threePlayersMap.isAdjacent("Roshar", "Midkemia"));
       assertFalse(threePlayersMap.isAdjacent("Narnia", "Aranthia"));

       assertEquals(initGroupOwners.get(0), threePlayersMap.getTerritoryByName("Mordor").getOwner());
       assertEquals(initGroupOwners.get(0), threePlayersMap.getTerritoryByName("Roshar").getOwner());
       assertEquals(initGroupOwners.get(0), threePlayersMap.getTerritoryByName("Narnia").getOwner());
       assertEquals(initGroupOwners.get(1), threePlayersMap.getTerritoryByName("Hogwarts").getOwner());
       assertEquals(initGroupOwners.get(1), threePlayersMap.getTerritoryByName("Highgarden").getOwner());
       assertEquals(initGroupOwners.get(1), threePlayersMap.getTerritoryByName("Aranthia").getOwner());
       assertEquals(initGroupOwners.get(2), threePlayersMap.getTerritoryByName("Winterfell").getOwner());
       assertEquals(initGroupOwners.get(2), threePlayersMap.getTerritoryByName("Braavos").getOwner());
       assertEquals(initGroupOwners.get(2), threePlayersMap.getTerritoryByName("Volantis").getOwner());

       assertEquals(8, initGroupOwners.get(0).getTerritories().size());
       assertEquals(8, initGroupOwners.get(1).getTerritories().size());
       assertEquals(8, initGroupOwners.get(2).getTerritories().size());

       assertEquals(3,threePlayersMap.getCostBetween("Narnia", "Midkemia") );
       assertEquals(2,threePlayersMap.getCostBetween("Narnia", "Roshar") );

       assertEquals(8,threePlayersMap.getCostBetween("Oz", "Elantris") );
       assertEquals(8,threePlayersMap.getCostBetween("Elantris", "Oz") );

       assertEquals(6,threePlayersMap.getCostBetween("Highgarden", "Galadria") );
       assertEquals(6,threePlayersMap.getCostBetween("Galadria","Highgarden") );

       assertEquals(5,threePlayersMap.getCostBetween("Highgarden", "Aranthia") );
       assertEquals(5,threePlayersMap.getCostBetween("Aranthia","Highgarden") );

       assertEquals(7,threePlayersMap.getCostBetween("Mordor", "Hogwarts") );
       assertEquals(7,threePlayersMap.getCostBetween("Hogwarts", "Mordor") );

       assertEquals(7,threePlayersMap.getCostBetween("Pyke", "Pentos") );
       assertEquals(7,threePlayersMap.getCostBetween("Pentos", "Pyke") );
    
    }
}
