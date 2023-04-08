package edu.duke.ece651.team7.server;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public class UpgradeOrder implements Order {
    protected Player issuer;
    protected Territory target;
    protected Level from;
    protected Level to;
    protected int units;

    public UpgradeOrder(Player p, Territory t, Level from, Level to, int u){
        this.issuer = p;
        this.target = t;
        this.from = from;
        this.to = to;
        this.units = u;
    }

   @Override
    public <T> T accept(OrderVisitor<T> visitor) {
        return visitor.visit(this);
    }
    
}
