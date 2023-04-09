package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.GameMap;

public class UnitNumberChecker extends OrderRuleChecker {
    /**
     * The newly issued order's src should have enough units to move or attack,
     * the issued unit should be > 0;
     * 
     * @param n: next chained rule
     */

    public UnitNumberChecker(OrderRuleChecker n) {
        super(n);
    }

    @Override
    protected String checkMyRule(GameMap map, Order o) {
        if (o.getUnits() <= 0) {
            return "Number of Units must be > 0";
        }
        if (o.getUnits() > o.getSrc().getUnits()) {
            return "No enough units in the source Territory";
        }
        return null;
    }
}
