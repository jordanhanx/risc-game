package edu.duke.ece651.team7.server.model;

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
        if(o.getClass() == AttackOrder.class || o.getClass() == MoveOrder.class){
            BasicOrder order = (BasicOrder) o;
            if(!order.src.getOwner().equals(order.issuer) && !order.src.getOwner().isAlliance(order.issuer)){
                return "Access Denied: source Territory does not belong to you/your alliance";
            }
            if(order.getClass() == AttackOrder.class){
                return checkAttackRule(map, (AttackOrder)order);
            }else{
                return checkMoveRule(map, (MoveOrder)order);
            }
        }else if(o.getClass() == UpgradeOrder.class){
            UpgradeOrder order = (UpgradeOrder) o;
            if(!order.target.getOwner().equals(order.issuer) && !order.target.getOwner().isAlliance(order.issuer)){
                return "Access Denied: target Territory does not belong to you/your alliance";
            }
            return null;
        }else{
            return null;
        }
    }

    private String checkAttackRule(GameMap map, AttackOrder order){
        if(order.dest.getOwner().equals(order.issuer)){
            return "Cannot attack your own territory";
        }
        if(!map.isAdjacent(order.src.getName(), order.dest.getName()) && !order.useAircraft){
            return "Can only attack adjacent territory";
        }
        return null;
    }

    private String checkMoveRule(GameMap map, MoveOrder order){
        if(!order.dest.getOwner().equals(order.issuer) && !order.dest.getOwner().isAlliance(order.issuer)){
            return "Access Denied: destination Territory does not belong to you/your alliance";
        }
        //do not have a path
        if(!map.hasPath(order.src.getName(), order.dest.getName()) && !order.useAircraft){
            return "Path does not exists between these two Territories";
        }
        return null;
    }


    
}
