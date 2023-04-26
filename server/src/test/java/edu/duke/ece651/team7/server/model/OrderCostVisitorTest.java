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


public class OrderCostVisitorTest {
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
    public void test_constructor(){
        GameMap map = makeGameMap();
        OrderCostVisitor costVisitor = new OrderCostVisitor(map);
    }
    
    @Test
    public void test_moveorder(){
        GameMap map = makeGameMap();
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        OrderCostVisitor costVisitor = new OrderCostVisitor(map);
        MoveOrder m1 = new MoveOrder(p3, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Hogwarts"), 3);
        assertEquals(12, costVisitor.visit(m1).getAmount());

        MoveOrder m2 = new MoveOrder(p3, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Mordor"), 2);
        assertEquals(4, costVisitor.visit(m2).getAmount());

        MoveOrder m3 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Oz"), 4);
        assertEquals(16, costVisitor.visit(m3).getAmount());
    }

    @Test
    public void test_attackOrder(){
        GameMap map = makeGameMap();
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        OrderCostVisitor costVisitor = new OrderCostVisitor(map);

        AttackOrder m1 = new AttackOrder(p3, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Oz"), 8);
        assertEquals(24, costVisitor.visit(m1).getAmount());

        AttackOrder m2 = new AttackOrder(p3, map.getTerritoryByName("Scadrial"), map.getTerritoryByName("Mordor"), 2);
        assertEquals(6, costVisitor.visit(m2).getAmount());

        AttackOrder m3 = new AttackOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Elantris"), 4);
        assertEquals(12, costVisitor.visit(m3).getAmount());
    }

    @Test
    public void test_UpgradeOrder(){
        GameMap map = makeGameMap();
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        OrderCostVisitor costVisitor = new OrderCostVisitor(map);

        UpgradeOrder u1 = new UpgradeOrder(p3, map.getTerritoryByName("Narnia"), Level.CIVILIAN, Level.AIRBORNE, 3);
        assertEquals(270, costVisitor.visit(u1).getAmount());

        UpgradeOrder u2 = new UpgradeOrder(p3, map.getTerritoryByName("Narnia"), Level.INFANTRY, Level.ULTRON, 3);
        assertEquals(411, costVisitor.visit(u2).getAmount());

        UpgradeOrder u3 = new UpgradeOrder(p3, map.getTerritoryByName("Narnia"), Level.CAVALRY, Level.ARTILLERY, 3);
        assertEquals(132, costVisitor.visit(u3).getAmount());
    }

    @Test
    public void test_ResearchOrder(){
        GameMap map = makeGameMap();
        OrderCostVisitor costVisitor = new OrderCostVisitor(map);

        Player playerA = new Player("GroupA", Level.INFANTRY);
        Player playerB = new Player("GroupB", Level.AIRBORNE);
        Player playerC = new Player("GroupC", Level.CAVALRY);
        Player playerD = new Player("GroupC", Level.ULTRON);

        ResearchOrder r1 = new ResearchOrder(playerA);
        assertEquals(20, costVisitor.visit(r1).getAmount());

        ResearchOrder r2 = new ResearchOrder(playerB);
        assertEquals(320, costVisitor.visit(r2).getAmount());

        ResearchOrder r3 = new ResearchOrder(playerC);
        assertEquals(40, costVisitor.visit(r3).getAmount());

        ResearchOrder r4 = new ResearchOrder(playerD);
        assertThrows(IllegalArgumentException.class, ()->costVisitor.visit(r4));
    }

    @Test
    public void test_AllianceOrder(){
        GameMap map = makeGameMap();
        OrderCostVisitor costVisitor = new OrderCostVisitor(map);
        Player playerA = new Player("GroupA", Level.INFANTRY);
        Player playerB = new Player("GroupB", Level.AIRBORNE);
        AllianceOrder o1 = new AllianceOrder(playerA, playerB);
        assertNull(costVisitor.visit(o1));
    }

    @Test
    public void test_ManufactureOrder(){


    }

}
