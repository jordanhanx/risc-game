package edu.duke.ece651.team7.server;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.MapFactory;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;
import edu.duke.ece651.team7.shared.TextMapFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderExecuterTest {

    protected GameMap buildTestMap(){
        Territory territory1 = new Territory("Narnia");
        Territory territory2 = new Territory("Elantris");
        Territory territory3 = new Territory("Midkemia");
        Territory territory4 = new Territory("Oz");
        Territory territory5 = new Territory("Scadrial");
        Territory territory6 = new Territory("Roshar");
        Territory territory7 = new Territory("Gondor");
        Territory territory8 = new Territory("Mordor");
        Territory territory9 = new Territory("Hogwarts");

        Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
        territoriesAdjacentList.put(territory1, new ArrayList<Territory>(Arrays.asList(territory2, territory3)));
        territoriesAdjacentList.put(territory2,
                new ArrayList<Territory>(Arrays.asList(territory1, territory3, territory5, territory6)));
        territoriesAdjacentList.put(territory3,
                new ArrayList<Territory>(Arrays.asList(territory1, territory2, territory4, territory5)));
        territoriesAdjacentList.put(territory4,
                new ArrayList<Territory>(Arrays.asList(territory7, territory3, territory5, territory8)));
        territoriesAdjacentList.put(territory5, new ArrayList<Territory>(
                Arrays.asList(territory2, territory3, territory4, territory6, territory9, territory8)));
        territoriesAdjacentList.put(territory6,
                new ArrayList<Territory>(Arrays.asList(territory9, territory2, territory5)));
        territoriesAdjacentList.put(territory7, new ArrayList<Territory>(Arrays.asList(territory4, territory8)));
        territoriesAdjacentList.put(territory8,
                new ArrayList<Territory>(Arrays.asList(territory4, territory7, territory5, territory9)));
        territoriesAdjacentList.put(territory9,
                new ArrayList<Territory>(Arrays.asList(territory6, territory5, territory8)));

        GameMap map = new GameMap(territoriesAdjacentList);
        return map;
    }
    @Test
    public void test_doOneMove(){
        // MapFactory factory = new TextMapFactory();
        // GameMap map = factory.createMap();
        // OrderExecuter ox = new OrderExecuter(map);
        // Player p1 = new Player("a");
        // Player p2 = new Player("b");

        // MoveOrder m1 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"), 5);
        // ox.doOneMove(m1);
        // assertEquals(5, map.getTerritoryByName("Narnia").getUnits());
        // assertEquals(17, map.getTerritoryByName("Midkemia").getUnits());

        // MoveOrder m2 = new MoveOrder(p2, map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Oz"), 5);
        // ox.doOneMove(m2);
        // assertEquals(13, map.getTerritoryByName("Oz").getUnits());
        // assertEquals(12, map.getTerritoryByName("Midkemia").getUnits());


    };

    @Test
    public void test_isInAttackPool(){
    
    }



    @Test
    public void test_PushCombat(){
       
    }
    
    
}
