package edu.duke.ece651.team7.server.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Resource;
import edu.duke.ece651.team7.shared.Territory;
import edu.duke.ece651.team7.shared.Unit;

public class OrderExecuteVisitor implements OrderVisitor<String>{
    /**
     * @param checker the rule checker of order, checker whether the order is valid
     * @param map the gamemap
     * @param combatPool stores the requested combats
     * 
     */
    private OrderRuleChecker checker;
    private GameMap map;
    private List<Combat> combatPool;
    private OrderCostVisitor costVisitor;
    // private final PrintStream out;

    /**
     * 
     * @param map the GameMap of the game
     */
    public OrderExecuteVisitor(GameMap map){
        this.map = map;
        this.combatPool = new ArrayList<Combat>();
        this.costVisitor = new OrderCostVisitor(map);
        // this.out = out;
        checker = new PathChecker(null);
        checker = new UnitNumberChecker(checker);
        checker = new CostChecker(checker, costVisitor);
        
    }
    /**
     * Check if the issued Attack order's destination has already formed a combat
     * @param o Player's order
     * @return if the combat exists, return the combat. if not return null.
     */
    protected Combat isInCombatPool(Territory t){
        for (Combat c : combatPool){
            if(c.getBattlefield().equals(t)){
                return c;
            }
        }
        return null;
    }

    /**
     * when a player issues an attack order, let units depart form the territory,
     * but not arrive at the target territory
     * @param o AttackOrder issued by a player
     * @throws IllegalArgumentException if the order is not valid
     */
    protected void pushCombat(AttackOrder o) throws IllegalArgumentException{
        
        ArrayList<Unit> departUnits = new ArrayList<>();
        for(Level l: o.units.keySet()){
            departUnits.addAll(o.src.removeUnits(l, o.units.get(l)));
        }
        Combat targetCombat = isInCombatPool(o.dest);
        if(targetCombat != null){
            targetCombat.pushAttack(o.issuer, departUnits);
            // System.out.print("Player " + o.getPlayer().getName() +  ": [A " + o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]: ");
            // System.out.println("Player " + o.getPlayer().getName()+ " joins Combat at " + o.getDest().getName() + " from " + o.getSrc().getName() +" with " + o.getUnits() + " units");
            
        }else{
            targetCombat = new Combat(o.dest);
            targetCombat.pushAttack(o.issuer, departUnits);
            combatPool.add(targetCombat);
            // System.out.print( "Player " + o.getPlayer().getName() +  ": [A " + o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]: ");
            // System.out.println("Player " + o.getPlayer().getName()+ " adds new Combat at " + o.getDest().getName() + " from " + o.getSrc().getName()+ " with " + o.getUnits() + " units");
        }
    }

    /**
     * resolve all combats saved in combatPool and add one unit to each territory
     */
    protected void doAllCombats(){
        for(Combat c : combatPool){
            c.resolveCombat();
        }
        for(Territory t: map.getTerritories()){
            t.addUnits(new Unit());
        }
        combatPool.clear();
    }

    /**
     * 
     */
    @Override
    public String visit(MoveOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            Resource food = order.accept(costVisitor);
            order.issuer.getFood().comsumeResoure(food.getAmount());
            // System.out.print( "Player " + o.getPlayer().getName() +  ": [M " + o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]: ");
            // System.out.println("Player " + o.getPlayer().getName()+ " moves " +o.getUnits() + " from "+ o.getSrc().getName() + " to "+ o.getDest().getName());
            for(Level l: order.units.keySet()){
                order.dest.addUnits(order.src.removeUnits(l, order.units.get(l)));
            }
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }
    }


    @Override
    public String visit(AttackOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            Resource food = order.accept(costVisitor);
            order.issuer.getFood().comsumeResoure(food.getAmount());
            pushCombat(order);
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }
    }

    @Override
    public String visit(ResearchOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            if(order.issuer.getCurrentMaxLevel().label == 6){
                throw new IllegalArgumentException("LevelChecker error: Already the highest level.");
            }
            Resource tech = order.accept(costVisitor);
            order.issuer.getTech().comsumeResoure(tech.getAmount());
            // System.out.print( "Player " + o.getPlayer().getName() +  ": [M " + o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]: ");
            // System.out.println("Player " + o.getPlayer().getName()+ " moves " +o.getUnits() + " from "+ o.getSrc().getName() + " to "+ o.getDest().getName());
            order.issuer.upgradeMaxLevel();
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }

        
    }

    @Override
    public String visit(UpgradeOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            Resource tech = order.accept(costVisitor);
            order.issuer.getTech().comsumeResoure(tech.getAmount());
            // System.out.print( "Player " + o.getPlayer().getName() +  ": [M " + o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]: ");
            // System.out.println("Player " + o.getPlayer().getName()+ " moves " +o.getUnits() + " from "+ o.getSrc().getName() + " to "+ o.getDest().getName());
            order.target.upgradeUnits(order.from, order.to, order.units);
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }
        
    }

}