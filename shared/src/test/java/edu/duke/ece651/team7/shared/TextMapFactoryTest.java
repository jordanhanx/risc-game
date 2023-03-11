package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class TextMapFactoryTest {
    @Test
    public void test_createTwoPlayerMap(){
        TextMapFactory mf = new TextMapFactory();
        GameMap twoPlayerMap = mf.createTwoPlayerMap();

        //if don't override the equal, hashcode method in Territory,  GameMap class

        // List<Territory> mapTerritories = twoPlayerMap.getTerritories();
        // assertEquals(2, mapTerritories.size());
        // List<String> expectedTerritories = Arrays.asList("player1", "player2");
        // List<String> actualTerritories = new ArrayList<String>();
        // for (Territory territory : twoPlayerMap.getTerritories()) {
        //      actualTerritories.add(territory.getName());
        // }
        // assertEquals(expectedTerritories, actualTerritories);

        
        //if override the equal, hashcode method in Territory,  GameMap class 
        List<Territory> expectedTerritories2 = new ArrayList<Territory>(Arrays.asList(new Territory("player1"),new Territory("player2")));
        GameMap twoPlayerMapExpected  = new GameMap(expectedTerritories2);
        assertEquals(twoPlayerMap, twoPlayerMapExpected);
  



    }

    @Test
    public void test_createThreePlayerMap(){
        TextMapFactory mf = new TextMapFactory();
        GameMap threePlayerMap = mf.createThreePlayerMap();

        //if don't override the equal, hashcode method in Territory,  GameMap class

        // List<Territory> mapTerritories = threePlayerMap.getTerritories();
        // assertEquals(3, mapTerritories.size());
        // List<String> expectedTerritories = Arrays.asList("player1", "player2","player3");
        // List<String> actualTerritories = new ArrayList<String>();
        // for (Territory territory : threePlayerMap.getTerritories()) {
        //      actualTerritories.add(territory.getName());
        // }
        // assertEquals(expectedTerritories, actualTerritories);


        //if override the equal, hashcode method in Territory,  GameMap class
        List<Territory> expectedTerritories2 = Arrays.asList(new Territory("player1"),new Territory("player2"), new Territory("player3"));
        GameMap threePlayerMapExpected  = new GameMap(expectedTerritories2);
        assertEquals(threePlayerMap, threePlayerMapExpected);
    }
}
