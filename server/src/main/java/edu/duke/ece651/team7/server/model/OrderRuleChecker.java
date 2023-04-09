package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.GameMap;

public abstract class OrderRuleChecker {
    /**
     * 
     * @param next the chained next rule for validating order
     */
    private OrderRuleChecker next;

    public OrderRuleChecker(OrderRuleChecker n){
        this.next = n;
    }

    protected abstract String checkMyRule(GameMap map, Order o);

    /**
     * recursively cheack all rules
     * @param map Game Map
     * @param o order to be checked
     * @return false if one rule is not satisfied
     * @return true if pass all rules 
     */
    public String checkOrderValidity(GameMap map, Order o) {
        String error_message = checkMyRule(map, o);
        if (error_message!= null) {
          return error_message;
        }
        // other wise, ask the rest of the chain.
        if (next != null) {
          return next.checkOrderValidity(map, o);
        }
        // if there are no more rules, then the placement is legal
        return null;
      }


}
