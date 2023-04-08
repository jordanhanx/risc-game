package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.duke.ece651.team7.shared.Level;

public abstract class BasicOrder implements Order{
    protected Territory src;
    protected Territory dest;
    protected Player issuer;
    protected Map<Level, Integer> units;

    public BasicOrder(Player p, Territory s, Territory d, Object... u){
        issuer = p;
        src = s;
        dest = d;
        units = new LinkedHashMap<Level, Integer>();
        for (int i = 0; i < u.length; i+=2) {
            Level l = (Level)u[i];
            units.put(l,(Integer) u[i+1]);
        }
    }

    public BasicOrder(Player p, Territory s, Territory d, Map<Level, Integer> u){
        issuer = p;
        src = s;
        dest = d;
        units = u;
    }

    // public Player getPlayer(){
    //     return issuer;
    // }
    // public Territory getDest(){
    //     return dest;
    // }

    // public int getUnits(){
    //     return units;
    // }
    // public Territory getSrc(){
    //     return src;
    // }

    // public void increaseUnits(int num){
    //     units+=num;
    // }

    // public void decreaseUnits(int num){
    //     if(units - num >= 0){
    //         units-=num;
    //     }else{
    //         throw new ArithmeticException("units cannot be less than 0");
    //     }
    // }

    


}
