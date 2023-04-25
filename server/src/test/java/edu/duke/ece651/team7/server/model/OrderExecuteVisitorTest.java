package edu.duke.ece651.team7.server.model;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;
import edu.duke.ece651.team7.shared.Unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void test_visitBasicOrderwithAircraft(){
        GameMap map = makeGameMap();
        OrderCostVisitor oc = new OrderCostVisitor(map);
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        int food1 = 1000;
        int food2 = 1000;
        int food3 = 1000;
        p1.getFood().addResource(food1);
        p2.getFood().addResource(food2);
        p3.getFood().addResource(food3);

        MoveOrder m1 = new MoveOrder(p1, true, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"), Level.CIVILIAN,5);
        assertThrows(IllegalArgumentException.class, ()->m1.accept(ox));

        map.getTerritoryByName("Narnia").addUnits(Arrays.asList(new Unit(Level.AIRBORNE, p1), new Unit(Level.AIRBORNE, p1), new Unit(Level.AIRBORNE, p1), 
        new Unit(Level.AIRBORNE, p1), new Unit(Level.AIRBORNE, p1), new Unit(Level.AIRBORNE, p1)));

        map.getTerritoryByName("Midkemia").setOwner(p3);
        MoveOrder m2 = new MoveOrder(p1, true, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Oz"), Level.CIVILIAN,5);
        assertThrows(IllegalArgumentException.class, ()->m2.accept(ox));

        MoveOrder m3 = new MoveOrder(p1, true, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Oz"), Level.AIRBORNE,5);
        assertThrows(IllegalArgumentException.class, ()->m3.accept(ox));

        p1.addAircraft(1);
        m3.accept(ox);
        food1-= m3.accept(oc).getAmount();
        assertEquals(11, map.getTerritoryByName("Narnia").getUnitsNumber());
        assertEquals(15, map.getTerritoryByName("Oz").getUnitsNumber());
        assertEquals(food1, p1.getFood().getAmount());

        AttackOrder a1 = new AttackOrder(p1, true, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Gondor"), Level.CIVILIAN,5);
        assertThrows(IllegalArgumentException.class, ()->a1.accept(ox));

        AttackOrder a2 = new AttackOrder(p1, false, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Gondor"), Level.AIRBORNE,5);
        assertThrows(IllegalArgumentException.class, ()->a2.accept(ox));

        AttackOrder a3 = new AttackOrder(p1, true, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Gondor"), Level.AIRBORNE,1);
        a3.accept(ox);
        food1-= a3.accept(oc).getAmount();
        assertEquals(10, map.getTerritoryByName("Narnia").getUnitsNumber());
        assertEquals(food1, p1.getFood().getAmount());
        assertEquals(1, ox.isInCombatPool(map.getTerritoryByName("Gondor")).getAttackUnitofPlayer(p1));
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
        assertThrows(IllegalArgumentException.class, ()->r1.accept(ox));
        ox.doAllResearch();
        assertEquals(Level.CAVALRY, p1.getCurrentMaxLevel());
        assertEquals(980, p1.getTech().getAmount());

        r1.accept(ox);
        assertEquals(Level.CAVALRY, p1.getCurrentMaxLevel());
        ox.doAllResearch();
        assertEquals(Level.TROOPER, p1.getCurrentMaxLevel());
        assertEquals(940, p1.getTech().getAmount());

        r1.accept(ox);
        assertEquals(Level.TROOPER, p1.getCurrentMaxLevel());
        ox.doAllResearch();
        assertEquals(Level.ARTILLERY, p1.getCurrentMaxLevel());
        assertEquals(860, p1.getTech().getAmount());


        r1.accept(ox);
        ox.doAllResearch();
        assertEquals(Level.AIRBORNE, p1.getCurrentMaxLevel());
        assertEquals(700, p1.getTech().getAmount());

        r1.accept(ox);
        assertEquals(Level.AIRBORNE, p1.getCurrentMaxLevel());
        assertEquals(380, p1.getTech().getAmount());

        ox.doAllResearch();
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
        assertEquals(6, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.CIVILIAN,p1));
        assertEquals(4, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.INFANTRY,p1));

        System.out.println(p1.getTech().getAmount());
        UpgradeOrder u3 = new UpgradeOrder(p1, map.getTerritoryByName("Narnia"), Level.INFANTRY, Level.CAVALRY, 4);
        assertThrows(IllegalArgumentException.class, ()->u3.accept(ox));
        System.out.println(p1.getTech().getAmount());

        ResearchOrder r1 = new ResearchOrder(p1);
        tech1 -= r1.accept(oc).getAmount();
        r1.accept(ox);
        ox.doAllResearch();
        assertEquals(tech1, p1.getTech().getAmount());

        u3.accept(ox);
        tech1 = tech1 - u3.accept(oc).getAmount();
        assertEquals(tech1, p1.getTech().getAmount());
        assertEquals(6, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.CIVILIAN, p1));
        assertEquals(0, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.INFANTRY, p1));
        assertEquals(4, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.CAVALRY, p1));
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

        map.getTerritoryByName("Roshar").upgradeUnits(Level.CIVILIAN, Level.TROOPER, 4);
        
        MoveOrder m4 = new MoveOrder(p2,map.getTerritoryByName("Roshar"),map.getTerritoryByName("Elantris"), Level.TROOPER, 3,Level.CIVILIAN,5);
        m4.accept(ox);
        assertEquals(1, map.getTerritoryByName("Roshar").getUnitsNumberByLevel(Level.CIVILIAN));
        assertEquals(1, map.getTerritoryByName("Roshar").getUnitsNumberByLevel(Level.TROOPER));
        assertEquals(2, map.getTerritoryByName("Roshar").getUnitsNumber());

        assertEquals(18, map.getTerritoryByName("Elantris").getUnitsNumber());
        assertEquals(15, map.getTerritoryByName("Elantris").getUnitsNumberByLevel(Level.CIVILIAN));
        assertEquals(3, map.getTerritoryByName("Elantris").getUnitsNumberByLevel(Level.TROOPER));

    };

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

        p1.addAlliance(p3);
        p3.addAlliance(p1);


        map.getTerritoryByName("Narnia").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3))));
        map.getTerritoryByName("Oz").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3), new Unit(p3))));

        map.getTerritoryByName("Gondor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1))));
        map.getTerritoryByName("Mordor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1), new Unit(p1))));

        AttackOrder a6 = new AttackOrder(p1,map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Scadrial"),2);
        AttackOrder a7 = new AttackOrder(p3,map.getTerritoryByName("Hogwarts"), map.getTerritoryByName("Scadrial"),2);
        ox.visit(a6);
        ox.visit(a7);
        assertEquals(4,ox.isInCombatPool(a6.dest).getAttackUnitofPlayer(p1));

        AttackOrder a5 = new AttackOrder(p1,map.getTerritoryByName("Gondor"), map.getTerritoryByName("Mordor"),2);
        ox.visit(a5);
        assertFalse(p1.isAlliance(p3));
        assertEquals(1, ox.isInCombatPool(map.getTerritoryByName("Gondor")).getAttackUnitofPlayer(p1));
        assertEquals(6, ox.isInCombatPool(map.getTerritoryByName("Mordor")).getAttackUnitofPlayer(p1));
        assertEquals(17,  map.getTerritoryByName("Gondor").getUnitsNumber(p3));
    }

    @Test
    public void test_visitAllianceOrder(){
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

        AllianceOrder a1 = new AllianceOrder(p1, p2);
        a1.accept(ox);
        AllianceOrder a2 = new AllianceOrder(p2, p3);
        a2.accept(ox);
        AllianceOrder a3 = new AllianceOrder(p3, p1);
        a3.accept(ox);

        ox.resolveAllAlliance();
        assertNull(p1.getAlliance());
        assertNull(p2.getAlliance());
        assertNull(p3.getAlliance());

        AllianceOrder a4 = new AllianceOrder(p1, p2);
        a4.accept(ox);
        AllianceOrder a5 = new AllianceOrder(p2, p1);
        a5.accept(ox);
        AllianceOrder a6 = new AllianceOrder(p3, p1);
        a6.accept(ox);

        ox.resolveAllAlliance();
        assertEquals(p2, p1.getAlliance());
        assertEquals(p1, p2.getAlliance());
        assertNull(p3.getAlliance());

        AllianceOrder a7 = new AllianceOrder(p1, p1);
        assertThrows(IllegalArgumentException.class, ()->a7.accept(ox));

        AllianceOrder a8 = new AllianceOrder(p1, p2);
        a8.accept(ox);
        AllianceOrder a9 = new AllianceOrder(p1, p3);
        assertThrows(IllegalArgumentException.class, ()->a9.accept(ox));
    }

    @Test
    public void test_visitManufactureOrder(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        OrderCostVisitor oc = new OrderCostVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();

        int tech1 = 10000;

        p1.getTech().addResource(tech1);

        ManufactureOrder o1 = new ManufactureOrder(p1, true, 3);
        ManufactureOrder o2 = new ManufactureOrder(p1, false, 3);
        o1.accept(ox);
        assertEquals(3, p1.getBomb());
        tech1 -= oc.visit(o1).getAmount();
        assertEquals(tech1, p1.getTech().getAmount());

        o2.accept(ox);
        assertEquals(3, p1.getAircraft().size());
        tech1 -= oc.visit(o2).getAmount();
        assertEquals(tech1, p1.getTech().getAmount());

        ManufactureOrder o3 = new ManufactureOrder(p1, false, 20);
        assertThrows(IllegalArgumentException.class, ()->o3.accept(ox));
    }

    @Test
    public void test_AllianceMoveOrder(){
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
        AllianceOrder a1 = new AllianceOrder(p1, p2);
        a1.accept(ox);
        AllianceOrder a2 = new AllianceOrder(p2, p1);
        a2.accept(ox);
        ox.resolveAllAlliance();

        MoveOrder m1 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Scadrial"), 5);
        m1.accept(ox);
        food1-= m1.accept(oc).getAmount();
        assertEquals(5, map.getTerritoryByName("Narnia").getUnitsNumber());
        assertEquals(15, map.getTerritoryByName("Scadrial").getUnitsNumber());
        assertEquals(food1, p1.getFood().getAmount());

        MoveOrder m2 = new MoveOrder(p2, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Gondor"), 4);
        assertThrows(IllegalArgumentException.class, () -> m2.accept(ox));
    }

    @Test
    public void test_doAllResearch(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        p1.getTech().addResource(1000);
        p2.getTech().addResource(1000);
        p3.getTech().addResource(1000);

        ResearchOrder r1 = new ResearchOrder(p1);
        ResearchOrder r2 = new ResearchOrder(p2);
        ResearchOrder r3 = new ResearchOrder(p3);
        r1.accept(ox);
        r2.accept(ox);
        r3.accept(ox);
        ox.doAllResearch();
        assertEquals(Level.CAVALRY, p1.getCurrentMaxLevel());
        assertEquals(980, p1.getTech().getAmount());
        assertEquals(Level.CAVALRY, p2.getCurrentMaxLevel());
        assertEquals(980, p2.getTech().getAmount());
        assertEquals(Level.CAVALRY, p3.getCurrentMaxLevel());
        assertEquals(980, p3.getTech().getAmount());
        r1.accept(ox);
        r2.accept(ox);
        r3.accept(ox);
        ox.doAllResearch();
        assertEquals(Level.TROOPER, p1.getCurrentMaxLevel());
        assertEquals(940, p1.getTech().getAmount());
        assertEquals(Level.TROOPER, p2.getCurrentMaxLevel());
        assertEquals(940, p2.getTech().getAmount());
        assertEquals(Level.TROOPER, p3.getCurrentMaxLevel());
        assertEquals(940, p3.getTech().getAmount());
    }

    
    @Test
    public void test_collectResource(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        // OrderCostVisitor oc = new OrderCostVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        assertEquals(0, p1.getTech().getAmount());
        assertEquals(0, p2.getTech().getAmount());
        assertEquals(0, p3.getTech().getAmount());
        assertEquals(0, p1.getFood().getAmount());
        assertEquals(0, p2.getFood().getAmount());
        assertEquals(0, p3.getFood().getAmount());

        ox.collectAllResource();
        assertEquals(3, p1.getFood().getAmount());
        assertEquals(3, p2.getFood().getAmount());
        assertEquals(3, p3.getFood().getAmount());
        assertEquals(3, p1.getTech().getAmount());
        assertEquals(3, p2.getTech().getAmount());
        assertEquals(3, p3.getTech().getAmount());

    }

    @Test
    public void test_retreat(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        p1.addAlliance(p3);
        p3.addAlliance(p1);
        map.getTerritoryByName("Narnia").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3))));
        map.getTerritoryByName("Oz").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3), new Unit(p3))));

        map.getTerritoryByName("Gondor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1))));
        map.getTerritoryByName("Mordor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1), new Unit(p1))));

        assertEquals(3, map.getTerritoryByName("Narnia").getUnitsNumber(p3));
        assertEquals(4, map.getTerritoryByName("Oz").getUnitsNumber(p3));

        ox.retreat(p1, p3);
        assertEquals(0, map.getTerritoryByName("Narnia").getUnitsNumber(p3));
        assertEquals(10, map.getTerritoryByName("Narnia").getUnitsNumber(p1));
        assertEquals(0, map.getTerritoryByName("Oz").getUnitsNumber(p3));

        assertEquals(17, map.getTerritoryByName("Gondor").getUnitsNumber(p3));
        assertEquals(3, map.getTerritoryByName("Gondor").getUnitsNumber(p1));
    }

    @Test
    public void test_breakAlliance(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        p1.addAlliance(p3);
        p3.addAlliance(p1);
        map.getTerritoryByName("Narnia").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3))));
        map.getTerritoryByName("Oz").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3), new Unit(p3))));

        map.getTerritoryByName("Gondor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1))));
        map.getTerritoryByName("Mordor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1), new Unit(p1))));

        ox.breakAlliance(p1, p3);
        assertEquals(0, map.getTerritoryByName("Narnia").getUnitsNumber(p3));
        assertEquals(10, map.getTerritoryByName("Narnia").getUnitsNumber(p1));
        assertEquals(0, map.getTerritoryByName("Oz").getUnitsNumber(p3));

        assertEquals(17, map.getTerritoryByName("Gondor").getUnitsNumber(p3));
        assertEquals(0, map.getTerritoryByName("Gondor").getUnitsNumber(p1));

        assertEquals(3,ox.isInCombatPool(map.getTerritoryByName("Gondor")).getAttackUnitofPlayer(p1));
        assertEquals(4,ox.isInCombatPool(map.getTerritoryByName("Mordor")).getAttackUnitofPlayer(p1));
    }

    @Test
    public void test_equipUnits(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        ArrayList<Unit> units = new ArrayList<>(Arrays.asList(new Unit(Level.ULTRON, p3), new Unit(Level.ULTRON, p3), new Unit(Level.ULTRON, p3), new Unit(Level.ULTRON, p3)));

        p3.modifyBombAmount(4);
        ox.equipUnits(units, 3);
        assertEquals(1, p3.getBomb());
        assertTrue(units.get(0).hasBomb());
        assertTrue(units.get(1).hasBomb());
        assertTrue(units.get(2).hasBomb());
        assertFalse(units.get(3).hasBomb());
    }


    @Test
    public void test_visitAttackOrderWithBomb(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = spy(new OrderExecuteVisitor(map));
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

        map.getTerritoryByName("Narnia").addUnits(Arrays.asList(new Unit(Level.ULTRON, p1),new Unit(Level.ULTRON, p1),new Unit(Level.ULTRON, p1),
                                                                     new Unit(Level.ULTRON, p1),new Unit(Level.ULTRON, p1),new Unit(Level.ULTRON, p1),
                                                                     new Unit(Level.CAVALRY, p1),new Unit(Level.CAVALRY, p1)));
        AttackOrder a1 = new AttackOrder(p1, false, 3, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Elantris"), Level.CAVALRY, 10);
        assertThrows(IllegalArgumentException.class, () -> a1.accept(ox));


        p1.modifyBombAmount(5);
        AttackOrder a2 = new AttackOrder(p1, false, 3, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Elantris"), Level.ULTRON, 5);
        a2.accept(ox);
        food1 -= a2.accept(oc).getAmount();
        assertNotNull(ox.isInCombatPool(a2.dest));
        assertEquals(food1, p1.getFood().getAmount());
        assertEquals(2, p1.getBomb());
        assertEquals(13, map.getTerritoryByName("Narnia").getUnitsNumber());
        assertEquals(10, map.getTerritoryByName("Elantris").getUnitsNumber());
    }

    @Test
    public void test_doAllCombat(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        // OrderCostVisitor oc = new OrderCostVisitor(map);
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        // p2.addTerritory(map.getTerritoryByName("Scadrial"));
        // p3.addTerritory(map.getTerritoryByName("Mordor"));
        Player p4 = new Player("green");
        Player p5 = new Player("blue");

        map.getTerritoryByName("Elantris").setOwner(p4);
        p4.addTerritory(map.getTerritoryByName("Elantris"));
        map.getTerritoryByName("Elantris").addUnits(new ArrayList<>(Arrays.asList(new Unit(p4), new Unit(p4), new Unit(p4), new Unit(p4))));

        map.getTerritoryByName("Roshar").setOwner(p5);
        p5.addTerritory(map.getTerritoryByName("Roshar"));
        map.getTerritoryByName("Roshar").addUnits(new ArrayList<>(Arrays.asList(new Unit(p5), new Unit(p5), new Unit(p5), new Unit(p5))));


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
        assertEquals(10, map.getTerritoryByName("Hogwarts").getUnitsNumber());
        assertEquals(0, map.getTerritoryByName("Midkemia").getUnitsNumber());
        assertEquals(11, map.getTerritoryByName("Elantris").getUnitsNumber());
    }
    
    @Test
    public void test_resolveOneRound(){
        GameMap map = makeGameMap();
        OrderExecuteVisitor ox = new OrderExecuteVisitor(map);
        // Player p1 = map.getTerritoryByName("Narnia").getOwner();
        // Player p2 = map.getTerritoryByName("Elantris").getOwner();
        // Player p3 = map.getTerritoryByName("Gondor").getOwner();


        ox.resolveOneRound();
        assertEquals(11, map.getTerritoryByName("Narnia").getUnitsNumberByLevel(Level.CIVILIAN));
        assertEquals(11, map.getTerritoryByName("Elantris").getUnitsNumber());
        assertEquals(11, map.getTerritoryByName("Gondor").getUnitsNumber());
    }
    
}
