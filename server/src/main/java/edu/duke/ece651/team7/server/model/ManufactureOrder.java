package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.*;

public class ManufactureOrder implements Order{

    protected Player issuer;
    protected boolean bomb;

    public ManufactureOrder(Player p, boolean isBomb){
        this.issuer = p;
        bomb = isBomb;
    }

    @Override
    public <T> T accept(OrderVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
