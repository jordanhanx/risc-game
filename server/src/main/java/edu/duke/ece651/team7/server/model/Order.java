package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public abstract class Order {
    protected Territory src;
    protected Player issuer;
    protected Territory dest;
    protected int units;

    public Order(Player p, Territory s, Territory d, int u) {
        issuer = p;
        src = s;
        dest = d;
        units = u;
    }

    public Player getPlayer() {
        return issuer;
    }

    public Territory getDest() {
        return dest;
    }

    public int getUnits() {
        return units;
    }

    public Territory getSrc() {
        return src;
    }

    public void increaseUnits(int num) {
        units += num;
    }

    public void decreaseUnits(int num) {
        if (units - num >= 0) {
            units -= num;
        } else {
            throw new ArithmeticException("units cannot be less than 0");
        }
    }

}
