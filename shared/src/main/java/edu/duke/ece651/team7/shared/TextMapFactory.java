package edu.duke.ece651.team7.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextMapFactory implements MapFactory {

        // public GameMap createTestMap() {
        //         GameMap map = new GameMap(3);
        //         List<Player> initGroupOwners = map.getInitGroupOwners();

        //         Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 0);
        //         initGroupOwners.get(0).addTerritory(territory1);
        //         Territory territory2 = new Territory("Elantris", initGroupOwners.get(1), 0);
        //         initGroupOwners.get(1).addTerritory(territory2);
        //         Territory territory3 = new Territory("Midkemia", initGroupOwners.get(0), 0);
        //         initGroupOwners.get(0).addTerritory(territory3);
        //         Territory territory4 = new Territory("Oz", initGroupOwners.get(0), 0);
        //         initGroupOwners.get(0).addTerritory(territory4);
        //         Territory territory5 = new Territory("Scadrial", initGroupOwners.get(1), 0);
        //         initGroupOwners.get(1).addTerritory(territory5);
        //         Territory territory6 = new Territory("Roshar", initGroupOwners.get(1), 0);
        //         initGroupOwners.get(1).addTerritory(territory6);
        //         Territory territory7 = new Territory("Gondor", initGroupOwners.get(2), 0);
        //         initGroupOwners.get(2).addTerritory(territory7);
        //         Territory territory8 = new Territory("Mordor", initGroupOwners.get(2), 0);
        //         initGroupOwners.get(2).addTerritory(territory8);
        //         Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(2), 0);
        //         initGroupOwners.get(2).addTerritory(territory9);
        //         map.addTerritoryAndNeighbors(territory1, territory2, territory3);
        //         map.addTerritoryAndNeighbors(territory2, territory1, territory3, territory5, territory6);
        //         map.addTerritoryAndNeighbors(territory3, territory1, territory2, territory4, territory5);
        //         map.addTerritoryAndNeighbors(territory4, territory7, territory3, territory5, territory8);
        //         map.addTerritoryAndNeighbors(territory5, territory2, territory3, territory4, territory6, territory9, territory8);
        //         map.addTerritoryAndNeighbors(territory6, territory9, territory2, territory5);
        //         map.addTerritoryAndNeighbors(territory7, territory4, territory8);
        //         map.addTerritoryAndNeighbors(territory8, territory4, territory7, territory5, territory9);
        //         map.addTerritoryAndNeighbors(territory9, territory6, territory5, territory8);

        //         return map;
        // }

        // @Override
        // public GameMap createPlayerMap(int initGroupNum) {

        //         if (initGroupNum == 2) {
        //                 return createTwoPlayersMap();
        //         } else if (initGroupNum == 3) {
        //                 return createThreePlayersMap();
        //         } else if (initGroupNum == 4) {
        //                 return createFourPlayersMap();
        //         } else {
        //                 throw new IllegalArgumentException("Only support 2 to 4 players game");
        //         }

        // }
        @Override
        public GameMap createPlayerMap(int initGroupNum) {
                if (initGroupNum < 2 || initGroupNum > 4){
                        throw new IllegalArgumentException("Only support 2 to 4 players game");
                }else{
                        return createMap(initGroupNum);
                }

        }

        public GameMap createMap(int initGroupNum){
                GameMap map = new GameMap(initGroupNum);
                List<Player> initGroupOwners = map.getInitGroupOwners();
                Territory territory1 = new Territory("Narnia", 0);
                Territory territory2 = new Territory("Midkemia", 0);
                Territory territory3 = new Territory("Oz", 0);
                Territory territory4 = new Territory("Gondor", 0);
                Territory territory5 = new Territory("Elantris",0);
                Territory territory6 = new Territory("Scadrial", 0);
                Territory territory7 = new Territory("Roshar", 0);
                Territory territory8 = new Territory("Mordor", 0);
                Territory territory9 = new Territory("Hogwarts", 0);
                Territory territory10 = new Territory("Westeros", 0);
                Territory territory11 = new Territory("Essos", 0);
                Territory territory12 = new Territory("Dorne", 0);
                Territory territory13 = new Territory("Aranthia", 0);
                Territory territory14 = new Territory("Drakoria", 0);
                Territory territory15 = new Territory("Galadria", 0);
                Territory territory16 = new Territory("Highgarden", 0);
                Territory territory17 = new Territory("Winterfell", 0);
                Territory territory18 = new Territory("Helvoria", 0);
                Territory territory19 = new Territory("Dragonstone", 0);
                Territory territory20 = new Territory("Pyke", 0);
                Territory territory21 = new Territory("Oldtown", 0);
                Territory territory22 = new Territory("Braavos", 0);
                Territory territory23 = new Territory("Pentos", 0);
                Territory territory24 = new Territory("Volantis", 0);
                map.addTerritoryAndNeighbors(territory1, territory2, 2, territory3, 3, territory19, 4);
                map.addTerritoryAndNeighbors(territory2, territory1, 2, territory3, 3, territory24, 8);
                map.addTerritoryAndNeighbors(territory3, territory1, 3, territory2, 3, territory4, 4, territory5, 9, territory6, 8);
                map.addTerritoryAndNeighbors(territory4, territory3, 4, territory5, 1, territory9, 6, territory10, 1);
                map.addTerritoryAndNeighbors(territory5, territory3, 9, territory4, 1, territory6, 5, territory7, 2, territory8, 4, territory9, 4);
                map.addTerritoryAndNeighbors(territory6, territory3, 8, territory5, 5, territory7, 4);
                map.addTerritoryAndNeighbors(territory7, territory5, 2, territory6, 4, territory8, 7);
                map.addTerritoryAndNeighbors(territory8, territory5, 4, territory7, 7, territory9, 2, territory11, 12);
                map.addTerritoryAndNeighbors(territory9, territory4, 6, territory5, 4, territory8, 2, territory10, 8, territory11, 15, territory12, 5, territory13, 4);
                map.addTerritoryAndNeighbors(territory10, territory4, 1, territory9, 8, territory13, 2);
                map.addTerritoryAndNeighbors(territory11, territory8, 12, territory9, 15, territory12, 7);
                map.addTerritoryAndNeighbors(territory12, territory9, 5, territory11, 7, territory13, 3, territory16, 7);
                map.addTerritoryAndNeighbors(territory13, territory9, 4, territory10, 2, territory12, 3, territory14, 11, territory15, 9, territory16, 14);
                map.addTerritoryAndNeighbors(territory14, territory13, 11, territory15, 5, territory18, 3, territory19, 3, territory20, 6);
                map.addTerritoryAndNeighbors(territory15, territory13, 9, territory14, 5, territory16, 2, territory18, 3);
                map.addTerritoryAndNeighbors(territory16, territory12, 7, territory13, 14, territory15, 2, territory17, 2, territory18, 4);
                map.addTerritoryAndNeighbors(territory17, territory16, 2, territory18, 6);
                map.addTerritoryAndNeighbors(territory18, territory14, 3, territory15, 3, territory16, 4, territory17, 6, territory20, 8, territory22, 1);
                map.addTerritoryAndNeighbors(territory19, territory1, 4, territory14, 3, territory20, 5);
                map.addTerritoryAndNeighbors(territory20, territory14, 6, territory18, 8, territory19, 5, territory22, 6, territory23, 10, territory24, 7);
                map.addTerritoryAndNeighbors(territory21, territory22, 2);
                map.addTerritoryAndNeighbors(territory22, territory18, 1, territory20, 6, territory21, 2, territory23, 6);
                map.addTerritoryAndNeighbors(territory23, territory20, 10, territory22, 6, territory24, 5);
                map.addTerritoryAndNeighbors(territory24, territory2, 8, territory20, 7, territory23, 5);

                //assign initial groups
                int numTperP = 24/initGroupNum;
                ArrayList<Territory> territories = new ArrayList<>(map.getTerritories());
                for(int i  = 0; i < initGroupNum; i++){
                        for(int j = numTperP *i; j < numTperP*(i+1); j++){
                                territories.get(j).setOwner(initGroupOwners.get(i));
                                initGroupOwners.get(i).addTerritory(territories.get(j));
                        }
                }

                return map;
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
                initGroupOwners.get(0).addTerritory(territory21);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory22);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory23);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory24);
                map.addTerritoryAndNeighbors(territory1, territory2, territory4, territory10);
                map.addTerritoryAndNeighbors(territory2, territory1, territory3, territory4, territory5);
                map.addTerritoryAndNeighbors(territory3, territory2, territory5);
                map.addTerritoryAndNeighbors(territory4, territory1, territory2, territory5, territory10,
                                territory11);
                map.addTerritoryAndNeighbors(territory5, territory2, territory3, territory4, territory6, territory7,
                                territory11, territory12);
                map.addTerritoryAndNeighbors(territory6, territory5, territory7, territory12);
                map.addTerritoryAndNeighbors(territory7, territory5, territory6, territory12);
                map.addTerritoryAndNeighbors(territory8, territory11, territory15, territory16);
                map.addTerritoryAndNeighbors(territory9, territory10, territory18);
                map.addTerritoryAndNeighbors(territory10, territory1, territory4, territory9, territory20, territory23);
                map.addTerritoryAndNeighbors(territory11, territory4, territory5, territory8,
                                territory14, territory19, territory17);
                map.addTerritoryAndNeighbors(territory12, territory6, territory7, territory21, territory22, territory24,
                                territory5);
                map.addTerritoryAndNeighbors(territory13, territory22);
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
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory1);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory2);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory3);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory8);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory9);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory10);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory11);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory12);
                Territory territory13 = new Territory("Aranthia", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory13);
                Territory territory14 = new Territory("Drakoria", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory14);
                Territory territory15 = new Territory("Galadria", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory15);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory16);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory17);
                Territory territory18 = new Territory("Helvoria", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory18);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(1),0);
                initGroupOwners.get(1).addTerritory(territory19);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory20);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory21);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory22);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory23);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(0), 0);
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
                Territory territory1 = new Territory("Narnia", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory1);
                Territory territory2 = new Territory("Midkemia", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory2);
                Territory territory3 = new Territory("Oz", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory3);
                Territory territory4 = new Territory("Gondor", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory4);
                Territory territory5 = new Territory("Elantris", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory5);
                Territory territory6 = new Territory("Scadrial", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory6);
                Territory territory7 = new Territory("Roshar", initGroupOwners.get(3), 0);
                initGroupOwners.get(3).addTerritory(territory7);
                Territory territory8 = new Territory("Mordor", initGroupOwners.get(3), 0);
                initGroupOwners.get(3).addTerritory(territory8);
                Territory territory9 = new Territory("Hogwarts", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory9);
                Territory territory10 = new Territory("Westeros", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory10);
                Territory territory11 = new Territory("Essos", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory11);
                Territory territory12 = new Territory("Dorne", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory12);
                Territory territory13 = new Territory("Aranthia", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory13);
                Territory territory14 = new Territory("Drakoria", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory14);
                Territory territory15 = new Territory("Galadria", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory15);
                Territory territory16 = new Territory("Highgarden", initGroupOwners.get(0), 0);
                initGroupOwners.get(0).addTerritory(territory16);
                Territory territory17 = new Territory("Winterfell", initGroupOwners.get(3), 0);
                initGroupOwners.get(3).addTerritory(territory17);
                Territory territory18 = new Territory("Helvoria", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory18);
                Territory territory19 = new Territory("Dragonstone", initGroupOwners.get(3), 0);
                initGroupOwners.get(3).addTerritory(territory19);
                Territory territory20 = new Territory("Pyke", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory20);
                Territory territory21 = new Territory("Oldtown", initGroupOwners.get(3), 0);
                initGroupOwners.get(3).addTerritory(territory21);
                Territory territory22 = new Territory("Braavos", initGroupOwners.get(1), 0);
                initGroupOwners.get(1).addTerritory(territory22);
                Territory territory23 = new Territory("Pentos", initGroupOwners.get(2), 0);
                initGroupOwners.get(2).addTerritory(territory23);
                Territory territory24 = new Territory("Volantis", initGroupOwners.get(3), 0);
                initGroupOwners.get(3).addTerritory(territory24);

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