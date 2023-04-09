package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;

public class UnitNumberChecker extends OrderRuleChecker{
  /**
   * The newly issued order's src should have enough units to move or attack,
   * the issued unit should be > 0;
   * @param n: next chained rule
   */

    public UnitNumberChecker(OrderRuleChecker n){
        super(n);
    }

    @Override
    protected String checkMyRule(GameMap map, Order o) {
        if(o.getClass() == AttackOrder.class || o.getClass() == MoveOrder.class){
            BasicOrder order = (BasicOrder) o;
            for(Level l: order.units.keySet()){
                if(order.units.get(l) <= 0){
                    return "Number of Units must be > 0";
                }
                if(order.units.get(l) > order.src.getUnitsNumberByLevel(l)){
                    return "No enough units in the source Territory";
                }
            }
            return null;
        }

        if(o.getClass() == UpgradeOrder.class){
            UpgradeOrder order = (UpgradeOrder) o;
            if(order.units > order.target.getUnitsNumberByLevel(order.from)){
                return "No enough units in the source Territory";
            }else{
                return null;
            }
        }
        return null;
    }
}
