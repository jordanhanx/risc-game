package edu.duke.ece651.team7.server.model;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderExecuteVisitorTest {

    private GameMap makeGameMap(){
        Player playerA = new Player("GroupA");
        Player playerB = new Player("GroupB");
        Player playerC = new Player("GroupC");
        Territory territory1 = new Territory("Narnia", playerA, 10);
        Territory territory2 = new Territory("Elantris", playerB, 10);
        Territory territory3 = new Territory("Midkemia", playerA, 10);
        Territory territory4 = new Territory("Oz", playerA, 10);
        Territory territory5 = new Territory("Scadrial", playerB, 10);
        Territory territory6 = new Territory("Roshar", playerB, 10);
        Territory territory7 = new Territory("Gondor", playerC, 10);
        Territory territory8 = new Territory("Mordor", playerC, 10);
        Territory territory9 = new Territory("Hogwarts", playerC, 10);

        playerA.addTerritory(territory1);
        playerA.addTerritory(territory3);
        playerA.addTerritory(territory4);

        playerB.addTerritory(territory2);
        playerB.addTerritory(territory5);
        playerB.addTerritory(territory6);

        playerC.addTerritory(territory7);
        playerC.addTerritory(territory8);
        playerC.addTerritory(territory9);

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
    public void test_visitResearchOrder(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        p1.getTech().addResource(1000);
        p2.getTech().addResource(1000);
        p3.getTech().addResource(1000);

        ResearchOrder r1 = new ResearchOrder(p1);
        assertEquals(Level.INFANTRY, p1.getCurrentMaxLevel());
        r1.accept(ox);
        assertEquals(Level.CAVALRY, p1.getCurrentMaxLevel());
        assertEquals(980, p1.getTech().getAmount());

        r1.accept(ox);
        assertEquals(Level.TROOPER, p1.getCurrentMaxLevel());
        assertEquals(940, p1.getTech().getAmount());

        r1.accept(ox);
        assertEquals(Level.ARTILLERY, p1.getCurrentMaxLevel());
        assertEquals(860, p1.getTech().getAmount());


        r1.accept(ox);
        assertEquals(Level.AIRFORCE, p1.getCurrentMaxLevel());
        assertEquals(700, p1.getTech().getAmount());

        r1.accept(ox);
        assertEquals(Level.ULTRON, p1.getCurrentMaxLevel());
        assertEquals(380, p1.getTech().getAmount());

        assertThrows(IllegalArgumentException.class, ()->r1.accept(ox));
        assertEquals(Level.ULTRON, p1.getCurrentMaxLevel());
        assertEquals(380, p1.getTech().getAmount());

    }

    @Test
    public void test_visitUpgradeOrder(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        OrderCostVisitor oc = new OrderCostVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        int tech1 = 1000;
        int tech2 = 1000;
        int tech3 = 1000;
        p1.getTech().addResource(tech1);
        p2.getTech().addResource(tech2);
        p3.getTech().addResource(tech3);

        UpgradeOrder u1 = new UpgradeOrder(p1, map.getTerritoryByName("Narnia"), Level.ARTILLERY, Level.ULTRON, 1);
        assertThrows(IllegalArgumentException.class, ()->u1.accept(ox));

        UpgradeOrder u2 = new UpgradeOrder(p1, map.getTerritoryByName("Narnia"), Level.CIVILIAN, Level.INFANTRY, 4);
        u2.accept(ox);
        tech1 = tech1-u2.accept(oc).getAmount();
        assertEquals(tech1, p1.getTech().getAmount());
        assertEquals(6, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.CIVILIAN));
        assertEquals(4, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.INFANTRY));

        System.out.println(p1.getTech().getAmount());
        UpgradeOrder u3 = new UpgradeOrder(p1, map.getTerritoryByName("Narnia"), Level.INFANTRY, Level.CAVALRY, 4);
        assertThrows(IllegalArgumentException.class, ()->u3.accept(ox));
        System.out.println(p1.getTech().getAmount());

        ResearchOrder r1 = new ResearchOrder(p1);
        tech1 -= r1.accept(oc).getAmount();
        r1.accept(ox);
        assertEquals(tech1, p1.getTech().getAmount());

        u3.accept(ox);
        tech1 = tech1 - u3.accept(oc).getAmount();
        assertEquals(tech1, p1.getTech().getAmount());
        assertEquals(6, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.CIVILIAN));
        assertEquals(0, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.INFANTRY));
        assertEquals(4, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.CAVALRY));
    }

    @Test
    public void test_visitMoveOrder(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        OrderCostVisitor oc = new OrderCostVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        int food1 = 1000;
        int food2 = 1000;
        int food3 = 1000;
        p1.getFood().addResource(food1);
        p2.getFood().addResource(food2);
        p3.getFood().addResource(food3);

        MoveOrder m1 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"), 5);
        m1.accept(ox);
        food1-= m1.accept(oc).getAmount();
        assertEquals(5, map.getTerritoryByName("Narnia").getUnitsNumber());
        assertEquals(15, map.getTerritoryByName("Midkemia").getUnitsNumber());
        assertEquals(food1, p1.getFood().getAmount());

        MoveOrder m2 = new MoveOrder(p2, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Mordor"), 4);
        assertThrows(IllegalArgumentException.class, () -> m2.accept(ox));

        MoveOrder m3 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"), 0);
        assertThrows(IllegalArgumentException.class, () -> m3.accept(ox));
    };



    // @Test
    // public void test_PushCombat(){
    //     GameMap map = makeGameMap();
    //     OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
    //     Player p1 = map.getTerritoryByName("Narnia").getOwner();
    //     Player p2 = map.getTerritoryByName("Elantris").getOwner();
    //     Player p3 = map.getTerritoryByName("Gondor").getOwner();

    //     AttackOrder a1 = new AttackOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Elantris"), 10);
    //     ox.pushCombat(a1);
    //     assertNotNull(ox.isInCombatPool(a1.dest));

    //     AttackOrder a2 = new AttackOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Gondor"), 10);
    //     assertThrows(IllegalArgumentException.class, () -> ox.pushCombat(a2));
    //     assertNull(ox.isInCombatPool(map.getTerritoryByName("Gondor")));

    //     AttackOrder a3 = new AttackOrder(p1,  map.getTerritoryByName("Narnia"),  map.getTerritoryByName("Elantris"), 0);
    //     assertThrows(IllegalArgumentException.class, () -> ox.pushCombat(a3));

    //     AttackOrder a4 = new AttackOrder(p1,  map.getTerritoryByName("Midkemia"),  map.getTerritoryByName("Elantris"), 4);
    //     ox.pushCombat(a4);
    //     Combat targetCombat = ox.isInCombatPool(a4.dest);
    //     assertEquals(14,targetCombat.getAttackUnitofPlayer(p1));

    // }

    @Test
    public void test_visitAttackOrder(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        OrderCostVisitor oc = new OrderCostVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        int food1 = 1000;
        int food2 = 1000;
        int food3 = 1000;
        p1.getFood().addResource(food1);
        p2.getFood().addResource(food2);
        p3.getFood().addResource(food3);

        AttackOrder a1 = new AttackOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Elantris"), 10);
        a1.accept(ox);
        food1 -= a1.accept(oc).getAmount();
        assertNotNull(ox.isInCombatPool(a1.dest));
        assertEquals(food1, p1.getFood().getAmount());
        assertEquals(0, map.getTerritoryByName("Narnia").getUnitsNumber());
        assertEquals(10, map.getTerritoryByName("Elantris").getUnitsNumber());

        AttackOrder a2 = new AttackOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Gondor"), 10);
        assertThrows(IllegalArgumentException.class, () -> a2.accept(ox));
        assertNull(ox.isInCombatPool(map.getTerritoryByName("Gondor")));

        AttackOrder a3 = new AttackOrder(p1,  map.getTerritoryByName("Narnia"),  map.getTerritoryByName("Elantris"), 0);
        assertThrows(IllegalArgumentException.class, () -> a3.accept(ox));

        AttackOrder a4 = new AttackOrder(p1,  map.getTerritoryByName("Midkemia"),  map.getTerritoryByName("Elantris"), 4);
        a4.accept(ox);
        Combat targetCombat = ox.isInCombatPool(a4.dest);
        assertEquals(14,targetCombat.getAttackUnitofPlayer(p1));

    }

    @Test
    public void test_doAllCombat(){

        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        OrderCostVisitor oc = new OrderCostVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        // p2.addTerritory(map.getTerritoryByName("Scadrial"));
        // p3.addTerritory(map.getTerritoryByName("Mordor"));
        Player p4 = new Player("green");
        Player p5 = new Player("blue");

        map.getTerritoryByName("Elantris").setOwner(p4);
        p4.addTerritory(map.getTerritoryByName("Elantris"));

        map.getTerritoryByName("Roshar").setOwner(p5);
        p5.addTerritory(map.getTerritoryByName("Roshar"));

        int food1 = 1000;
        int food2 = 1000;
        int food3 = 1000;
        int food4 = 1000;
        int food5 = 1000;
        p1.getFood().addResource(food1);
        p2.getFood().addResource(food2);
        p3.getFood().addResource(food3);
        p4.getFood().addResource(food4);
        p5.getFood().addResource(food5);

        AttackOrder m1 = new AttackOrder(p1, map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Scadrial"), 10);
        AttackOrder m2 = new AttackOrder(p4, map.getTerritoryByName("Elantris"), map.getTerritoryByName("Scadrial"), 3);
        AttackOrder m3 = new AttackOrder(p5, map.getTerritoryByName("Roshar"), map.getTerritoryByName("Scadrial"), 2);

        AttackOrder m4 = new AttackOrder(p3, map.getTerritoryByName("Mordor"), map.getTerritoryByName("Scadrial"), 3);


        AttackOrder m5 = new AttackOrder(p1, map.getTerritoryByName("Oz"), map.getTerritoryByName("Mordor"), 5);
        AttackOrder m6 = new AttackOrder(p2, map.getTerritoryByName("Scadrial"), map.getTerritoryByName("Mordor"), 1);
        m1.accept(ox);
        m2.accept(ox);
        m3.accept(ox);
        m4.accept(ox);
        m5.accept(ox);
        m6.accept(ox);
        // System.out.println(map.getTerritoryByName("Scadrial").getOwner().getName());
        // System.out.println(map.getTerritoryByName("Mordor").getOwner().getName());
        ox.doAllCombats();
        
        // System.out.println(map.getTerritoryByName("Scadrial").getOwner().getName());
        // System.out.println(map.getTerritoryByName("Mordor").getOwner().getName());
        assertNull(ox.isInCombatPool(m4.dest));
        // assertNull(ox.isInCombatPool(m6.getDest()));
        assertEquals(11, map.getTerritoryByName("Hogwarts").getUnitsNumber());
        assertEquals(1, map.getTerritoryByName("Midkemia").getUnitsNumber());
        assertEquals(8, map.getTerritoryByName("Elantris").getUnitsNumber());
    }
    
    
}
