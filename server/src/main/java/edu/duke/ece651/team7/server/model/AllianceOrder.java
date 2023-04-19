package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.Player;

public class AllianceOrder implements Order{
    protected Player issuer;
    protected Player alliance;

    public AllianceOrder(Player issu, Player alli){
        this.issuer = issu;
        this.alliance = alli;
    }

    @Override
    public <T> T accept(OrderVisitor<T> visitor) {
       return visitor.visit(this);
    }
    
}
