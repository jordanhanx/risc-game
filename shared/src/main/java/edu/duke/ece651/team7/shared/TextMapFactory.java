package edu.duke.ece651.team7.shared;

import java.util.List;

public class TextMapFactory implements MapFactory {


        @Override
        public GameMap createPlayerMap(int initGroupNum) {

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
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory1);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory2);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory3);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory8);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory9);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory10);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory11);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory12);
                Territory territory13 = new Territory("Aranthia", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory13);
                Territory territory14 = new Territory("Drakoria", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory14);
                Territory territory15 = new Territory("Galadria", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory15);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory16);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory17);
                Territory territory18 = new Territory("Helvoria", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory18);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory19);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory20);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(0), 0);
                initGroupOwners.get(1).addTerritory(territory21);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory22);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory23);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory24);
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
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 10);
                initGroupOwners.get(0).addTerritory(territory1);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(1), 12);
                initGroupOwners.get(1).addTerritory(territory2);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(2), 8);
                initGroupOwners.get(2).addTerritory(territory3);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(2), 13);
                initGroupOwners.get(2).addTerritory(territory4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(1), 6);
                initGroupOwners.get(1).addTerritory(territory5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(2), 5);
                initGroupOwners.get(2).addTerritory(territory6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(0), 3);
                initGroupOwners.get(0).addTerritory(territory7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(2), 14);
                initGroupOwners.get(2).addTerritory(territory8);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(1), 3);
                initGroupOwners.get(1).addTerritory(territory9);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(2), 4);
                initGroupOwners.get(2).addTerritory(territory10);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(0), 5);
                initGroupOwners.get(0).addTerritory(territory11);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(1), 6);
                initGroupOwners.get(1).addTerritory(territory12);
                Territory territory13 = new Territory("Aranthia", initGroupOwners.get(2), 3);
                initGroupOwners.get(2).addTerritory(territory13);
                Territory territory14 = new Territory("Drakoria", initGroupOwners.get(2), 4);
                initGroupOwners.get(2).addTerritory(territory14);
                Territory territory15 = new Territory("Galadria", initGroupOwners.get(0), 5);
                initGroupOwners.get(0).addTerritory(territory15);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(1), 6);
                initGroupOwners.get(1).addTerritory(territory16);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(1), 3);
                initGroupOwners.get(1).addTerritory(territory17);
                Territory territory18 = new Territory("Helvoria", initGroupOwners.get(0), 4);
                initGroupOwners.get(0).addTerritory(territory18);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(1), 5);
                initGroupOwners.get(1).addTerritory(territory19);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(0), 6);
                initGroupOwners.get(0).addTerritory(territory20);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(2), 3);
                initGroupOwners.get(2).addTerritory(territory21);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 4);
                initGroupOwners.get(1).addTerritory(territory22);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(0), 5);
                initGroupOwners.get(0).addTerritory(territory23);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(0), 6);
                initGroupOwners.get(0).addTerritory(territory24);

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
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 10);
                initGroupOwners.get(0).addTerritory(territory1);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(1), 12);
                initGroupOwners.get(1).addTerritory(territory2);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(0), 8);
                initGroupOwners.get(2).addTerritory(territory3);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(0), 13);
                initGroupOwners.get(2).addTerritory(territory4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(1), 6);
                initGroupOwners.get(1).addTerritory(territory5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(2), 5);
                initGroupOwners.get(2).addTerritory(territory6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(3), 3);
                initGroupOwners.get(0).addTerritory(territory7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(3), 14);
                initGroupOwners.get(2).addTerritory(territory8);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(2), 3);
                initGroupOwners.get(1).addTerritory(territory9);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(0), 4);
                initGroupOwners.get(2).addTerritory(territory10);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(1), 5);
                initGroupOwners.get(0).addTerritory(territory11);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(2), 6);
                initGroupOwners.get(1).addTerritory(territory12);
                Territory territory13 = new Territory("Aranthia", initGroupOwners.get(0), 3);
                initGroupOwners.get(2).addTerritory(territory13);
                Territory territory14 = new Territory("Drakoria", initGroupOwners.get(2), 4);
                initGroupOwners.get(2).addTerritory(territory14);
                Territory territory15 = new Territory("Galadria", initGroupOwners.get(1), 5);
                initGroupOwners.get(0).addTerritory(territory15);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(0), 6);
                initGroupOwners.get(1).addTerritory(territory16);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(3), 3);
                initGroupOwners.get(1).addTerritory(territory17);
                Territory territory18 = new Territory("Helvoria", initGroupOwners.get(1), 4);
                initGroupOwners.get(0).addTerritory(territory18);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(3), 5);
                initGroupOwners.get(1).addTerritory(territory19);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(2), 6);
                initGroupOwners.get(0).addTerritory(territory20);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(3), 3);
                initGroupOwners.get(2).addTerritory(territory21);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 4);
                initGroupOwners.get(1).addTerritory(territory22);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(2), 5);
                initGroupOwners.get(0).addTerritory(territory23);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(3), 6);
                initGroupOwners.get(0).addTerritory(territory24);

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