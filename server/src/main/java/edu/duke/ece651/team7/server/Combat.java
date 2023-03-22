package edu.duke.ece651.team7.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team7.shared.Dice;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;


public class Combat {
    /**
     * @param battleField the Territory this combat is located at
     * @param attackPool the map of participants and their units;
     * @param participants players that participate in the combat
     */
    private Territory battleField;
    private Map<Player, Integer> attackPool;
    private List<Player> participants;
    // private final PrintStream out;

    public Combat(Territory t){
        this.battleField = t;
        this.attackPool = new HashMap<Player, Integer>();
        this.participants = new ArrayList<Player>();
    }

    /**
     * get the territory where the combat is happening
     * @return
     */
    public Territory getBattlefield(){
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
            return attackPool.get(p);
        }else{
            return -1;
        }
    }

    /**
     * 
     * @return number of participants in the combat
     */
    public int getParticipantsSize(){
        return participants.size();
    }

    /**
     * 
     * @return number of attack in the attackpool
     */
    public int getAttackPoolSize(){
        return attackPool.size();
    }


    /**
     * Push an atttack into the order
     * @param p player that issues the attack
     * @param units number of units used to attack
     */
    public void pushAttack(Player p, int units){
        attackPool.put(p, attackPool.getOrDefault(p, 0) + units);
        if(!participants.contains(p)){
            participants.add(p);
        }
    }

    /**
     * whether there is a combat at this battleField
     * @return true is yes
     * @return false if not
     */
    public boolean hasCombat(){
        if (participants.size() >= 1 && attackPool.size() >= 1){
            return true;
        }
        return false;
    }

    /**
     * whether the combat is end, it is end if there remains only one player in 
     * the participant list 
     * @return true yes
     * @return false no
     */
    public boolean combatEnd(){
        if (participants.size() == 1 && attackPool.size() ==1){
            return true;
        }
        return false;
    }


    /**
     * Execute one unit combat between two Player
     * @param defender Player as defender
     * @param attacker Player as attacker
     * @return true if attacker succeeds, 
     * @return false if defender succeeds
     */
    public boolean doOneUnitCombat(Player defender, Player attacker){
        //the original owner of the territory should always be defender
        if(attacker == battleField.getOwner()){
            attacker = defender;
            defender = battleField.getOwner();
        }
        System.out.println("Attacker: " + attacker.getName() + "(" + getAttackUnitofPlayer(attacker) + ") "
         +"Defender: " + defender.getName() + "(" + getAttackUnitofPlayer(defender) + ") ");
        //if the player does not have any units combating, return 
        if(attackPool.get(defender) == 0){
            return true;
        }
        if(attackPool.get(attacker) == 0){
            return false;
        }
        Dice attackD = new Dice(20);
        Dice defenseD = new Dice(20);
        if(attackD.throwDice()> defenseD.throwDice()){
            attackPool.put(defender, attackPool.get(defender) - 1);
            return true;
        }else{
            attackPool.put(attacker, attackPool.get(attacker) - 1);
            return false;
        }
    }   

    /**
     * resolve the combat result, return the next defender's index
     * @param defender index of the current defense Player in the participant list 
     * @param attacker index of the current attack Player in the participant list 
     * @return next defender's index in the participant list 
     */
    public int updateParticipantList(int defender, int attacker){
        //if the CombatOrder lose, remove it from the combat list
        Player defendPlayer = participants.get(defender);
        Player attackPlayer = participants.get(attacker);
        if(attackPool.get(defendPlayer) == 0){
            participants.remove(defendPlayer);
            attackPool.remove(defendPlayer);
            if(defender >= participants.size()){
                return 0; 
            }else{
                return defender;
            }
        }else if(attackPool.get(attackPlayer) == 0){
            participants.remove(attacker);
            attackPool.remove(attackPlayer);
            if(attacker >= participants.size()){
                return 0; 
            }
        }
        return attacker;
    }


    public void printCombat(){
        System.out.print(battleField.getName() + ": ");
        for(Player p: participants){
            System.out.print( "(" + p.getName() + ": " + attackPool.get(p) + "), ");
        }
        System.out.println();
    }
    /**
     * resolve combats, do one unit attack recurcively, 0/1, 1/2, 2/3....5/0
     * until only one player left
     * @return null if does not have combat
     * @return player that wins in the combat
     */
    public Player resolveCombat(){
        System.out.println("\nResolving Combat...");
        if (!hasCombat()){
            return null;
        }
         //owner of the territory participate in the combat
        pushAttack(battleField.getOwner(), battleField.getUnits());
        battleField.setUnits(0);;
        printCombat();
        Player originOwner = battleField.getOwner();
        int defender = 0;
        while (true){
            //wrap around
            int attacker = defender + 1;
            if(defender == participants.size()-1){
                attacker = 0;
            }
            doOneUnitCombat(participants.get(defender), participants.get(attacker));
            defender = updateParticipantList(defender, attacker);
            if(combatEnd()){ //combats end
                // System.out.println("Winner is " + participants.get(0) + " with " + attackPool.get(participants.get(0)) + " units");
    
                originOwner.removeTerritory(battleField);
                battleField.setOwner(participants.get(0));
                participants.get(0).addTerritory(battleField);
                battleField.setUnits(attackPool.get(participants.get(0)));
                System.out.println("Winner of Combat in " +battleField.getName() + " (" + battleField.getUnits()+") is: " + participants.get(0).getName());

                //for testing
    
                return participants.get(0);
            }
        }
    }
}
