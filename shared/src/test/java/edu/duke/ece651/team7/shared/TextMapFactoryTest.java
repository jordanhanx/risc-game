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
        assertEquals(true, twoPlayerMap.isAdjacent("territory1", "territory2"));
        assertEquals(true, twoPlayerMap.hasPath("territory1", "territory2"));

    }

    @Test
    public void test_createThreePlayerMap(){
        TextMapFactory mf = new TextMapFactory();
        GameMap threePlayerMap = mf.createThreePlayerMap();
        assertEquals(true, threePlayerMap.isAdjacent("territory2", "territory3"));
        assertEquals(true, threePlayerMap.isAdjacent("territory1", "territory2"));
        assertEquals(true, threePlayerMap.isAdjacent("territory1", "territory3"));
        assertEquals(true, threePlayerMap.hasPath("territory1", "territory2"));
        assertEquals(true, threePlayerMap.hasPath("territory1", "territory3"));
        assertEquals(true, threePlayerMap.hasPath("territory3", "territory2"));

    }

    
}
