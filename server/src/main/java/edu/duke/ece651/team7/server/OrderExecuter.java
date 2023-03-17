package edu.duke.ece651.team7.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public class OrderExecuter {
    /**
     * @param CombatOrderPool stores the requested combats
     */
    // private OrderRuleChecker checker;
    private GameMap map;
    private List<Combat> combatPool;

    /**
     * 
     * @param map the gameMap
     */
    public OrderExecuter(GameMap map){
        this.map = map;
        this.combatPool = new ArrayList<Combat>();
    }

    public int getCombatsPoolSize(){
        return combatPool.size();
    }
    /**
     * Execute one move order, move the unit from one territory to another
     * @param o order to execute
     * @return null if success, error message if not
     * @throws IllegalArgumentException if the order is not valid
     */
    public void doOneMove(MoveOrder o) throws IllegalArgumentException{
        //need rule checker
        o.getSrc().decreaseUnits(o.getUnits());
        o.getDest().increaseUnits(o.getUnits());
    }

    /**
     * Check if the issued Attack order's destination is already formed a combat by the same player
     * @param o Player's order
     * @return if the combat exists, return the combat. if not return null.
     */
    public Combat isInCombatPool(Territory t){
        for (Combat c : combatPool){
            if(c.getBattlefield().equals(t)){
                return c;
            }
        }
        return null;
    }

    /**
     * when a player issues an attack order, let units depart form the territory,
     * but not arrive at the target
     * @param o
     * @throws IllegalArgumentException if the order is not valid
     */
    public void pushCombat(AttackOrder o) throws IllegalArgumentException{
        //need rule checker
        o.getSrc().decreaseUnits(o.getUnits());
        Combat targetCombat = isInCombatPool(o.getDest());
        if(targetCombat != null){
            targetCombat.pushAttack(o.getPlayer(), o.getUnits());
            System.out.println("Combine combat Force: " + o.getDest().getName());
        }else{
            targetCombat = new Combat(o.getDest());
            targetCombat.pushAttack(o.getPlayer(), o.getUnits());
            combatPool.add(targetCombat);
            System.out.println("Add new Combat: " + o.getDest().getName());
        }
    }

    /**
     * resolve all combats saved in combatOrderPool and update Territory owner
     */
    public void doAllCombats(){
        for(Combat c : combatPool){
            c.resolveCombat();
        }
        for(Territory t: map.getTerritories()){
            t.increaseUnits();
        }
        combatPool.clear();
    }

}