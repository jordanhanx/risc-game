package edu.duke.ece651.team7.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMapFactory implements MapFactory {

    // @Override
    // public void createTerritory(){

    // }
    
    /**
    *Creates a two-player game map with two territories and their adjacency information.
    *@return a new instance of GameMap representing the two-player game map
    */
    @Override
    public GameMap createTwoPlayerMap(){
        Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
        Territory territory1 = new Territory("territory1");
        Territory territory2 = new Territory("territory2");
        territoriesAdjacentList.put(territory1, new ArrayList<>(List.of(territory2)));
        territoriesAdjacentList.put(territory2, new ArrayList<>(List.of(territory1)));
        GameMap twoPlayerMap = new GameMap(territoriesAdjacentList);
        return twoPlayerMap;
    
    }

    /**
    *Creates a three-player game map with three territories and their adjacency information.
    *@return a new instance of GameMap representing the three-player game map
    */
    @Override
    public GameMap createThreePlayerMap(){
        List<Territory> territories = new ArrayList<Territory>();
        Territory territory1 = new Territory("territory1");
        Territory territory2 = new Territory("territory2");
        Territory territory3 = new Territory("territory3");
        territories.add(territory1);
        territories.add(territory2);
        territories.add(territory3);   
        Map<Territory, List<Territory>> territoriesAdjacentList = new HashMap<>();
        territoriesAdjacentList.put(territory1, new ArrayList<Territory>(Arrays.asList(territory2, territory3)));
        territoriesAdjacentList.put(territory2, new ArrayList<Territory>(Arrays.asList(territory1, territory3)));
        territoriesAdjacentList.put(territory3, new ArrayList<Territory>(Arrays.asList(territory1, territory2)));  
        GameMap threePlayerMap = new GameMap(territoriesAdjacentList);
        return threePlayerMap;
    }

    @Override
    public GameMap createMapTest(){
        // List<Territory> territories = new ArrayList<Territory>();
        Territory territory1 = new Territory("Narnia");
        Territory territory2 = new Territory("Elantris");
        Territory territory3 = new Territory("Midkemia");
        Territory territory4 = new Territory("Ravenclaw");
        Territory territory5 = new Territory("Scadrial");
        Territory territory6 = new Territory("Roshar");
        Territory territory7 = new Territory("Gryffindor");
        Territory territory8 = new Territory("Hufflepuff");
        Territory territory9 = new Territory("Slytherin");
         
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
