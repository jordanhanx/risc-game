package edu.duke.ece651.team7.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team7.shared.Dice;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public class OrderExecuter {
    /**
     * @param CombatOrderPool stores the requested combats
     */
    // private OrderRuleChecker checker;
    private Map<Territory, ArrayList<CombatOrder> > combatOrderPool;

    /**
     * 
     * @param terris territories in the gamemap
     */
    public OrderExecuter(Collection<Territory> terris){
        combatOrderPool = new HashMap<Territory, ArrayList<CombatOrder> >();
        for(Territory t: terris){
            combatOrderPool.put(t, new ArrayList<CombatOrder>(Arrays.asList(new CombatOrder(t.getOwner(),t,t.getUnits()))));
        }
        // checker = new PathChecker(null);
        // checker = new UnitNumberChecker(checker);
        // checker = new IssuerChecker(checker);
        // this.checker = checker;
    }

    public ArrayList<CombatOrder> getCombatsAt(Territory t){
        return combatOrderPool.get(t);
    }

    /**
     * Execute one move order, move the unit from one territory to another
     * @param o order to execute
     * @return null if success, error message if not
     */
    public String doOneMove(MoveOrder o){
        //need rule checker
        o.getSrc().decreaseUnits(o.getUnits());
        o.getDest().increaseUnits(o.getUnits());
        return null;
    }

    /**
     * Check if the issued Attack order's destination is already formed a combat by the same player
     * @param o Player's order
     * @return if the combat exists, return the combat. if not return null.
     */
    public CombatOrder isInAttackPool(AttackOrder o){
        for(CombatOrder order: combatOrderPool.get(o.getDest())){
            if (order.getPlayer().equals(o.getPlayer())){
                return order;
            }
        }
        return null;
    }

    /**
     * when a player issues an attack order, let units depart form the territory,
     * but not arrive at the target
     * @param o
     * @return null if success, error message if not
     */
    public String pushCombat(AttackOrder o){
        //need rule checker
        o.getSrc().decreaseUnits(o.getUnits());
        CombatOrder targetCombat = isInAttackPool(o);
        if(targetCombat != null){
            targetCombat.increaseUnits(o.getUnits());
            System.out.println("Combine combat Force: " + o.getDest().getName());
        }else{
            System.out.println("Add new Combat: " + o.getDest().getName());
            combatOrderPool.get(o.getDest()).add(new CombatOrder(o));
        }
        return null;
    }

    /**
     * Execute one unit combat between two Combat Order
     * @param defender 
     * @param attacker
     * @return true if attacker succeeds, false if defender succeeds
     */
    public boolean doOneUnitCombat(CombatOrder defender, CombatOrder attacker){
        Dice attackD = new Dice(20);
        Dice defenseD = new Dice(20);
        if(attackD.throwDice()> defenseD.throwDice()){
            defender.decreaseUnits(1);
            return true;
        }else{
            attacker.decreaseUnits(1);
            return false;
        }
    }   

    /**
     *  resolve the combat result, return the next defender's index
     * @param defender index of the current defender Order in the combat list
     * @param attacker index of the current attacker Order in the combat list
     * @param combats the list of all combats
     * @return next defender's index in the combat list
     */
    public int updateCombatList(int defender, int attacker, ArrayList<CombatOrder> combats){
        //for testing
        // System.out.println("Combat result: " + combats.get(defender).getPlayer().getName() + ": "
        // + combats.get(defender).getUnits() + ", " + combats.get(attacker).getPlayer().getName() + ": "
        // + combats.get(attacker).getUnits());
        
        //if the CombatOrder lose, remove it from the combat list
        if(combats.get(defender).getUnits() == 0){
            combats.remove(defender);
            if(defender >= combats.size()){
                return 0; 
            }else{
                return defender;
            }
        }else if(combats.get(attacker).getUnits() == 0){
            combats.remove(attacker);
            if(attacker >= combats.size()){
                return 0; 
            }
        }
        return attacker;
    }

    /**
     * resolve all combats saved in combatOrderPool and update Territory owner
     */
    public void doAllCombats(){
        //for each battle field
        System.out.println("Resolve Combat Result");
        for(Territory t: combatOrderPool.keySet()){
            Player originOwner = t.getOwner();
            ArrayList<CombatOrder> combats = combatOrderPool.get(t);
            //no one attacks this territory
            if(combats.size() == 1){
                // System.out.print(t.getName() + " ");
                continue;
            }
            int defender = 0;
            System.out.println("Territory is: " + t.getName());
            while (true){
                //wrap around
                int attacker = defender+1;
                if(defender == combats.size()-1){
                    attacker = 0;
                }
                doOneUnitCombat(combats.get(defender), combats.get(attacker));
                defender = updateCombatList(defender, attacker, combats);
                if(combats.size() == 1){ //combats end
                    t.setOwner(combats.get(0).getPlayer());
                    originOwner.removeTerritory(t);
                    combats.get(0).getPlayer().addTerritory(t);
                    //for testing
                    System.out.println("Winner of Combat in " +t.getName() + " is: " + combats.get(0).getPlayer().getName());
                    break;
                }
            }
        }

    }
}
