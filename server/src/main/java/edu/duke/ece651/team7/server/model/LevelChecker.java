package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;

public class LevelChecker extends OrderRuleChecker{
    

    public LevelChecker(OrderRuleChecker n) {
        super(n);
    }

    @Override
    protected String checkMyRule(GameMap map, Order o) {
        if(o.getClass() == UpgradeOrder.class){
            UpgradeOrder order = (UpgradeOrder) o;
            if(order.from.compareTo(order.to) >= 0){
                return "LevelChecker error: Cannot upgrade to a lower or equal level";
            }
            if(order.to.compareTo(order.issuer.getCurrentMaxLevel())>0){
                return "LevelChecker error: Your current Maximum level is " + order.issuer.getCurrentMaxLevel();
            }
            return null;
        }else if(o.getClass() == ResearchOrder.class){
            ResearchOrder order = (ResearchOrder) o;
            if(order.issuer.getCurrentMaxLevel() == Level.ULTRON){
                return "LevelChecker error: Already the highest level.";
            }
            return null;
        }else{
            return null;
        }
        
    }
}
