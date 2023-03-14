package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.Territory;

public abstract class Order {
    private Territory src;
    private Territory dest;
    private int units;

    public Order(Territory s, Territory d, int u){
        src = s;
        dest = d;
        units = u;
    }

    public Territory getSrc(){
        return src;
    }

    public Territory getDest(){
        return dest;
    }

    public int getUnits(){
        return units;
    }

    public boolean equals(Object o){
        if(o != null && o.getClass().equals(getClass())){
            Order other = (Order) o;
            return src == other.getSrc() && dest == other.getDest() && units == other.getUnits();
        }else{
            return false;
        }
    }


}
