package edu.duke.ece651.team7.shared;

import java.util.HashSet;
import java.util.Set;

public class TextMapFactory implements MapFactory {

    // @Override
    // public void createTerritory(){

    // }

    @Override
    public GameMap createTwoPlayerMap(){
        Set<Territory> territories = new HashSet<Territory>();
        Territory territory = new Territory("player1");
        Territory territory2 = new Territory("player2");
        territories.add(territory);
        territories.add(territory2);

        GameMap twoPlayerMap = new GameMap(territories);
        return twoPlayerMap;
    }

    @Override
    public GameMap createThreePlayerMap(){
        Set<Territory> territories = new HashSet<Territory>();
        Territory territory = new Territory("player1");
        Territory territory2 = new Territory("player2");
        Territory territory3 = new Territory("player3");
        territories.add(territory);
        territories.add(territory2);
        territories.add(territory3);
        GameMap threePlayerMap = new GameMap(territories);
        return threePlayerMap;
    }


}
