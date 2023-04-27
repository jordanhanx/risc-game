package edu.duke.ece651.team7.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GUIMapFactory implements MapFactory {
        @Override
        public GameMap createPlayerMap(int initGroupNum) {
                if (initGroupNum < 2 || initGroupNum > 4) {
                        throw new IllegalArgumentException("Only support 2 to 4 players game");
                } else {
                        
                        return createMap(initGroupNum, 10 + (-1) * initGroupNum, initGroupNum*4);
                }

        }

        public GameMap createMap(int initGroupNum, int initfood, int inittech) {
                GameMap map = new GameMap(initGroupNum);
                List<Player> initGroupOwners = map.getInitGroupOwners();
                Territory territory1 = new Territory("Narnia", 0, initfood, inittech);
                Territory territory2 = new Territory("Midkemia", 0, initfood, inittech);
                Territory territory3 = new Territory("Oz", 0, initfood, inittech);
                Territory territory4 = new Territory("Gondor", 0, initfood, inittech);
                Territory territory5 = new Territory("Elantris", 0, initfood, inittech);
                Territory territory6 = new Territory("Scadrial", 0, initfood, inittech);
                Territory territory7 = new Territory("Roshar", 0, initfood, inittech);
                Territory territory8 = new Territory("Mordor", 0, initfood, inittech);
                Territory territory9 = new Territory("Hogwarts", 0, initfood, inittech);
                Territory territory10 = new Territory("Westeros", 0, initfood, inittech);
                Territory territory11 = new Territory("Essos", 0, initfood, inittech);
                Territory territory12 = new Territory("Dorne", 0, initfood, inittech);
                Territory territory13 = new Territory("Aranthia", 0, initfood, inittech);
                Territory territory14 = new Territory("Drakoria", 0, initfood, inittech);
                Territory territory15 = new Territory("Galadria", 0, initfood, inittech);
                Territory territory16 = new Territory("Highgarden", 0, initfood, inittech);
                Territory territory17 = new Territory("Winterfell", 0, initfood, inittech);
                Territory territory18 = new Territory("Helvoria", 0, initfood, inittech);
                Territory territory19 = new Territory("Dragonstone", 0, initfood, inittech);
                Territory territory20 = new Territory("Pyke", 0, initfood, inittech);
                Territory territory21 = new Territory("Oldtown", 0, initfood, inittech);
                Territory territory22 = new Territory("Braavos", 0, initfood, inittech);
                Territory territory23 = new Territory("Pentos", 0, initfood, inittech);
                Territory territory24 = new Territory("Volantis", 0, initfood, inittech);

                map.addTerritoryAndNeighbors(territory1, territory2, 3, territory7, 2, territory18, 12, territory23, 9,
                                                         territory19, 4, territory21, 4, territory20, 5, territory14, 11);
                map.addTerritoryAndNeighbors(territory2, territory1, 3, territory18, 2);
                map.addTerritoryAndNeighbors(territory3, territory6, 10, territory8, 14, territory5, 8, territory4, 2,
                                territory14, 15);
                map.addTerritoryAndNeighbors(territory4, territory3, 2, territory5, 4, territory10, 1, territory13, 4, territory14, 9);
                map.addTerritoryAndNeighbors(territory5, territory3, 8, territory4, 4, territory8, 5, territory12, 6, territory16, 4);
                map.addTerritoryAndNeighbors(territory6, territory3, 10, territory8, 5);
                map.addTerritoryAndNeighbors(territory7, territory1, 2, territory21, 6, territory19, 7);
                map.addTerritoryAndNeighbors(territory8, territory3, 14, territory5, 5, territory6, 5, territory9, 7, territory12, 8);
                map.addTerritoryAndNeighbors(territory9, territory8, 7, territory11, 4, territory12, 7);
                map.addTerritoryAndNeighbors(territory10, territory4, 1, territory13, 2);
                map.addTerritoryAndNeighbors(territory11, territory9, 4, territory12, 7);
                map.addTerritoryAndNeighbors(territory12, territory5, 6, territory8, 8, territory9, 7, territory11, 7, 
                                                territory16, 7, territory17, 7);
                map.addTerritoryAndNeighbors(territory13, territory4, 4, territory10, 2, territory14, 8, territory16, 5);
                map.addTerritoryAndNeighbors(territory14, territory3, 15, territory4, 9, territory13, 8, territory15,
                                7, territory18, 6, territory1, 11, territory20, 16, territory24, 15);
                map.addTerritoryAndNeighbors(territory15, territory14, 7, territory16, 6, territory17, 2, territory18, 2, territory19, 4);
                map.addTerritoryAndNeighbors(territory16, territory13, 5, territory5, 4, territory12, 7, territory17,
                                8, territory15, 6);
                map.addTerritoryAndNeighbors(territory17, territory12, 7, territory15, 2, territory16, 8, territory19, 7);
                map.addTerritoryAndNeighbors(territory18, territory14, 6, territory15, 2, territory19, 7, territory1, 12, territory2, 2);
                map.addTerritoryAndNeighbors(territory19, territory1, 4, territory7, 7, territory15, 4, territory17, 7, territory18, 7);
                map.addTerritoryAndNeighbors(territory20, territory14, 16, territory1, 5, territory23, 7, territory24, 9);
                map.addTerritoryAndNeighbors(territory21, territory1, 4, territory7, 6, territory23, 6,territory22, 5);
                map.addTerritoryAndNeighbors(territory22, territory21, 5, territory23, 4);
                map.addTerritoryAndNeighbors(territory23, territory20, 7, territory24, 4, territory22, 4, territory21, 6, territory1, 9);
                map.addTerritoryAndNeighbors(territory24, territory14, 15, territory20, 9, territory23, 4);

                // assign initial groups
                int numTperP = 24 / initGroupNum;
                ArrayList<Territory> territories = new ArrayList<>(map.getTerritories());
                for (int i = 0; i < initGroupNum; i++) {
                        for (int j = numTperP * i; j < numTperP * (i + 1); j++) {
                                territories.get(j).setOwner(initGroupOwners.get(i));
                                initGroupOwners.get(i).addTerritory(territories.get(j));
                        }
                }
                return map;
        }


        // public GameMap createMap(int initGroupNum, int initfood, int inittech) {
        //         GameMap map = new GameMap(initGroupNum);
        //         List<Player> initGroupOwners = map.getInitGroupOwners();
        //         Territory territory1 = new Territory("Narnia", 0, initfood, inittech);
        //         Territory territory2 = new Territory("Midkemia", 0, initfood, inittech);
        //         Territory territory3 = new Territory("Oz", 0, initfood, inittech);
        //         Territory territory4 = new Territory("Gondor", 0, initfood, inittech);
        //         Territory territory5 = new Territory("Elantris", 0, initfood, inittech);
        //         Territory territory6 = new Territory("Scadrial", 0, initfood, inittech);
        //         Territory territory7 = new Territory("Roshar", 0, initfood, inittech);
        //         Territory territory8 = new Territory("Mordor", 0, initfood, inittech);
        //         Territory territory9 = new Territory("Hogwarts", 0, initfood, inittech);
        //         Territory territory10 = new Territory("Westeros", 0, initfood, inittech);
        //         Territory territory11 = new Territory("Essos", 0, initfood, inittech);
        //         Territory territory12 = new Territory("Dorne", 0, initfood, inittech);
        //         Territory territory13 = new Territory("Aranthia", 0, initfood, inittech);
        //         Territory territory14 = new Territory("Drakoria", 0, initfood, inittech);
        //         Territory territory15 = new Territory("Galadria", 0, initfood, inittech);
        //         Territory territory16 = new Territory("Highgarden", 0, initfood, inittech);
        //         Territory territory17 = new Territory("Winterfell", 0, initfood, inittech);
        //         Territory territory18 = new Territory("Helvoria", 0, initfood, inittech);
        //         Territory territory19 = new Territory("Dragonstone", 0, initfood, inittech);
        //         Territory territory20 = new Territory("Pyke", 0, initfood, inittech);
        //         Territory territory21 = new Territory("Oldtown", 0, initfood, inittech);
        //         Territory territory22 = new Territory("Braavos", 0, initfood, inittech);
        //         Territory territory23 = new Territory("Pentos", 0, initfood, inittech);
        //         Territory territory24 = new Territory("Volantis", 0, initfood, inittech);
                
        //         map.addTerritoryAndNeighbors(territory1, territory2, 2, territory3, 3, territory19, 4);
        //         map.addTerritoryAndNeighbors(territory2, territory1, 2, territory3, 3, territory24, 8);
        //         map.addTerritoryAndNeighbors(territory3, territory1, 3, territory2, 3, territory4, 4, territory5, 9,
        //                         territory6, 8);
        //         map.addTerritoryAndNeighbors(territory4, territory3, 4, territory5, 1, territory9, 6, territory10, 1);
        //         map.addTerritoryAndNeighbors(territory5, territory3, 9, territory4, 1, territory6, 5, territory7, 2,
        //                         territory8, 4, territory9, 4);
        //         map.addTerritoryAndNeighbors(territory6, territory3, 8, territory5, 5, territory7, 4);
        //         map.addTerritoryAndNeighbors(territory7, territory5, 2, territory6, 4, territory8, 7);
        //         map.addTerritoryAndNeighbors(territory8, territory5, 4, territory7, 7, territory9, 2, territory11, 12);
        //         map.addTerritoryAndNeighbors(territory9, territory4, 6, territory5, 4, territory8, 2, territory10, 8,
        //                         territory11, 15, territory12, 5, territory13, 4);
        //         map.addTerritoryAndNeighbors(territory10, territory4, 1, territory9, 8, territory13, 2);
        //         map.addTerritoryAndNeighbors(territory11, territory8, 12, territory9, 15, territory12, 7);
        //         map.addTerritoryAndNeighbors(territory12, territory9, 5, territory11, 7, territory13, 3, territory16,
        //                         7);
        //         map.addTerritoryAndNeighbors(territory13, territory9, 4, territory10, 2, territory12, 3, territory14,
        //                         11, territory15, 9, territory16, 14);
        //         map.addTerritoryAndNeighbors(territory14, territory13, 11, territory15, 5, territory18, 3, territory19,
        //                         3, territory20, 6);
        //         map.addTerritoryAndNeighbors(territory15, territory13, 9, territory14, 5, territory16, 2, territory18,
        //                         3);
        //         map.addTerritoryAndNeighbors(territory16, territory12, 7, territory13, 14, territory15, 2, territory17,
        //                         2, territory18, 4);
        //         map.addTerritoryAndNeighbors(territory17, territory16, 2, territory18, 6);
        //         map.addTerritoryAndNeighbors(territory18, territory14, 3, territory15, 3, territory16, 4, territory17,
        //                         6, territory20, 8, territory22, 1);
        //         map.addTerritoryAndNeighbors(territory19, territory1, 4, territory14, 3, territory20, 5);
        //         map.addTerritoryAndNeighbors(territory20, territory14, 6, territory18, 8, territory19, 5, territory22,
        //                         6, territory23, 10, territory24, 7);
        //         map.addTerritoryAndNeighbors(territory21, territory22, 2);
        //         map.addTerritoryAndNeighbors(territory22, territory18, 1, territory20, 6, territory21, 2, territory23,
        //                         6);
        //         map.addTerritoryAndNeighbors(territory23, territory20, 10, territory22, 6, territory24, 5);
        //         map.addTerritoryAndNeighbors(territory24, territory2, 8, territory20, 7, territory23, 5);

        //         // assign initial groups
        //         int numTperP = 24 / initGroupNum;
        //         ArrayList<Territory> territories = new ArrayList<>(map.getTerritories());
        //         for (int i = 0; i < initGroupNum; i++) {
        //                 for (int j = numTperP * i; j < numTperP * (i + 1); j++) {
        //                         territories.get(j).setOwner(initGroupOwners.get(i));
        //                         initGroupOwners.get(i).addTerritory(territories.get(j));
        //                 }
        //         }
        //         return map;
        // }

}
