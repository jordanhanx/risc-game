package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.FoodResource;
import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Resource;

public class CostChecker extends OrderRuleChecker {
    private OrderCostVisitor costVisitor;

    public CostChecker(OrderRuleChecker n, GameMap map) {
        super(n);
        costVisitor = new OrderCostVisitor(map);
    }

    @Override
    protected String checkMyRule(GameMap map, Order o) {
        if(o.getClass() == AttackOrder.class || o.getClass() == MoveOrder.class){
            return checkBasicOrder(map, (BasicOrder) o);
        }else if(o.getClass() == UpgradeOrder.class){
            return checkUpgradeOrder(map, (UpgradeOrder) o);
        }else{
            return checkResearchOrder(map, (ResearchOrder) o);
        }
    }

    private String checkBasicOrder(GameMap map, BasicOrder o){
        Resource food = o.accept(costVisitor);
        if(o.issuer.getFood().compareTo(food) < 0){
            return "CostCheker error: No enough food.";
        }else{
            return null;
        }

    }

    private String checkUpgradeOrder(GameMap map, UpgradeOrder o){
        Resource tech = o.accept(costVisitor);
        if(o.issuer.getTech().compareTo(tech) < 0){
            return "CostCheker error: No enough Tech.";
        
        }else{
            return null;
        }
    }

    private String checkResearchOrder(GameMap map, ResearchOrder o){
        Resource tech = o.accept(costVisitor);
        if(o.issuer.getTech().compareTo(tech) < 0){
            return "CostCheker error: No enough Tech.";
        }else{
            return null;
        }
    }
}
