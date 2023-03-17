package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.GameMap;

public class PathChecker extends OrderRuleChecker {
    /**
     * for MoveOrder, there must be a path from src to dest in issuer's territories
     * for AttackOrder, the src and dest territory must be adjacent
     * @param n
     */
    public PathChecker(OrderRuleChecker n) {
        super(n);
    }

    @Override
    protected String checkMyRule(GameMap map, Order o) {
        if(o.getClass() == AttackOrder.class){

        }else if(o.getClass() == MoveOrder.class){

        }
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkMyRule'");
    }
    
}
