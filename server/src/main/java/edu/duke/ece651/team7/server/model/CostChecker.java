package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.FoodResource;
import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Resource;
import edu.duke.ece651.team7.shared.TechResource;

public class CostChecker extends OrderRuleChecker {
    private OrderCostVisitor costVisitor;

    public CostChecker(OrderRuleChecker n, OrderCostVisitor costVisitor) {
        super(n);
        this.costVisitor = costVisitor;
    }

    @Override
    protected String checkMyRule(GameMap map, Order o) {
        if(o.getClass() == AttackOrder.class || o.getClass() == MoveOrder.class){
            return checkBasicOrder(map, (BasicOrder) o);
        }else if(o.getClass() == UpgradeOrder.class){
            return checkUpgradeOrder(map, (UpgradeOrder) o);
        }else if(o.getClass() == ResearchOrder.class){
            return checkResearchOrder(map, (ResearchOrder) o);
        }else{
            return checkManufactureOrder(map, (ManufactureOrder) o);
        }
    }

    private String checkBasicOrder(GameMap map, BasicOrder o){
        FoodResource food = (FoodResource)o.accept(costVisitor);
        if(o.issuer.getFood().compareTo(food) < 0){
            return "CostCheker error: No enough food.";
        }
        if(o.useAircraft && o.issuer.getAircraft().size()==0){
            return "CostCheker error: No enough aircraft.";
        }
        if(o.getClass() == AttackOrder.class){
            AttackOrder order = (AttackOrder) o;
            if (o.issuer.getBomb() < order.numBomb){
                return "CostCheker error: No enough Bomb.";
            }
        }
        return null;

    }

    private String checkUpgradeOrder(GameMap map, UpgradeOrder o){
        Resource tech = o.accept(costVisitor);
        if(o.issuer.getTech().compareTo((TechResource)tech) < 0){
            return "CostCheker error: No enough Tech.";
        
        }else{
            return null;
        }
    }

    private String checkResearchOrder(GameMap map, ResearchOrder o){
        Resource tech = o.accept(costVisitor);
        if(o.issuer.getTech().compareTo((TechResource)tech) < 0){
            return "CostCheker error: No enough Tech.";
        }else{
            return null;
        }
    }

    private String checkManufactureOrder(GameMap map, ManufactureOrder o){
        Resource tech = o.accept(costVisitor);
        if(o.issuer.getTech().compareTo((TechResource)tech) < 0){
            return "CostCheker error: No enough Tech.";
        }else{
            return null;
        }
    }
}
