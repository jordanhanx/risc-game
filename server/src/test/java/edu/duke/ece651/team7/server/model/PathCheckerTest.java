package edu.duke.ece651.team7.server.model;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.*;


public class PathCheckerTest {

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
    public void test_checkMyruleMove(){
        OrderRuleChecker checker = new PathChecker(null);
        GameMap map = makeGameMap();
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        MoveOrder m1 = new MoveOrder(p2, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Narnia"), 3);
        assertEquals("Access Denied: source Territory does not belong to you/your alliance", checker.checkOrderValidity(map, m1));

        MoveOrder m2 = new MoveOrder(p1, map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Narnia"), 3);
        assertNull(checker.checkOrderValidity(map, m2));

        MoveOrder m3 = new MoveOrder(p3, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Narnia"), 3);
        assertEquals("Access Denied: destination Territory does not belong to you/your alliance", checker.checkOrderValidity(map, m3));

        map.getTerritoryByName("Roshar").setOwner(p1);
        MoveOrder m4 = new MoveOrder(p1, map.getTerritoryByName("Roshar"), map.getTerritoryByName("Narnia"), 3);
        assertEquals("Path does not exists between these two Territories", checker.checkOrderValidity(map, m4));

        map.getTerritoryByName("Roshar").setOwner(p2);

        //Test alliance path
        p1.addAlliance(p3);
        p3.addAlliance(p1);
        MoveOrder m5 = new MoveOrder(p3, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Narnia"), 3);
        assertNull(checker.checkOrderValidity(map, m5));

        MoveOrder m6 = new MoveOrder(p3, map.getTerritoryByName("Oz"), map.getTerritoryByName("Narnia"), 3);
        assertNull(checker.checkOrderValidity(map, m6));

        map.getTerritoryByName("Narnia").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3))));
        map.getTerritoryByName("Oz").addUnits(new ArrayList<>(Arrays.asList(new Unit(p3), new Unit(p3), new Unit(p3), new Unit(p3))));

        map.getTerritoryByName("Gondor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1))));
        map.getTerritoryByName("Mordor").addUnits(new ArrayList<>(Arrays.asList(new Unit(p1), new Unit(p1), new Unit(p1), new Unit(p1))));

        MoveOrder m7 = new MoveOrder(p1, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Mordor"), 3);
        assertNull(checker.checkOrderValidity(map, m7));
    }

    @Test
    public void test_BasicOrderwithAircraft(){
        OrderRuleChecker checker = new PathChecker(null);
        GameMap map = makeGameMap();


        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        MoveOrder m1 = new MoveOrder(p2, true, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Narnia"),Level.CIVILIAN, 3);
        assertEquals("Access Denied: source Territory does not belong to you/your alliance", checker.checkOrderValidity(map, m1));

        MoveOrder m2 = new MoveOrder(p1,  true,map.getTerritoryByName("Midkemia"), map.getTerritoryByName("Narnia"),Level.CIVILIAN, 3);
        assertNull(checker.checkOrderValidity(map, m2));

        MoveOrder m3 = new MoveOrder(p3,  true,map.getTerritoryByName("Gondor"), map.getTerritoryByName("Narnia"),Level.CIVILIAN, 3);
        assertEquals("Access Denied: destination Territory does not belong to you/your alliance", checker.checkOrderValidity(map, m3));


        map.getTerritoryByName("Roshar").setOwner(p1);
        MoveOrder m4 = new MoveOrder(p1, true, map.getTerritoryByName("Roshar"), map.getTerritoryByName("Narnia"), Level.CIVILIAN,3);
        assertNull(checker.checkOrderValidity(map, m4));

        map.getTerritoryByName("Roshar").setOwner(p2);

        AttackOrder a1 = new AttackOrder(p1, false,  map.getTerritoryByName("Narnia"),  map.getTerritoryByName("Scadrial"),Level.CIVILIAN,  10);
        assertEquals("Can only attack adjacent territory", checker.checkOrderValidity(map, a1));

        AttackOrder a2 = new AttackOrder(p1, true,  map.getTerritoryByName("Narnia"),  map.getTerritoryByName("Scadrial"),Level.CIVILIAN,  10);
        assertNull(checker.checkOrderValidity(map, a2));
    }

    @Test
    public void test_checkMyruleAttack(){
        OrderRuleChecker checker = new PathChecker(null);
        GameMap map = makeGameMap();
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        AttackOrder a1 = new AttackOrder(p1,  map.getTerritoryByName("Narnia"),  map.getTerritoryByName("Scadrial"), 10);
        assertEquals("Can only attack adjacent territory", checker.checkOrderValidity(map, a1));

        AttackOrder a2 = new AttackOrder(p1,  map.getTerritoryByName("Narnia"),  map.getTerritoryByName("Midkemia"), 10);
        assertEquals("Cannot attack your own territory", checker.checkOrderValidity(map, a2));

        AttackOrder a3 = new AttackOrder(p1,  map.getTerritoryByName("Narnia"),  map.getTerritoryByName("Elantris"), 10);
        assertNull(checker.checkOrderValidity(map, a3));
    }

    @Test
    public void test_checkMyruleUpgrade(){
        OrderRuleChecker checker = new PathChecker(null);
        GameMap map = makeGameMap();
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();
        UpgradeOrder u1 = new UpgradeOrder(p3, map.getTerritoryByName("Gondor"), Level.CIVILIAN, Level.CAVALRY, 5);
        assertNull(checker.checkMyRule(map, u1));

        UpgradeOrder u2 = new UpgradeOrder(p3, map.getTerritoryByName("Narnia"), Level.CIVILIAN, Level.CAVALRY, 5);
        assertNotNull(checker.checkMyRule(map, u2));

        p1.addAlliance(p3);
        p3.addAlliance(p1);
        UpgradeOrder u3 = new UpgradeOrder(p3, map.getTerritoryByName("Narnia"), Level.CIVILIAN, Level.CAVALRY, 5);
        assertNull(checker.checkMyRule(map, u3));

    }

    @Test
    public void test_checkMyruleResearchandAlliance(){
        OrderRuleChecker checker = new PathChecker(null);
        GameMap map = makeGameMap();
        Player p1 = map.getTerritoryByName("Narnia").getOwner();
        Player p2 = map.getTerritoryByName("Elantris").getOwner();
        Player p3 = map.getTerritoryByName("Gondor").getOwner();

        ResearchOrder r1 = new ResearchOrder(p3);
        assertNull(checker.checkMyRule(map, r1));
    }
}
