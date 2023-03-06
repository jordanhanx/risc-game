package edu.duke.ece651.team7.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class TextMapFactoryTest {
    @Test
    public void test_createTwoPlayerMap(){
        TextMapFactory mf = new TextMapFactory();
        Set<Territory> res= mf.createTwoPlayerMap().getTerritories();
        assertEquals(2,res.size());
        for(Territory t: res){
            if(t.getName().equals("player1")){
            assertEquals("player1", t.getName());
            }else{
            assertEquals("player2", t.getName());
            }
        }
    }

    @Test
    public void test_createThreePlayerMap(){
        TextMapFactory mf = new TextMapFactory();
        Set<Territory> res= mf.createThreePlayerMap().getTerritories();
        assertEquals(3,res.size());

    }
}
