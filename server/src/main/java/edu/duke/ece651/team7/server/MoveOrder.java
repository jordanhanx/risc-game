package edu.duke.ece651.team7.server;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.duke.ece651.team7.shared.*;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public class MoveOrder extends BasicOrder {
    // private Territory src;

    public MoveOrder(Player p, Territory s, Territory d, Map<Level, Integer> u){
        super(p, s, d, u);
    }

    public MoveOrder(Player p, Territory s, Territory d, Object... u) {
        super(p,s,d,u);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass().equals(getClass())){
            MoveOrder other = (MoveOrder) o;
            return issuer.equals(other.issuer) &&  src.equals(other.src)
            && dest.equals(other.dest) && units == other.units;
        }else{
            return false;
        }
    }

    @Override
    public <T> T accept(OrderVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
