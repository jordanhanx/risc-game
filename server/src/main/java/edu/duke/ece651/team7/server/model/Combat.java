package edu.duke.ece651.team7.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team7.shared.Dice;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;
import edu.duke.ece651.team7.shared.Unit;
import java.util.Collections;


public class Combat {
    /**
     * @param battleField  the Territory this combat is located at
     * @param attackPool   the map of participants and their units;
     * @param participants players that participate in the combat
     */
    private Territory battleField;
    // private Map<Player, Integer> attackPool;
    private Map<Player, ArrayList<Unit> > attackPool;
    private List<Player> participants;
    
    // private final PrintStream out;


    public Combat(Territory t){
        this.battleField = t;
        this.attackPool = new LinkedHashMap<Player, ArrayList<Unit> >();
        this.participants = new ArrayList<Player>();
    }

    /**
     * get the territory where the combat is happening
     * @return
     */
    public Territory getBattlefield() {
        return battleField;
    }

    /**
     * 
     * @param p one participant of the combat
     * @return number of participant the player issues
     * @return -1 if the player is not in the attackpool
     */
    public int getAttackUnitofPlayer(Player p){
        if(participants.contains(p)){
            return attackPool.get(p).size();
        }else{
            return -1;
        }
    }

    /**
     * 
     * @return number of participants in the combat
     */
    public int getParticipantsSize() {
        return participants.size();
    }

    /**
     * 
     * @return number of attack in the attackpool
     */
    public int getAttackPoolSize() {
        return attackPool.size();
    }

    public ArrayList<Unit> getAttackUnitsbyPlayer(Player p ){
        return attackPool.get(p);
    }


    // /**
    //  * Push an atttack into the order
    //  * @param p player that issues the attack
    //  * @param units number of units used to attack
    //  */
    // public void pushAttack(Player p, int units){
    //     attackPool.put(p, attackPool.getOrDefault(p, 0) + units);
    //     if(!participants.contains(p)){
    //         participants.add(p);
    //     }
    // }

    /**
     * Push an atttack into the order
     * @param p     player that issues the attack
     * @param units number of units used to attack
     */
    public void pushAttack(Player p, Collection<Unit> units){
        if(!participants.contains(p)){
            participants.add(p);
            attackPool.put(p, new ArrayList<Unit>(units));
        }else{
            attackPool.get(p).addAll(units);
        }
        Collections.sort(attackPool.get(p));
    }

    /**
     * whether there is a combat at this battleField
     * 
     * @return true is yes
     * @return false if not
     */
    public boolean hasCombat() {
        if (participants.size() >= 1 && attackPool.size() >= 1) {
            return true;
        }
        return false;
    }

    /**
     * whether the combat is end, it is end if there remains only one player in
     * the participant list
     * 
     * @return true yes
     * @return false no
     */
    public boolean combatEnd() {
        if (participants.size() == 1 && attackPool.size() == 1) {
            return true;
        }
        return false;
    }

