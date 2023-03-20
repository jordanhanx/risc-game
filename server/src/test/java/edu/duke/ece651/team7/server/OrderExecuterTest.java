package edu.duke.ece651.team7.server;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.MapFactory;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;
import edu.duke.ece651.team7.shared.TextMapFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

public class OrderExecuterTest {

    // protected OrderExecuter buildOrderExecuter(){
    //     MapFactory factory = new TextMapFactory();
    //     GameMap map = factory.createMapTest();
    //     OrderExecuter ox = new OrderExecuter(map.getTerritories());
    //     return ox;
    // }
    @Test
    public void test_doOneMove(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createPlayerMap(3);
        OrderExecuter ox = new OrderExecuter(map.getTerritories());
        Player p1 = new Player("a");
        Player p2 = new Player("b");

        MoveOrder m1 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"), 5);
        ox.doOneMove(m1);
        assertEquals(5, map.getTerritoryByName("Narnia").getUnits());
        assertEquals(17, map.getTerritoryByName("Midkemia").getUnits());

        MoveOrder m2 = new MoveOrder(p2, map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Oz"), 5);
        ox.doOneMove(m2);
        assertEquals(13, map.getTerritoryByName("Oz").getUnits());
        assertEquals(12, map.getTerritoryByName("Midkemia").getUnits());
    };

    @Test
    public void test_PushCombat(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createPlayerMap(3);
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        Player p3 = new Player("c");
        map.getTerritoryByName("Elantris").setOwner(p3);
        map.getTerritoryByName("Hogwarts").setOwner(p3);
        OrderExecuter ox = new OrderExecuter(map.getTerritories());

        AttackOrder m1 = new AttackOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Elantris"), 5);
        AttackOrder m2 = new AttackOrder(p1, map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Elantris"), 5);

        AttackOrder m3 = new AttackOrder(p2, map.getTerritoryByName("Scadrial"), map.getTerritoryByName("Elantris"), 4);
        AttackOrder m4 = new AttackOrder(p2, map.getTerritoryByName("Roshar"), map.getTerritoryByName("Hogwarts"), 2);
        ox.pushCombat(m1);
        ox.pushCombat(m2);
        ox.pushCombat(m3);
        ox.pushCombat(m4);

        ArrayList<CombatOrder> combat1 = ox.getCombatsAt(map.getTerritoryByName("Elantris"));
        assertTrue(combat1.contains(new CombatOrder(p1,map.getTerritoryByName("Elantris"), 10)));
        assertTrue(combat1.contains(new CombatOrder(p2,map.getTerritoryByName("Elantris"), 4)));
        assertTrue(combat1.contains(new CombatOrder(p3,map.getTerritoryByName("Elantris"), 6)));

        ArrayList<CombatOrder> combat2 = ox.getCombatsAt(map.getTerritoryByName("Hogwarts"));
        assertTrue(combat2.contains(new CombatOrder(p2,map.getTerritoryByName("Hogwarts"), 2)));
        assertTrue(combat2.contains(new CombatOrder(p3,map.getTerritoryByName("Hogwarts"), 3)));
    }
    
    @Test
    public void test_doOneCombat(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createPlayerMap(3);
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        Player p3 = new Player("c");
        map.getTerritoryByName("Elantris").setOwner(p3);
        map.getTerritoryByName("Hogwarts").setOwner(p3);
        OrderExecuter ox = new OrderExecuter(map.getTerritories());
        int u1 = 10;
        int u2 = 6;
        CombatOrder c1 = new CombatOrder(p1,map.getTerritoryByName("Elantris"),u1);
        CombatOrder c2 = new CombatOrder(p3,map.getTerritoryByName("Elantris"), u2);
        for(int i = 0; i< 5; i++){
            if(ox.doOneUnitCombat(c1, c2)){
                u1--;
                assertEquals(u1,c1.getUnits());
                assertEquals(u2,c2.getUnits());
            }else{
                u2--;
                assertEquals(u1,c1.getUnits());
                assertEquals(u2,c2.getUnits());
            }
        }
    }

    @Test
    public void test_doAllCombat(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createPlayerMap(3);
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        Player p3 = new Player("c");
        Player p4 = new Player("d");
        Player p5 = new Player("e");
        Player p6 = new Player("f");
        p6.addTerritory(map.getTerritoryByName("Scadrial"));
        map.getTerritoryByName("Scadrial").setOwner(p6);
        OrderExecuter ox = new OrderExecuter(map.getTerritories());

        AttackOrder m1 = new AttackOrder(p1, map.getTerritoryByName("Elantris"), map.getTerritoryByName("Scadrial"), 5);
        AttackOrder m2 = new AttackOrder(p2, map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Scadrial"), 8);

        AttackOrder m3 = new AttackOrder(p3, map.getTerritoryByName("Oz"), map.getTerritoryByName("Scadrial"), 4);
        AttackOrder m4 = new AttackOrder(p4, map.getTerritoryByName("Roshar"), map.getTerritoryByName("Scadrial"), 2);

        AttackOrder m5 = new AttackOrder(p5, map.getTerritoryByName("Mordor"), map.getTerritoryByName("Scadrial"), 9);
        ox.pushCombat(m1);
        ox.pushCombat(m2);
        ox.pushCombat(m3);
        ox.pushCombat(m4);
        ox.pushCombat(m5);

        ox.doAllCombats();
        
    }
    
}
