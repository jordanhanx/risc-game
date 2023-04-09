package edu.duke.ece651.team7.server.model;
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

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass().equals(getClass())){
            ResearchOrder other = (ResearchOrder) o;
            return issuer.equals(other.issuer);
        }else{
            return false;
        }
    }

}
