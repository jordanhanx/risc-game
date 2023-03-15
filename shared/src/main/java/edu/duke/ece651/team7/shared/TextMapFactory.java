package edu.duke.ece651.team7.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMapFactory implements MapFactory {

    @Override
    public GameMap createMap(){
        Player p1 = new Player("blue");
        Player p2 = new Player("purple");
        Player p3 = new Player("red");
        Territory territory1 = new Territory("Narnia", p1, 10);
        Territory territory2 = new Territory("Elantris", p2, 6);
        Territory territory3 = new Territory("Midkemia", p1,12);
        Territory territory4 = new Territory("Oz",p1, 8);
        Territory territory5 = new Territory("Scadrial", p2,5);
        Territory territory6 = new Territory("Roshar",p2,3);
        Territory territory7 = new Territory("Gondor",p3,13);
        Territory territory8 = new Territory("Mordor",p3,14);
        Territory territory9 = new Territory("Hogwarts",p3,3);
         
        Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
        territoriesAdjacentList.put(territory1, new ArrayList<Territory>(Arrays.asList(territory2, territory3)));
        territoriesAdjacentList.put(territory2, new ArrayList<Territory>(Arrays.asList(territory1, territory3, territory5, territory6)));
        territoriesAdjacentList.put(territory3, new ArrayList<Territory>(Arrays.asList(territory1, territory2, territory4, territory5)));
        territoriesAdjacentList.put(territory4, new ArrayList<Territory>(Arrays.asList(territory7, territory3, territory5, territory8)));
        territoriesAdjacentList.put(territory5, new ArrayList<Territory>(Arrays.asList(territory2, territory3, territory4, territory6, territory9, territory8)));
        territoriesAdjacentList.put(territory6, new ArrayList<Territory>(Arrays.asList(territory9, territory2, territory5)));  
        territoriesAdjacentList.put(territory7, new ArrayList<Territory>(Arrays.asList(territory4, territory8)));
        territoriesAdjacentList.put(territory8, new ArrayList<Territory>(Arrays.asList(territory4, territory7, territory5, territory9)));
        territoriesAdjacentList.put(territory9, new ArrayList<Territory>(Arrays.asList(territory6, territory5, territory8)));  
          
        GameMap newMap = new GameMap(territoriesAdjacentList);
        return newMap;
    }

    @Override
    public GameMap createMapTest(){
        // List<Territory> territories = new ArrayList<Territory>();
        Territory territory1 = new Territory("Narnia");
        Territory territory2 = new Territory("Elantris");
        Territory territory3 = new Territory("Midkemia");
        Territory territory4 = new Territory("Oz");
        Territory territory5 = new Territory("Scadrial");
        Territory territory6 = new Territory("Roshar");
        Territory territory7 = new Territory("Gondor");
        Territory territory8 = new Territory("Mordor");
        Territory territory9 = new Territory("Hogwarts");
         
        Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
        territoriesAdjacentList.put(territory1, new ArrayList<Territory>(Arrays.asList(territory2, territory3)));
        territoriesAdjacentList.put(territory2, new ArrayList<Territory>(Arrays.asList(territory1, territory3, territory5, territory6)));
        territoriesAdjacentList.put(territory3, new ArrayList<Territory>(Arrays.asList(territory1, territory2, territory4, territory5)));
        territoriesAdjacentList.put(territory4, new ArrayList<Territory>(Arrays.asList(territory7, territory3, territory5, territory8)));
        territoriesAdjacentList.put(territory5, new ArrayList<Territory>(Arrays.asList(territory2, territory3, territory4, territory6, territory9, territory8)));
        territoriesAdjacentList.put(territory6, new ArrayList<Territory>(Arrays.asList(territory9, territory2, territory5)));  
        territoriesAdjacentList.put(territory7, new ArrayList<Territory>(Arrays.asList(territory4, territory8)));
        territoriesAdjacentList.put(territory8, new ArrayList<Territory>(Arrays.asList(territory4, territory7, territory5, territory9)));
        territoriesAdjacentList.put(territory9, new ArrayList<Territory>(Arrays.asList(territory6, territory5, territory8)));  
          
        GameMap threePlayerMap = new GameMap(territoriesAdjacentList);
        return threePlayerMap;
    }



}
