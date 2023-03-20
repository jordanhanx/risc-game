package edu.duke.ece651.team7.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TextMapFactory implements MapFactory {

        @Override
        public GameMap createMap() {
                /**
                 * Use Player's Anonymous Class as initial Territories group owner.
                 * Player realPlayer = new Player();
                 * Player subtypePlayer = new Player() {} ;
                 * realPlayer.equals(subtypePlayer) == false ;
                 */
                Player placeholderA = new Player("GroupA") {
                };
                Player placeholderB = new Player("GroupB") {
                };
                Player placeholderC = new Player("GroupC") {
                };
                Territory territory1 = new Territory("Narnia", placeholderA, 10);
                Territory territory2 = new Territory("Elantris", placeholderB, 6);
                Territory territory3 = new Territory("Midkemia", placeholderA, 12);
                Territory territory4 = new Territory("Oz", placeholderA, 8);
                Territory territory5 = new Territory("Scadrial", placeholderB, 5);
                Territory territory6 = new Territory("Roshar", placeholderB, 3);
                Territory territory7 = new Territory("Gondor", placeholderC, 13);
                Territory territory8 = new Territory("Mordor", placeholderC, 14);
                Territory territory9 = new Territory("Hogwarts", placeholderC, 3);

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

                GameMap newMap = new GameMap(territoriesAdjacentList);
                return newMap;
        }

        @Override
        public GameMap createMapNew(int initGroupNum) {

                if (initGroupNum == 2) {
                        return createTwoPlayersMap();
                } else if (initGroupNum == 3) {
                        return createThreePlayersMap();
                } else if (initGroupNum == 4) {
                        return createFourPlayersMap();
                } else {
                        throw new IllegalArgumentException("Only support 2 to 4 players game");
                }

        }

        /**
         * create map for two players
         * 
         * @return GameMap object with 24 territories
         */
        public GameMap createTwoPlayersMap() {

                GameMap map = new GameMap(2);
                List<Player> initGroupOwners = map.getInitGroupOwners();
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 5);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(0), 6);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(0), 7);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(0), 4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(1), 5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(1), 6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(1), 7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(1), 4);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(1), 3);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(0), 4);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(0), 5);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(0), 6);
                Territory territory13 = new Territory("The Wall", initGroupOwners.get(0), 3);
                Territory territory14 = new Territory("King's Landing", initGroupOwners.get(1), 4);
                Territory territory15 = new Territory("Casterly Rock", initGroupOwners.get(1), 5);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(1), 6);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(1), 3);
                Territory territory18 = new Territory("The Eyrie", initGroupOwners.get(0), 4);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(0), 5);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(0), 6);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(0), 3);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 4);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(1), 5);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(1), 6);

                map.addTerritoryAndNeighbors(territory1, territory2, territory4, territory10);
                map.addTerritoryAndNeighbors(territory2, territory1, territory3, territory4, territory5, territory11);
                map.addTerritoryAndNeighbors(territory3, territory2, territory5, territory11);
                map.addTerritoryAndNeighbors(territory4, territory1, territory2, territory5, territory6, territory10,
                                territory11);
                map.addTerritoryAndNeighbors(territory5, territory2, territory3, territory4, territory6, territory7,
                                territory11, territory12);
                map.addTerritoryAndNeighbors(territory6, territory4, territory5, territory7, territory12);
                map.addTerritoryAndNeighbors(territory7, territory5, territory6, territory12, territory13);
                map.addTerritoryAndNeighbors(territory8, territory11, territory15, territory16);
                map.addTerritoryAndNeighbors(territory9, territory10, territory18);
                map.addTerritoryAndNeighbors(territory10, territory1, territory4, territory9, territory20, territory23);
                map.addTerritoryAndNeighbors(territory11, territory2, territory3, territory4, territory5, territory8,
                                territory14, territory19, territory17);
                map.addTerritoryAndNeighbors(territory12, territory6, territory7, territory21, territory22, territory24,
                                territory5);
                map.addTerritoryAndNeighbors(territory13, territory7, territory22);
                map.addTerritoryAndNeighbors(territory14, territory11, territory21, territory19);
                map.addTerritoryAndNeighbors(territory15, territory8, territory18, territory23);
                map.addTerritoryAndNeighbors(territory16, territory8, territory17);
                map.addTerritoryAndNeighbors(territory17, territory11, territory16);
                map.addTerritoryAndNeighbors(territory18, territory9, territory15);
                map.addTerritoryAndNeighbors(territory19, territory11, territory14);
                map.addTerritoryAndNeighbors(territory20, territory10);
                map.addTerritoryAndNeighbors(territory21, territory12, territory14);
                map.addTerritoryAndNeighbors(territory22, territory12, territory13);
                map.addTerritoryAndNeighbors(territory23, territory10, territory15);
                map.addTerritoryAndNeighbors(territory24, territory12);

                return map;
        }

        /**
         * create a map for three players
         */
        public GameMap createThreePlayersMap() {

                GameMap map = new GameMap(3);
                List<Player> initGroupOwners = map.getInitGroupOwners();
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 5);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(1), 6);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(2), 7);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(2), 4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(1), 5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(2), 6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(0), 7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(2), 4);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(1), 3);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(2), 4);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(0), 5);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(1), 6);
                Territory territory13 = new Territory("The Wall", initGroupOwners.get(2), 3);
                Territory territory14 = new Territory("King's Landing", initGroupOwners.get(2), 4);
                Territory territory15 = new Territory("Casterly Rock", initGroupOwners.get(0), 5);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(1), 6);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(1), 3);
                Territory territory18 = new Territory("The Eyrie", initGroupOwners.get(0), 4);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(1), 5);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(0), 6);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(2), 3);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 4);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(0), 5);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(0), 6);

                map.addTerritoryAndNeighbors(territory1, territory2, territory4, territory10);
                map.addTerritoryAndNeighbors(territory2, territory1, territory3, territory4, territory5, territory11);
                map.addTerritoryAndNeighbors(territory3, territory2, territory5, territory11);
                map.addTerritoryAndNeighbors(territory4, territory1, territory2, territory5, territory6, territory10,
                                territory11);
                map.addTerritoryAndNeighbors(territory5, territory2, territory3, territory4, territory6, territory7,
                                territory11, territory12);
                map.addTerritoryAndNeighbors(territory6, territory4, territory5, territory7, territory12);
                map.addTerritoryAndNeighbors(territory7, territory5, territory6, territory12, territory13);
                map.addTerritoryAndNeighbors(territory8, territory11, territory15, territory16);
                map.addTerritoryAndNeighbors(territory9, territory10, territory18);
                map.addTerritoryAndNeighbors(territory10, territory1, territory4, territory9, territory20, territory23);
                map.addTerritoryAndNeighbors(territory11, territory2, territory3, territory4, territory5, territory8,
                                territory14, territory19, territory17);
                map.addTerritoryAndNeighbors(territory12, territory6, territory7, territory21, territory22, territory24,
                                territory5);
                map.addTerritoryAndNeighbors(territory13, territory7, territory22);
                map.addTerritoryAndNeighbors(territory14, territory11, territory21, territory19);
                map.addTerritoryAndNeighbors(territory15, territory8, territory18, territory23);
                map.addTerritoryAndNeighbors(territory16, territory8, territory17);
                map.addTerritoryAndNeighbors(territory17, territory11, territory16);
                map.addTerritoryAndNeighbors(territory18, territory9, territory15);
                map.addTerritoryAndNeighbors(territory19, territory11, territory14);
                map.addTerritoryAndNeighbors(territory20, territory10);
                map.addTerritoryAndNeighbors(territory21, territory12, territory14);
                map.addTerritoryAndNeighbors(territory22, territory12, territory13);
                map.addTerritoryAndNeighbors(territory23, territory10, territory15);
                map.addTerritoryAndNeighbors(territory24, territory12);

                return map;
        }

        /**
         * create a map for four players
         */
        public GameMap createFourPlayersMap() {
                GameMap map = new GameMap(4);
                List<Player> initGroupOwners = map.getInitGroupOwners();
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 5);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(1), 6);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(2), 7);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(1), 4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(0), 5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(2), 6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(1), 7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(3), 4);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(0), 3);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(1), 4);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(3), 5);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(0), 6);
                Territory territory13 = new Territory("The Wall", initGroupOwners.get(1), 3);
                Territory territory14 = new Territory("King's Landing", initGroupOwners.get(2), 4);
                Territory territory15 = new Territory("Casterly Rock", initGroupOwners.get(1), 5);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(0), 6);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(2), 3);
                Territory territory18 = new Territory("The Eyrie", initGroupOwners.get(3), 4);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(0), 5);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(2), 6);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(3), 3);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(3), 4);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(2), 5);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(3), 6);

                map.addTerritoryAndNeighbors(territory1, territory2, territory4, territory10);
                map.addTerritoryAndNeighbors(territory2, territory1, territory3, territory4, territory5, territory11);
                map.addTerritoryAndNeighbors(territory3, territory2, territory5, territory11);
                map.addTerritoryAndNeighbors(territory4, territory1, territory2, territory5, territory6, territory10,
                                territory11);
                map.addTerritoryAndNeighbors(territory5, territory2, territory3, territory4, territory6, territory7,
                                territory11, territory12);
                map.addTerritoryAndNeighbors(territory6, territory4, territory5, territory7, territory12);
                map.addTerritoryAndNeighbors(territory7, territory5, territory6, territory12, territory13);
                map.addTerritoryAndNeighbors(territory8, territory11, territory15, territory16);
                map.addTerritoryAndNeighbors(territory9, territory10, territory18);
                map.addTerritoryAndNeighbors(territory10, territory1, territory4, territory9, territory20, territory23);
                map.addTerritoryAndNeighbors(territory11, territory2, territory3, territory4, territory5, territory8,
                                territory14, territory19, territory17);
                map.addTerritoryAndNeighbors(territory12, territory6, territory7, territory21, territory22, territory24,
                                territory5);
                map.addTerritoryAndNeighbors(territory13, territory7, territory22);
                map.addTerritoryAndNeighbors(territory14, territory11, territory21, territory19);
                map.addTerritoryAndNeighbors(territory15, territory8, territory18, territory23);
                map.addTerritoryAndNeighbors(territory16, territory8, territory17);
                map.addTerritoryAndNeighbors(territory17, territory11, territory16);
                map.addTerritoryAndNeighbors(territory18, territory9, territory15);
                map.addTerritoryAndNeighbors(territory19, territory11, territory14);
                map.addTerritoryAndNeighbors(territory20, territory10);
                map.addTerritoryAndNeighbors(territory21, territory12, territory14);
                map.addTerritoryAndNeighbors(territory22, territory12, territory13);
                map.addTerritoryAndNeighbors(territory23, territory10, territory15);
                map.addTerritoryAndNeighbors(territory24, territory12);
                return map;
        }

}