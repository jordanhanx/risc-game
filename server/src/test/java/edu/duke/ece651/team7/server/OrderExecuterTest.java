package edu.duke.ece651.team7.server;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.MapFactory;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.TextMapFactory;

import static org.junit.jupiter.api.Assertions.*;

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
        GameMap map = factory.createMap();
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
    public void test_isInAttackPool(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createMap();
        OrderExecuter ox = new OrderExecuter(map.getTerritories());

        
        AttackOrder m1 = new AttackOrder(map.getTerritoryByName("Oz").getOwner(), map.getTerritoryByName("Oz"), map.getTerritoryByName("Scadrial"), 5);
        AttackOrder m2 = new AttackOrder(map.getTerritoryByName("Midkemia").getOwner(), map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Scadrial"), 5);
       

        AttackOrder m3 = new AttackOrder(map.getTerritoryByName("Mordor").getOwner(), map.getTerritoryByName("Mordor"), map.getTerritoryByName("Scadrial"), 1);
        AttackOrder m4 = new AttackOrder(map.getTerritoryByName("Roshar").getOwner(), map.getTerritoryByName("Roshar"), map.getTerritoryByName("Hogwarts"), 2);
        AttackOrder m5 = new AttackOrder(map.getTerritoryByName("Scadrial").getOwner(), map.getTerritoryByName("Scadrial"), map.getTerritoryByName("Hogwarts"), 1);

        AttackOrder m6 = new AttackOrder(map.getTerritoryByName("Hogwarts").getOwner(), map.getTerritoryByName("Hogwarts"), map.getTerritoryByName("Scadrial"), 1);

        ox.pushCombat(m1);
        ox.pushCombat(m4);

        assertNotNull(ox.isInAttackPool(m2));
        assertNull(ox.isInAttackPool(m3));
        assertNotNull(ox.isInAttackPool(m5));
        assertNull(ox.isInAttackPool(m6));
    }



    @Test
    public void test_PushCombat(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createMap();
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
        GameMap map = factory.createMap();
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
    public void test_updateCombatList(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createMap();
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        Player p3 = new Player("c");
        Player p4 = new Player("d");
        Player p5 = new Player("e");
        Player p6 = new Player("f");
        p6.addTerritory(map.getTerritoryByName("Scadrial"));
        map.getTerritoryByName("Scadrial").setOwner(p6);

        map.getTerritoryByName("Midkemia").setOwner(p1);
        map.getTerritoryByName("Oz").setOwner(p2);
        p2.addTerritory(map.getTerritoryByName("Oz"));

        map.getTerritoryByName("Roshar").setOwner(p3);
        map.getTerritoryByName("Mordor").setOwner(p4);
        map.getTerritoryByName("Elantris").setOwner(p5);

        OrderExecuter ox = new OrderExecuter(map.getTerritories());

        AttackOrder m1 = new AttackOrder(p5, map.getTerritoryByName("Elantris"), map.getTerritoryByName("Scadrial"), 5);
        AttackOrder m2 = new AttackOrder(p1, map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Scadrial"), 8);
        AttackOrder m3 = new AttackOrder(p2, map.getTerritoryByName("Oz"), map.getTerritoryByName("Scadrial"), 4);
        AttackOrder m4 = new AttackOrder(p3, map.getTerritoryByName("Roshar"), map.getTerritoryByName("Scadrial"), 2);
        AttackOrder m5 = new AttackOrder(p4, map.getTerritoryByName("Mordor"), map.getTerritoryByName("Scadrial"), 9);
        ox.pushCombat(m1);
        ox.pushCombat(m2);
        ox.pushCombat(m3);
        ox.pushCombat(m4);
        ox.pushCombat(m5);

        ArrayList<CombatOrder> combats = ox.getCombatsAt(map.getTerritoryByName("Scadrial"));
        assertEquals(1, ox.updateCombatList(0, 1, combats));
        assertEquals(2, ox.updateCombatList(1, 2, combats));
        assertEquals(3, ox.updateCombatList(2, 3, combats));
        assertEquals(4, ox.updateCombatList(3, 4, combats));
        assertEquals(5, ox.updateCombatList(4, 5, combats));
        assertEquals(0, ox.updateCombatList(5, 0, combats));

        combats.get(5).decreaseUnits(9);
        assertEquals(0, ox.updateCombatList(4, 5, combats));
        assertEquals(5, combats.size());

        combats.get(4).decreaseUnits(2);
        assertEquals(0, ox.updateCombatList(4, 0, combats));
        assertEquals(4, combats.size());

        combats.get(2).decreaseUnits(8);
        assertEquals(2, ox.updateCombatList(2, 3, combats));
        assertEquals(3, combats.size());
    }

    @Test
    public void test_doAllCombat(){
        MapFactory factory = new TextMapFactory();
        GameMap map = factory.createMap();
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        Player p3 = new Player("c");
        Player p4 = new Player("d");
        Player p5 = new Player("e");
        Player p6 = new Player("f");
        p6.addTerritory(map.getTerritoryByName("Scadrial"));
        map.getTerritoryByName("Scadrial").setOwner(p6);

        map.getTerritoryByName("Midkemia").setOwner(p1);
        map.getTerritoryByName("Oz").setOwner(p2);
        p2.addTerritory(map.getTerritoryByName("Oz"));

        map.getTerritoryByName("Roshar").setOwner(p3);
        map.getTerritoryByName("Mordor").setOwner(p4);
        map.getTerritoryByName("Elantris").setOwner(p5);
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
