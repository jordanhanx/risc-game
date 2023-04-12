package edu.duke.ece651.team7.shared;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DiceTest {
    @Test
    public void test_throw(){
        assertThrows(IllegalArgumentException.class, () -> new Dice(-1));
        Dice d = new Dice(9);
        assertTrue(d.throwDice()<=9 && d.throwDice()>0);
        assertTrue(d.throwDice()<=9 && d.throwDice()>0);
        assertTrue(d.throwDice()<=9 && d.throwDice()>0);
        assertTrue(d.throwDice()<=9 && d.throwDice()>0);
        assertTrue(d.throwDice()<=9 && d.throwDice()>0);
    }

    @Test
    public void test_throwWithbonus(){
        assertThrows(IllegalArgumentException.class, () -> new Dice(-1));
        Dice d = new Dice(20);
        for(int i  = 0; i<10; i++){
            assertTrue(d.throwDicewithBonus(Level.CIVILIAN)>0 && d.throwDicewithBonus(Level.CIVILIAN)<= 20);
        }
        for(int i  = 0; i<10; i++){
            assertTrue(d.throwDicewithBonus(Level.ULTRON)>0 && d.throwDicewithBonus(Level.ULTRON)<= 35);
        }
    }
}
