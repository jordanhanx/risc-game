package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.*;

public class ManufactureOrder implements Order{

    protected Player issuer;
    protected boolean bomb;
    protected int amount;

    public ManufactureOrder(Player p, boolean isBomb, int num){
        this.issuer = p;
        bomb = isBomb;
        amount = num;
    }

    @Override
    public <T> T accept(OrderVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass().equals(getClass())){
            ManufactureOrder other = (ManufactureOrder) o;
            return issuer.equals(other.issuer) &&  bomb == other.bomb
            && amount == other.amount;
        }else{
            return false;
        }
    }
}
