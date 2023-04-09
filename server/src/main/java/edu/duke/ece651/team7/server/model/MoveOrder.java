package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public class MoveOrder extends Order {
    // private Territory src;

    public MoveOrder(Player p, Territory s, Territory d, int u) {
        super(p, s, d, u);
    }

    // public Territory getSrc(){
    // return src;
    // }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(getClass())) {
            MoveOrder other = (MoveOrder) o;
            return issuer.equals(other.getPlayer()) && src.equals(other.getSrc())
                    && dest.equals(other.getDest()) && units == other.getUnits();
        } else {
            return false;
        }
    }

}