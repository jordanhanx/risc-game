package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public class MoveOrder extends Order {
    private Territory src;

    public MoveOrder(Player p, Territory s, Territory d, int u) {
        super(p, d, u);
        src = s;
    }

    public Territory getSrc(){
        return src;
    }

}
