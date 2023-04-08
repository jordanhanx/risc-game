package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

import java.util.Map;

import edu.duke.ece651.team7.shared.Level;

public class AttackOrder extends BasicOrder{
    public AttackOrder(Player p, Territory s, Territory d, Map<Level, Integer> u){
        super(p, s, d, u);
    }

    public AttackOrder(Player p, Territory s, Territory d, Object... u) {
        super(p,s,d,u);
    }
    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass().equals(getClass())){
            AttackOrder other = (AttackOrder) o;
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
