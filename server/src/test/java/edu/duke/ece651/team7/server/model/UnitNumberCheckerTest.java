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

public class UnitNumberCheckerTest {

        private GameMap makeGameMap() {
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
                territoriesAdjacentList.put(territory1,
                                new ArrayList<Territory>(Arrays.asList(territory2, territory3)));
                territoriesAdjacentList.put(territory2,
                                new ArrayList<Territory>(
                                                Arrays.asList(territory1, territory3, territory5, territory6)));
                territoriesAdjacentList.put(territory3,
                                new ArrayList<Territory>(
                                                Arrays.asList(territory1, territory2, territory4, territory5)));
                territoriesAdjacentList.put(territory4,
                                new ArrayList<Territory>(
                                                Arrays.asList(territory7, territory3, territory5, territory8)));
                territoriesAdjacentList.put(territory5, new ArrayList<Territory>(
                                Arrays.asList(territory2, territory3, territory4, territory6, territory9, territory8)));
                territoriesAdjacentList.put(territory6,
                                new ArrayList<Territory>(Arrays.asList(territory9, territory2, territory5)));
                territoriesAdjacentList.put(territory7,
                                new ArrayList<Territory>(Arrays.asList(territory4, territory8)));
                territoriesAdjacentList.put(territory8,
                                new ArrayList<Territory>(
                                                Arrays.asList(territory4, territory7, territory5, territory9)));
                territoriesAdjacentList.put(territory9,
                                new ArrayList<Territory>(Arrays.asList(territory6, territory5, territory8)));
                GameMap map = new GameMap(territoriesAdjacentList);
                return map;
        }

        @Test
        public void test_checkMyrule() {
                UnitNumberChecker checker = new UnitNumberChecker(null);
                GameMap map = makeGameMap();
                Player p1 = map.getTerritoryByName("Narnia").getOwner();
                Player p2 = map.getTerritoryByName("Elantris").getOwner();
                Player p3 = map.getTerritoryByName("Gondor").getOwner();

                MoveOrder m1 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"),
                                30);
                assertEquals("No enough units in the source Territory", checker.checkOrderValidity(map, m1));

                MoveOrder m2 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"),
                                5);
                assertNull(checker.checkOrderValidity(map, m2));

                AttackOrder a1 = new AttackOrder(p1, map.getTerritoryByName("Narnia"),
                                map.getTerritoryByName("Elantris"), -10);
                assertEquals("Number of Units must be > 0", checker.checkOrderValidity(map, a1));
        }

}
