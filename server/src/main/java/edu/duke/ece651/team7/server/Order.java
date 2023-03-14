package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public abstract class Order {
    // private Territory src;
    private Player issuer;
    private Territory dest;
    private int units;

    public Order(Player p, Territory d, int u){
        // src = s;
        issuer = p;
        dest = d;
        units = u;
    }

    public Player getPlayer(){
        return issuer;
    }
    public Territory getDest(){
        return dest;
    }

    public int getUnits(){
        return units;
    }

    public void increaseUnitsBy(int num){
        units+=num;
    }

    public void decreaseUnitsBy(int num){
        units-=num;
    }

    // public boolean equals(Object o){
    //     if(o != null && o.getClass().equals(getClass())){
    //         Order other = (Order) o;
    //         return dest == other.getDest() && units == other.getUnits();
    //     }else{
    //         return false;
    //     }
    // }

    


}
