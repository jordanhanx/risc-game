package edu.duke.ece651.team7.logingate.model;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderExecuterTest {

        protected GameMap buildTestMap() {
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
        public void test_doOneMove() {
                GameMap map = buildTestMap();
                OrderExecuter ox = new OrderExecuter(map);
                Player p1 = map.getTerritoryByName("Narnia").getOwner();
                Player p2 = map.getTerritoryByName("Elantris").getOwner();
                Player p3 = map.getTerritoryByName("Gondor").getOwner();

                MoveOrder m1 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"),
                                5);
                ox.doOneMove(m1);
                assertEquals(5, map.getTerritoryByName("Narnia").getUnits());
                assertEquals(15, map.getTerritoryByName("Midkemia").getUnits());

                MoveOrder m2 = new MoveOrder(p2, map.getTerritoryByName("Gondor"), map.getTerritoryByName("Mordor"), 4);
                assertThrows(IllegalArgumentException.class, () -> ox.doOneMove(m2));

                MoveOrder m3 = new MoveOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Midkemia"),
                                0);
                assertThrows(IllegalArgumentException.class, () -> ox.doOneMove(m3));
        };

        // @Test
        // public void test_isInCombatPool(){
        // GameMap map = buildTestMap();
        // OrderExecuter ox = new OrderExecuter(map);
        // Player p1 = new Player("a");
        // Player p2 = new Player("b");
        // map.getTerritoryByName("Narnia").setOwner(p1);
        // map.getTerritoryByName("Midkemia").setOwner(p1);

        // AttackOrder m1 = new AttackOrder(p1, map.getTerritoryByName("Narnia"),
        // map.getTerritoryByName("Elantris"), 5);
        // AttackOrder m2 = new AttackOrder(p1, map.getTerritoryByName("Midkemia"),
        // map.getTerritoryByName("Elantris"), 5);

        // AttackOrder m3 = new AttackOrder(p2, map.getTerritoryByName("Scadrial"),
        // map.getTerritoryByName("Elantris"), 4);
        // AttackOrder m4 = new AttackOrder(p2, map.getTerritoryByName("Roshar"),
        // map.getTerritoryByName("Hogwarts"), 2);
        // ox.pushCombat(m1);
        // ox.pushCombat(m2);
        // ox.pushCombat(m3);
        // ox.pushCombat(m4);
        // }

        @Test
        public void test_PushCombat() {
                GameMap map = buildTestMap();
                OrderExecuter ox = new OrderExecuter(map);
                Player p1 = map.getTerritoryByName("Narnia").getOwner();
                Player p2 = map.getTerritoryByName("Elantris").getOwner();
                Player p3 = map.getTerritoryByName("Gondor").getOwner();
                AttackOrder a1 = new AttackOrder(p1, map.getTerritoryByName("Narnia"),
                                map.getTerritoryByName("Elantris"), 10);
                ox.pushCombat(a1);
                assertNotNull(ox.isInCombatPool(a1.getDest()));

                AttackOrder a2 = new AttackOrder(p1, map.getTerritoryByName("Narnia"), map.getTerritoryByName("Gondor"),
                                10);
                assertThrows(IllegalArgumentException.class, () -> ox.pushCombat(a2));
                assertNull(ox.isInCombatPool(map.getTerritoryByName("Gondor")));

                AttackOrder a3 = new AttackOrder(p1, map.getTerritoryByName("Narnia"),
                                map.getTerritoryByName("Elantris"), 0);
                assertThrows(IllegalArgumentException.class, () -> ox.pushCombat(a3));

                AttackOrder a4 = new AttackOrder(p1, map.getTerritoryByName("Midkemia"),
                                map.getTerritoryByName("Elantris"), 4);
                ox.pushCombat(a4);
                Combat targetCombat = ox.isInCombatPool(a4.getDest());
                assertEquals(14, targetCombat.getAttackUnitofPlayer(p1));

        }

        @Test
        public void test_doAllCombat() {
                GameMap map = buildTestMap();
                OrderExecuter ox = new OrderExecuter(map);
                Player p1 = map.getTerritoryByName("Narnia").getOwner();
                Player p2 = map.getTerritoryByName("Elantris").getOwner();
                p2.addTerritory(map.getTerritoryByName("Scadrial"));
                Player p3 = map.getTerritoryByName("Gondor").getOwner();
                p3.addTerritory(map.getTerritoryByName("Mordor"));
                Player p4 = new Player("green");
                Player p5 = new Player("blue");
                map.getTerritoryByName("Elantris").setOwner(p4);
                p4.addTerritory(map.getTerritoryByName("Elantris"));

                map.getTerritoryByName("Roshar").setOwner(p5);
                p5.addTerritory(map.getTerritoryByName("Roshar"));

                AttackOrder m1 = new AttackOrder(p1, map.getTerritoryByName("Midkemia"),
                                map.getTerritoryByName("Scadrial"), 10);
                AttackOrder m2 = new AttackOrder(p4, map.getTerritoryByName("Elantris"),
                                map.getTerritoryByName("Scadrial"), 3);
                AttackOrder m3 = new AttackOrder(p5, map.getTerritoryByName("Roshar"),
                                map.getTerritoryByName("Scadrial"), 2);

                AttackOrder m4 = new AttackOrder(p3, map.getTerritoryByName("Mordor"),
                                map.getTerritoryByName("Scadrial"), 3);

                AttackOrder m5 = new AttackOrder(p1, map.getTerritoryByName("Oz"), map.getTerritoryByName("Mordor"), 5);
                AttackOrder m6 = new AttackOrder(p2, map.getTerritoryByName("Scadrial"),
                                map.getTerritoryByName("Mordor"), 1);
                ox.pushCombat(m1);
                ox.pushCombat(m2);
                ox.pushCombat(m3);
                ox.pushCombat(m4);

                ox.pushCombat(m5);
                ox.pushCombat(m6);

                // System.out.println(map.getTerritoryByName("Scadrial").getOwner().getName());
                // System.out.println(map.getTerritoryByName("Mordor").getOwner().getName());
                ox.doAllCombats();

                // System.out.println(map.getTerritoryByName("Scadrial").getOwner().getName());
                // System.out.println(map.getTerritoryByName("Mordor").getOwner().getName());
                assertNull(ox.isInCombatPool(m4.getDest()));
                // assertNull(ox.isInCombatPool(m6.getDest()));
                assertEquals(11, map.getTerritoryByName("Hogwarts").getUnits());
                assertEquals(1, map.getTerritoryByName("Midkemia").getUnits());
                assertEquals(8, map.getTerritoryByName("Elantris").getUnits());

        }

}
