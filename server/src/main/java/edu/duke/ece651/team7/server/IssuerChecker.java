package edu.duke.ece651.team7.server;

import edu.duke.ece651.team7.shared.GameMap;

public class IssuerChecker extends OrderRuleChecker {
    /**
     * For move order, issuer should own the src and dest territory
     * For Attack order, issuer should own src territoty
     * @param n
     */
    public IssuerChecker(OrderRuleChecker n) {
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