    /**
     * Execute one unit combat between two Player
     * 
     * @param defender Player as defender
     * @param attacker Player as attacker
     * @return true if attacker succeeds,
     * @return false if defender succeeds
     */
    protected boolean doOneUnitCombat(Player attacker,Unit attU, Player defender, Unit defU){
        //if the player does not have any units combating, return 
        if(attackPool.get(attacker).size() == 0 ){
            return false;
        }
        if(attackPool.get(defender).size() == 0 ){
            return true;
        }

        Dice attackD = new Dice(20);
        Dice defenseD = new Dice(20);
        if(attackD.throwDicewithBonus(attU.getLevel()) > defenseD.throwDicewithBonus(defU.getLevel())){
            attackPool.get(defender).remove(defU);
            return true;
        }else{
            attackPool.get(attacker).remove(attU);
            return false;
        }
    }
    /**
     * the highest-bonus attacker unit paired with the lowest-bonus defender unit
     * the lowest-bonus attacker unit paired with the highest-bonus defender unit.
     * 
     * @param attacker player that act as attacker
     * @param defender Player as defender
     * @return 0 if attacker wins both units contest;
     *         1 if attacker wins first and lose the second
     *         2 if attacker loses first and wins the second
     *         3 if attacker loses first and loses the second
     */
    protected int doOneTurnCombat(Player attacker, Player defender){
         //the original owner of the territory should always be defender
         if(attacker == battleField.getOwner()){
            attacker = defender;
            defender = battleField.getOwner();
        }
        // System.out.println("Attacker: " + attacker.getName() + "(" + getAttackUnitofPlayer(attacker) + ") "
        //  +"Defender: " + defender.getName() + "(" + getAttackUnitofPlayer(defender) + ") ");
        //the return type is for test purpose
        if(attackPool.get(attacker).size() <= 0 ){
            return 4;
        }
        if(attackPool.get(defender).size() <= 0 ){
            return 5;
        }
        int attackUnitSize = attackPool.get(attacker).size();
        boolean r1 = doOneUnitCombat(attacker, attackPool.get(attacker).get(attackUnitSize-1), defender, attackPool.get(defender).get(0));

        int defendUnitSize = attackPool.get(defender).size();
        boolean r2;
        if(defendUnitSize > 0 && attackPool.get(attacker).size() > 0){
            r2 = doOneUnitCombat(attacker, attackPool.get(attacker).get(0), defender, attackPool.get(defender).get(defendUnitSize-1));
        }else{
            if (attackPool.get(attacker).size() > 0){
                r2 = true;
            }else{
                r2 = false;
            }
        }
        if(r1 && r2){
            return 0;
        }else if(r1 && !r2){
            return 1;
        }else if(!r1 && r2){
            return 2;
        }else{
            return 3;
        }
    }

    /**
     * resolve the combat result, return the next defender's index
     * 
     * @param defender index of the current defense Player in the participant list
     * @param attacker index of the current attack Player in the participant list
     * @return next defender's index in the participant list
     */
    public int updateParticipantList(int defender, int attacker) {
        // if the CombatOrder lose, remove it from the combat list
        Player defendPlayer = participants.get(defender);
        Player attackPlayer = participants.get(attacker);
        if(attackPool.get(defendPlayer).size() == 0){
            participants.remove(defendPlayer);
            attackPool.remove(defendPlayer);
            if (defender >= participants.size()) {
                return 0;
            } else {
                return defender;
            }
        }else if(attackPool.get(attackPlayer).size() == 0){
            participants.remove(attacker);
            attackPool.remove(attackPlayer);
            if (attacker >= participants.size()) {
                return 0;
            }
        }
        return attacker;
    }

    // public void printCombat(){
    // System.out.print(battleField.getName() + ": ");
    // for(Player p: participants){
    // System.out.print( "(" + p.getName() + ": " + attackPool.get(p) + "), ");
    // }
    // System.out.println();
    // }
    /**
     * resolve combats, do one unit attack recurcively, 0/1, 1/2, 2/3....5/0
     * until only one player left
     * 
     * @return null if does not have combat
     * @return player that wins in the combat
     */
    public Player resolveCombat() {
        // System.out.println("\nResolving Combat...");
        if (!hasCombat()) {
            return null;
        }
         //owner of the territory participate in the combat

        pushAttack(battleField.getOwner(), battleField.removeAllUnits());
        // printCombat();
        Player originOwner = battleField.getOwner();
        int defender = 0;
        while (true) {
            // wrap around
            int attacker = defender + 1;
            if (defender == participants.size() - 1) {
                attacker = 0;
            }
            doOneTurnCombat(participants.get(defender), participants.get(attacker));
            defender = updateParticipantList(defender, attacker);

            if(combatEnd()){ //combats end
                // System.out.println("Winner is " + participants.get(0) + " with " + attackPool.get(participants.get(0)) + " units");
    
                originOwner.removeTerritory(battleField);
                battleField.setOwner(participants.get(0));
                participants.get(0).addTerritory(battleField);
                battleField.addUnits(attackPool.get(participants.get(0)));
                System.out.println("Winner of Combat in " +battleField.getName() + " (" + battleField.getUnits()+") is: " + participants.get(0).getName());

                //for testing
                return participants.get(0);
            }
        }
    }
}
