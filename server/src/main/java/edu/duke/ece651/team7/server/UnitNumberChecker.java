package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.GameMap;

public class UnitNumberChecker extends OrderRuleChecker{
  /**
   * The newly issued order's src should have enough units to move or attack
   * @param n: next chained rule
   */

    public UnitNumberChecker(OrderRuleChecker n){
        super(n);
    }

    @Override
    protected String checkMyRule(GameMap map, Order O) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkMyRule'");
    }
}
