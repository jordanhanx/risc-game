package edu.duke.ece651.team7.server.model;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Territory;

public class OrderExecuter {
    /**
     * @param checker    the rule checker of order, checker whether the order is
     *                   valid
     * @param map        the gamemap
     * @param combatPool stores the requested combats
     * 
     */
    private OrderRuleChecker checker;
    private GameMap map;
    private List<Combat> combatPool;
    // private final PrintStream out;

    /**
     * 
     * @param map the GameMap of the game
     */
    public OrderExecuter(GameMap map) {
        this.map = map;
        this.combatPool = new ArrayList<Combat>();
        // this.out = out;
        checker = new PathChecker(null);
        checker = new UnitNumberChecker(checker);
    }

    /**
     * Execute one move order, move the unit from one territory to another
     * 
     * @param o order to execute
     * @throws IllegalArgumentException if the order is not valid
     */
    public void doOneMove(MoveOrder o) throws IllegalArgumentException {
        String err = checker.checkOrderValidity(map, o);
        if (err == null) {
            // System.out.print( "Player " + o.getPlayer().getName() + ": [M " +
            // o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]:
            // ");
            // System.out.println("Player " + o.getPlayer().getName()+ " moves "
            // +o.getUnits() + " from "+ o.getSrc().getName() + " to "+
            // o.getDest().getName());
            o.getSrc().decreaseUnits(o.getUnits());
            o.getDest().increaseUnits(o.getUnits());
        } else {
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * Check if the issued Attack order's destination has already formed a combat
     * 
     * @param o Player's order
     * @return if the combat exists, return the combat. if not return null.
     */
    public Combat isInCombatPool(Territory t) {
        for (Combat c : combatPool) {
            if (c.getBattlefield().equals(t)) {
                return c;
            }
        }
        return null;
    }

    /**
     * when a player issues an attack order, let units depart form the territory,
     * but not arrive at the target territory
     * 
     * @param o AttackOrder issued by a player
     * @throws IllegalArgumentException if the order is not valid
     */
    public void pushCombat(AttackOrder o) throws IllegalArgumentException {
        // need rule checker
        String err = checker.checkOrderValidity(map, o);
        if (err == null) {
            o.getSrc().decreaseUnits(o.getUnits());
            Combat targetCombat = isInCombatPool(o.getDest());
            if (targetCombat != null) {
                targetCombat.pushAttack(o.getPlayer(), o.getUnits());
                // System.out.print("Player " + o.getPlayer().getName() + ": [A " +
                // o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]:
                // ");
                // System.out.println("Player " + o.getPlayer().getName()+ " joins Combat at " +
                // o.getDest().getName() + " from " + o.getSrc().getName() +" with " +
                // o.getUnits() + " units");

            } else {
                targetCombat = new Combat(o.getDest());
                targetCombat.pushAttack(o.getPlayer(), o.getUnits());
                combatPool.add(targetCombat);
                // System.out.print( "Player " + o.getPlayer().getName() + ": [A " +
                // o.getSrc().getName() + " " + o.getDest().getName() + " "+o.getUnits() +"]:
                // ");
                // System.out.println("Player " + o.getPlayer().getName()+ " adds new Combat at
                // " + o.getDest().getName() + " from " + o.getSrc().getName()+ " with " +
                // o.getUnits() + " units");
            }
        } else {
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * resolve all combats saved in combatPool and add one unit to each territory
     */
    public void doAllCombats() {
        for (Combat c : combatPool) {
            c.resolveCombat();
        }
        for (Territory t : map.getTerritories()) {
            t.increaseUnits(1);
        }
        combatPool.clear();
    }

}