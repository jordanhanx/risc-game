package edu.duke.ece651.team7.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import edu.duke.ece651.team7.shared.Dice;
import edu.duke.ece651.team7.shared.Territory;

public class OrderExecuter {
    // private OrderRuleChecker checker;
    private Map<Territory, ArrayList<CombatOrder> > attackOrderPool;

    /**
     * 
     * @param terris territories in the gamemap
     */
    public OrderExecuter(Collection<Territory> terris){
        attackOrderPool = new HashMap<Territory, ArrayList<CombatOrder> >();
        for(Territory t: terris){
            attackOrderPool.put(t, new ArrayList<CombatOrder>(Arrays.asList(new CombatOrder(t.getOwner(),t,t.getUnits()))));
        }
    }

    public ArrayList<CombatOrder> getCombatsAt(Territory t){
        return attackOrderPool.get(t);
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
     * when a player issues an attack order, let the unit depart form the territory, but not arrived
     * @param o
     * @return null if success, error message if not
     */
    public String pushCombat(AttackOrder o){
        //need rule checker
        o.getSrc().decreaseUnits(o.getUnits());
        int flag = 0;
        for(Order order: attackOrderPool.get(o.getDest())){
            if (order.getPlayer().equals(o.getPlayer())){
                order.increaseUnits(o.getUnits());
                flag = 1;
            }
        }
        if(flag == 0){
            attackOrderPool.get(o.getDest()).add(new CombatOrder(o));
        }
        return null;
    }

    /**
     * Execute one unit combat
     * @return true if attacker success, false if defender success
     */
    public boolean doOneUnitCombat(CombatOrder defenser, CombatOrder attacker){
        Dice attack = new Dice(20);
        Dice defense = new Dice(20);
        if(attack.throwDice()> defense.throwDice()){
            defenser.decreaseUnits(1);
            return true;
        }else{
            attacker.decreaseUnits(1);
            return false;
        }
    }

    /**
     * resolve all combats saved in attackOrderPool and update Territory owner
     */
    public void doAllCombats(){
        //for each battle field
        for(Territory t: attackOrderPool.keySet()){
            ArrayList<CombatOrder> combats = attackOrderPool.get(t);
            //not one attacks this territory
            if(combats.size() == 1){
                continue;
            }
            int i = 0;
            // System.out.println("Territory is: " + t.getName());
            while (true){
                // assert(i < combats.size());
                int defenser = i;
                if(i == combats.size()-1){
                    i = -1;
                }
                int attacker = i+1;

                doOneUnitCombat(combats.get(defenser), combats.get(attacker));
                //for testing
                // System.out.println("Combat result: " + combats.get(defenser).getPlayer().getName() + ": "
                // + combats.get(defenser).getUnits() + ", " + combats.get(attacker).getPlayer().getName() + ": "
                // + combats.get(attacker).getUnits());

                if(combats.get(defenser).getUnits() == 0){
                    combats.remove(defenser);
                    if(i!=-1){
                        i--;
                    }
                }else if(combats.get(attacker).getUnits() == 0){
                    combats.remove(attacker);
                }
                //one winner
                if(combats.size() == 1){
                    t.setOwner(combats.get(0).getPlayer());
                    //for testing
                    // System.out.println("Winner is: " + combats.get(0).getPlayer().getName());
                    break;
                }
                i++;
            }
        }

    }
}
