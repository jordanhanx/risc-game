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
            if(order.issuer.getCurrentMaxLevel().equals(Level.ULTRON)){
                return "LevelChecker error: Already the highest level.";
            }
            return null;
        }else if(o.getClass() == AttackOrder.class || o.getClass() == MoveOrder.class){
            System.out.print(1);
            BasicOrder order = (BasicOrder) o;
            if(order.useAircraft && order.units.get(Level.AIRBORNE) == null){ //only AIRBORNE units can carry navigate Aircraft
                return "LevelChecker error: only AIRBORNE units can carry navigate Aircraft.";
            }
            if(order.getClass() == AttackOrder.class){ //only Ultron units can carry bomb
                AttackOrder aorder = (AttackOrder) o;
                if(aorder.units.get(Level.ULTRON) == null && aorder.numBomb > 0){
                    return "LevelChecker error: only Ultron units can carry Bomb.";
                }
                return null;
            }
            return null;
        }
        else{
            return null;
        }
        
    }
}
