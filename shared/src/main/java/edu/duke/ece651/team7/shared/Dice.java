package edu.duke.ece651.team7.shared;

import java.util.Random;

public class Dice {
    private final int faces;

    public Dice(int n){
        if(n>0){
            faces = n;
        }else{
            throw new IllegalArgumentException("Invalid Dice");
        }
    }
    
    public int throwDice(){
        Random rand = new Random();
        return rand.nextInt(faces)+1;
    }    
}
