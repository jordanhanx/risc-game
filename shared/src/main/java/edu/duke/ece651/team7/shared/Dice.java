package edu.duke.ece651.team7.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Dice {
    /**
     * @param faces number of faces of the dice
     */
    private final int faces;
    private Map<Level, Integer> Bonus;


    public Dice(int n){
        if(n>0){
            faces = n;
        }else{
            throw new IllegalArgumentException("Invalid Dice");
        }
        this.Bonus = new HashMap<>();
        this.Bonus.put(Level.valueOfLabel(0), 0);
        this.Bonus.put(Level.valueOfLabel(1), 1);
        this.Bonus.put(Level.valueOfLabel(2), 3);
        this.Bonus.put(Level.valueOfLabel(3), 5);
        this.Bonus.put(Level.valueOfLabel(4), 8);
        this.Bonus.put(Level.valueOfLabel(5), 11);
        this.Bonus.put(Level.valueOfLabel(6), 15);
    }
    
    /**
     * Throw the dice and return the value of the dice
     * @return
     */
    public int throwDice(){
        Random rand = new Random();
        return rand.nextInt(faces)+1;
    }    

     /**
     * Throw the dice and return the value of the dice
     * @return
     */
    public int throwDicewithBonus(Level l){
        return throwDice() + Bonus.get(l);
    }    
}
