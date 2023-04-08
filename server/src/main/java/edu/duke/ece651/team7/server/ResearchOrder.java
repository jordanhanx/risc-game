package edu.duke.ece651.team7.server;
import edu.duke.ece651.team7.shared.Player;

public class ResearchOrder implements Order{

    protected Player issuer;

    public ResearchOrder(Player p){
        issuer = p;
    }

   @Override
    public <T> T accept(OrderVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
