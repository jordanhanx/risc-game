package edu.duke.ece651.team7.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TextMapFactory implements MapFactory {

    // @Override
    // public void createTerritory(){

    // }

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


}
