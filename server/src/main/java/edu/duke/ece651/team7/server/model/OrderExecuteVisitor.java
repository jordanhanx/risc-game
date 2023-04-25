package edu.duke.ece651.team7.server.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.logging.LogLevel;

import edu.duke.ece651.team7.shared.FoodResource;
import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Resource;
import edu.duke.ece651.team7.shared.TechResource;
import edu.duke.ece651.team7.shared.Territory;
import edu.duke.ece651.team7.shared.Unit;

public class OrderExecuteVisitor implements OrderVisitor<String>{
    /**
     * @param checker the rule checker of order, checker whether the order is valid
     * @param map the gamemap
     * @param combatPool stores the requested combats
     * @param researchPool stores the requested ReseachOrder's issuers
     * @param costVisitor Used to calculate resource cost of different order types
     * 
     */
    private OrderRuleChecker checker;
    private GameMap map;
    private List<Combat> combatPool;
    private List<Player> researchPool;
    private Map<Player, Player> alliancePool;
    private OrderCostVisitor costVisitor;
    // private final PrintStream out;

    /**
     * 
     * @param map the GameMap of the game
     */
    public OrderExecuteVisitor(GameMap map){
        this.map = map;
        this.combatPool = new ArrayList<Combat>();
        this.researchPool = new ArrayList<Player>();
        this.alliancePool = new HashMap<>();
        this.costVisitor = new OrderCostVisitor(map);
        // this.out = out;
        checker = new CostChecker(null, costVisitor);
        checker = new UnitNumberChecker(checker);
        checker = new PathChecker(checker);
        checker = new LevelChecker(checker);
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
            departUnits.addAll(o.src.removeUnits(l, o.units.get(l), o.issuer));
        }
        //equip Ultron units with bomb
        if(o.numBomb > 0){
            equipUnits(departUnits, o.numBomb);
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

    protected void equipUnits(ArrayList<Unit> units, int numBomb){
        for(Unit u : units){
            if (u.getLevel() == Level.ULTRON && numBomb > 0){
                u.equipBomb();
                numBomb--;
                u.getOwner().modifyBombAmount(-1);
            }
        }
    }

    protected void breakAlliance(Player pAttack, Player pRetreat){
        retreat(pAttack, pRetreat);

        for(Territory t:pRetreat.getTerritories()){
            if(t.getUnitsNumber(pAttack) > 0){
                Map<Level, Integer> units = new HashMap<>();
                for(Unit u : t.getUnits(pAttack)){
                    units.put(u.getLevel(), units.getOrDefault(u.getLevel(), 0)+1);
                }
                pushCombat(new AttackOrder(pAttack, false,0, t, t, units));
            }
        }
        pAttack.breakAllianceWith(pRetreat);
    }

    protected void retreat(Player pAttack, Player pRetreat){
        for(Territory t: pAttack.getTerritories()){
            if(t.getUnitsNumber(pRetreat) > 0){
                Territory nearest = map.getNearestAllianceTerritory(t);
                nearest.addUnits(t.removeAllUnitsOfPlayer(pRetreat));
            }
        }
    }

    /**
     * check the validity of the move order then execute it
     * 
     * @param order the MoveOrder to be executed
     * @return null if success
     * @throws IllegalArgumentException if the order is not valid
     */
    @Override
    public String visit(MoveOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            Resource food = order.accept(costVisitor);
            order.issuer.getFood().consumeResource((FoodResource) food);
            if(order.useAircraft){
                order.issuer.consumeAircraft(1);
            }
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


    /**
     * check the validity of the AttackOrder, if it is valid, push it to the combat pool
     * 
     * @param order the AttackOrder to be executed
     * @return null if success
     * @throws IllegalArgumentException if the order is not valid
     */
    @Override
    public String visit(AttackOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            Resource food = order.accept(costVisitor);
            order.issuer.getFood().consumeResource((FoodResource) food);
            if(order.useAircraft){
                order.issuer.consumeAircraft(1);
            }
            pushCombat(order);
            //check if attack alliance's territory
            if(order.issuer.isAlliance(order.dest.getOwner())){
                breakAlliance(order.issuer, order.dest.getOwner());
            }
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * check the validity of the ResearchOrder, if it is valid, push it to the Research pool
     * 
     * @param order the ResearchOrder to be executed
     * @return null if success
     * @throws IllegalArgumentException if the order is not valid
     */
    @Override
    public String visit(ResearchOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            if(researchPool.contains(order.issuer)){
                throw new IllegalArgumentException("You can only issue one Research Order per turn");
            }
            Resource tech = order.accept(costVisitor);
            order.issuer.getTech().consumeResource((TechResource) tech);
            researchPool.add(order.issuer);
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }

        
    }

    /**
     * check the validity of the UpgradeOrder then execute it
     * 
     * @param order the UpgradeOrder to be executed
     * @return null if success
     * @throws IllegalArgumentException if the order is not valid
     */
    @Override
    public String visit(UpgradeOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            Resource tech = order.accept(costVisitor);
            order.issuer.getTech().consumeResource(tech.getAmount());
            order.target.upgradeUnits(order.from, order.to, order.units);
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }
        
    }
    @Override
    public String visit(AllianceOrder order) {
        //needs to be more than 3 player.
        if(order.alliance.equals(order.issuer)){
            throw new IllegalArgumentException("You cannot align with yourself.");
        }
        if(alliancePool.containsKey(order.issuer)){
            throw new IllegalArgumentException("You can only issue one AllianceOrder per turn");
        }
        alliancePool.put(order.issuer, order.alliance);
        return null;
    }

    @Override
    public String visit(ManufactureOrder order) {
        String err = checker.checkOrderValidity(map, order);
        if(err == null){
            Resource tech = order.accept(costVisitor);
            order.issuer.getTech().consumeResource(tech.getAmount());
            if(order.bomb){
                order.issuer.modifyBombAmount(order.amount);
            }else{
                order.issuer.addAircraft(order.amount);
            }
            return null;
        }else{
            throw new IllegalArgumentException(err);
        }
        
    }
    /**
     * resolve all combats saved in combatPool
     */
    protected void doAllCombats(){
        for(Combat c : combatPool){
            c.resolveCombat();
        }
        combatPool.clear();
    }

    /**
     * resolve all research order saved in researchPool
     */
    protected void doAllResearch(){
        for(Player p: researchPool){
            p.upgradeMaxLevel();
        }
        researchPool.clear();
    }

    protected void resolveAllAlliance(){
        for(Player p: alliancePool.keySet()){
            if(alliancePool.get(alliancePool.get(p)).equals(p)){
                p.addAlliance(alliancePool.get(p));
            }
        }
        alliancePool.clear();
    }
    /**
     * collect all player's resource
     */
    protected void collectAllResource(){
        ArrayList<Player> players = new ArrayList<>();
        for(Territory t: map.getTerritories()){
            Player p = t.getOwner();
            if(!players.contains(p)){
                p.collectResource();
                players.add(p);
            }else{
                continue;
            }
        }
    }
    /**
     * when all users committed, finish current Round
     * 1. resolving all the combats results
     * 2. finish the research order by upgrading user level in the research pool
     * 3. add onw basic unit to each territory
     */
    public void resolveOneRound(){
        doAllCombats();
        doAllResearch();
        resolveAllAlliance();
        for(Territory t: map.getTerritories()){
            t.addUnits(new Unit(t.getOwner()));
        }
        collectAllResource();
    }
    
   
}