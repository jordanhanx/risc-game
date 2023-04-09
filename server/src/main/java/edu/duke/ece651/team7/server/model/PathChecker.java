package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.GameMap;

public class PathChecker extends OrderRuleChecker {
    /**
     * for MoveOrder, there must be a path from src to dest in issuer's territories
     * for AttackOrder, the src and dest territory must be adjacent
     * 
     * @param n
     */
    public PathChecker(OrderRuleChecker n) {
        super(n);
    }

    @Override
    protected String checkMyRule(GameMap map, Order o) {
        if (!o.getSrc().getOwner().equals(o.getPlayer())) {
            return "Access Denied: source Territory does not belong to you";
        }
        if (o.getClass() == AttackOrder.class) {
            if (o.getDest().getOwner().equals(o.getPlayer())) {
                return "Cannot attack your own territory";
            }
            if (!map.isAdjacent(o.getSrc().getName(), o.getDest().getName())) {
                return "Can only attack adjacent territory";
            }
        } else {
            // dest is not issuer's
            if (!o.getDest().getOwner().equals(o.getPlayer())) {
                return "Access Denied: destination Territory does not belong to you";
            }
            // do not have a path
            if (!map.hasPath(o.getSrc().getName(), o.getDest().getName())) {
                return "Path does not exists between these two Territories";
            }
        }
        return null;
    }

}
