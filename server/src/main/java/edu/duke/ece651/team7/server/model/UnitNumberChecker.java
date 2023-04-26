package edu.duke.ece651.team7.server.model;

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
                    return "UniNumber Checker: Number of Units must be > 0";
                }
                if(order.units.get(l) > order.src.getUnitsNumberByLevel(l, order.issuer)){
                    return "UniNumber Checker: No enough units in the source Territory";
                }
            }
            if(order.useAircraft && (order.units.get(Level.AIRBORNE) == null || order.units.get(Level.AIRBORNE) > 10)){
                return "UniNumber Checker: Aircraft can only take up to 10 AIRBORNE units";
            }
            if(order.getClass() == AttackOrder.class){ //only Ultron units can carry bomb
                AttackOrder aorder = (AttackOrder) o;
                if(aorder.numBomb < 0){
                    return "UniNumber Checker: cannot issue negative amount of bomb";
                }
                if(aorder.numBomb > aorder.units.getOrDefault(Level.ULTRON, 0)){
                    return "UniNumber Checker: More Bomb than Ultron units";
                }
                return null;
            }
            return null;

        }
        if(o.getClass() == UpgradeOrder.class){
            UpgradeOrder order = (UpgradeOrder) o;
            if(order.units > order.target.getUnitsNumberByLevel(order.from, order.issuer)){
                return "UniNumber Checker: No enough units in the source Territory";
            }else{
                return null;
            }
        }
        return null;
    }
}
