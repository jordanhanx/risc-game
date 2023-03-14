package edu.duke.ece651.team7.server;

import java.util.ArrayList;

import edu.duke.ece651.team7.shared.Dice;
import edu.duke.ece651.team7.shared.GameMap;

public class OrderExecuter {
    private Dice dice;
    private ArrayList<Order> attackOrderPool;

    public OrderExecuter(int num){
        dice = new Dice(num);
        attackOrderPool = new ArrayList<Order>();
    }

    public String doOneMove(Order o, GameMap m){
        return null;
    }

    public String doOneAttack(Order o, GameMap m){
        return null;
    }

    public String pushCombat(Order o, GameMap m){
        return null;
    }

    public String doAllCombats(){
        return null;

    }
}
