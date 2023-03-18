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

        GameMap newMap = new GameMap(territoriesAdjacentList);
        return newMap;
    }

    @Override

    public GameMap createMapTest() {
        /**
        apply the userPickTerritoryGroup method to build the map
         */
        Territory territory1 = new Territory("Narnia",null, 10);
        Territory territory7 = new Territory("Elantris", null, 6);
        Territory territory2 = new Territory("Midkemia", null, 12);
        Territory territory3 = new Territory("Oz", null, 8);
        Territory territory8 = new Territory("Scadrial", null, 5);
        Territory territory9 = new Territory("Roshar", null, 3);
        Territory territory4 = new Territory("Gondor", null, 13);
        Territory territory5 = new Territory("Mordor", null, 14);
        Territory territory6 = new Territory("Hogwarts", null, 3);

    
        Map<Territory, List<Territory>> territoriesAdjacentList = new LinkedHashMap<>();
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
    
        GameMap newMap = new GameMap(territoriesAdjacentList);
        ArrayList<ArrayList<Territory> > terrgroups = new ArrayList<ArrayList<Territory> >();
        ArrayList<ArrayList<Territory> > terrlists = newMap.userPickTerritoryGroup(terrgroups, "GroupA", "PlayerA");
        terrlists = newMap.userPickTerritoryGroup(terrlists, "GroupB", "PlayerB");
        terrlists = newMap.userPickTerritoryGroup(terrlists, "GroupC", "PlayerC");

        for(ArrayList<Territory> terrList: terrgroups){
           for(Territory terr: terrList){
                for(Territory terrAdj: territoriesAdjacentList.keySet() ){
                        if(terr.getName().equals(terrAdj.getName())){
                                List<Territory> value = territoriesAdjacentList.get(terrAdj);
                                territoriesAdjacentList.remove(terrAdj); 
                                territoriesAdjacentList.put(terr,value );
                        }
                }
           }
        }
        GameMap finalMap = new GameMap(territoriesAdjacentList);
        return finalMap;
    }

    

}
