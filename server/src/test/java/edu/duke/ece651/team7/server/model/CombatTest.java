package edu.duke.ece651.team7.server.model;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.server.model.Combat;
import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;
import edu.duke.ece651.team7.shared.Unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class CombatTest {
    @Test
    public void test_constructor(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player groupC = mock(Player.class);
        GameMap mockMap = mock(GameMap.class);
        Territory tHogwarts = mock(Territory.class);
        Territory tMordor = mock(Territory.class);
        Territory tNarnia = mock(Territory.class);
        Territory tMidkemia = mock(Territory.class);
        // Setup mockGameMap
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");
        when(tNarnia.getName()).thenReturn("Narnia");
        when(tMidkemia.getName()).thenReturn("Midkemia");

        when(tMordor.getOwner()).thenReturn(groupA);
        when(tHogwarts.getOwner()).thenReturn(groupA);
        when(tNarnia.getOwner()).thenReturn(groupB);
        when(tMidkemia.getOwner()).thenReturn(groupB);

        when(tMordor.getUnitsNumber()).thenReturn(5);
        when(tHogwarts.getUnitsNumber()).thenReturn(5);
        when(tNarnia.getUnitsNumber()).thenReturn(5);
        when(tMidkemia.getUnitsNumber()).thenReturn(5);

        Combat combat1 = new Combat(tMidkemia);
        Combat combat2 = new Combat(tNarnia);
        assertEquals(combat1.getBattlefield(), tMidkemia);
        assertEquals(combat2.getBattlefield(), tNarnia);
        // assertEquals(5, combat1.getAttackUnitofPlayer(groupB));
        assertEquals(0, combat1.getParticipantsSize());
        // assertEquals(5, combat2.getAttackUnitofPlayer(groupB));
        assertEquals(0, combat2.getParticipantsSize());
    }

    @Test
    public void test_pushAttack(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player groupC = mock(Player.class);
        // GameMap mockMap = mock(GameMap.class);

        Territory tNarnia = mock(Territory.class);
        Territory tElantris = mock(Territory.class);
        Territory tMidkemia = mock(Territory.class);
        Territory tScadrial = mock(Territory.class);
        Territory tRoshar = mock(Territory.class);
        Territory tOz = mock(Territory.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        Territory tGondor = mock(Territory.class);
        
        // Setup mockGameMap
        // when(tNarnia.getName()).thenReturn("Narnia");
        // when(tElantris.getName()).thenReturn("Elantris");
        // when(tMidkemia.getName()).thenReturn("Midkemia");

        // when(tScadrial.getName()).thenReturn("Scadrial");

        // when(tHogwarts.getName()).thenReturn("Hogwarts");
        // when(tNarnia.getName()).thenReturn("Narnia");
        // when(tMidkemia.getName()).thenReturn("Midkemia");
        // when(tMidkemia.getName()).thenReturn("Midkemia");

        when(tNarnia.getOwner()).thenReturn(groupA);
        when(tElantris.getOwner()).thenReturn(groupB);
        when(tMidkemia.getOwner()).thenReturn(groupA);
        when(tScadrial.getOwner()).thenReturn(groupB);
        when(tRoshar .getOwner()).thenReturn(groupB);
        when(tOz.getOwner()).thenReturn(groupA);
        when(tMordor.getOwner()).thenReturn(groupC);
        when(tHogwarts.getOwner()).thenReturn(groupC);
        when(tGondor.getOwner()).thenReturn(groupC);


        // when(tNarnia.getUnits()).thenReturn(5);
        // when(tElantris.getUnits()).thenReturn(5);
        // when(tMidkemia.getUnits()).thenReturn(5);
        // when(tScadrial.getUnits()).thenReturn(5);
        // when(tRoshar.getUnits()).thenReturn(5);
        // when(tOz.getUnits()).thenReturn(5);
        // when(tMordor.getUnits()).thenReturn(5);
        // when(tHogwarts.getUnits()).thenReturn(5);
        // when(tGondor.getUnits()).thenReturn(5);
        
        Combat combat1 = new Combat(tScadrial);

        ArrayList<Unit> unitstoAdd = new ArrayList<>(Arrays.asList(new Unit(), new Unit(Level.CAVALRY), new Unit(Level.ULTRON), new Unit(Level.INFANTRY), new Unit(Level.TROOPER)));
        combat1.pushAttack(groupA, unitstoAdd);
        assertEquals(5, combat1.getAttackUnitofPlayer(groupA));
        assertEquals(1, combat1.getParticipantsSize());

        ArrayList<Unit> unitsA = combat1.getAttackUnitsbyPlayer(groupA);
        assertEquals(unitsA.get(0), unitstoAdd.get(0));
        assertEquals(unitsA.get(1), unitstoAdd.get(3));
        assertEquals(unitsA.get(unitsA.size()-1), unitstoAdd.get(2));

        combat1.pushAttack(groupA, new ArrayList<Unit>(Arrays.asList(new Unit(), new Unit(Level.AIRFORCE), new Unit(Level.ARTILLERY))));
    
        assertEquals(8, combat1.getAttackUnitofPlayer(groupA));
        assertEquals(1, combat1.getParticipantsSize());

        unitsA = combat1.getAttackUnitsbyPlayer(groupA);
        assertEquals(unitsA.get(2), unitstoAdd.get(3));
        assertEquals(unitsA.get(unitsA.size()-1), unitstoAdd.get(2));

        assertEquals(-1, combat1.getAttackUnitofPlayer(groupC));

    }

    @Test
    public void test_hasCombat_combatend(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player groupC = mock(Player.class);
        // GameMap mockMap = mock(GameMap.class);

        Territory tNarnia = mock(Territory.class);
        Territory tElantris = mock(Territory.class);
        Territory tMidkemia = mock(Territory.class);
        Territory tScadrial = mock(Territory.class);
        Territory tRoshar = mock(Territory.class);
        Territory tOz = mock(Territory.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);
        Territory tGondor = mock(Territory.class);
        
        // Setup mockGameMap
        // when(tNarnia.getName()).thenReturn("Narnia");
        // when(tElantris.getName()).thenReturn("Elantris");
        // when(tMidkemia.getName()).thenReturn("Midkemia");

        // when(tScadrial.getName()).thenReturn("Scadrial");

        // when(tHogwarts.getName()).thenReturn("Hogwarts");
        // when(tNarnia.getName()).thenReturn("Narnia");
        // when(tMidkemia.getName()).thenReturn("Midkemia");
        // when(tMidkemia.getName()).thenReturn("Midkemia");

        when(tNarnia.getOwner()).thenReturn(groupA);
        when(tElantris.getOwner()).thenReturn(groupB);
        when(tMidkemia.getOwner()).thenReturn(groupA);
        when(tScadrial.getOwner()).thenReturn(groupB);
        when(tRoshar .getOwner()).thenReturn(groupB);
        when(tOz.getOwner()).thenReturn(groupA);
        when(tMordor.getOwner()).thenReturn(groupC);
        when(tHogwarts.getOwner()).thenReturn(groupC);
        when(tGondor.getOwner()).thenReturn(groupC);


        // when(tNarnia.getUnits()).thenReturn(5);
        // when(tElantris.getUnits()).thenReturn(5);
        // when(tMidkemia.getUnits()).thenReturn(5);
        // when(tScadrial.getUnits()).thenReturn(5);
        // when(tRoshar.getUnits()).thenReturn(5);
        // when(tOz.getUnits()).thenReturn(5);
        // when(tMordor.getUnits()).thenReturn(5);
        // when(tHogwarts.getUnits()).thenReturn(5);
        // when(tGondor.getUnits()).thenReturn(5);
        
        Combat combat1 = new Combat(tScadrial);
        assertFalse(combat1.hasCombat());
        // assertTrue(combat1.combatEnd());

        combat1.pushAttack(groupC, new ArrayList<Unit>(Arrays.asList(new Unit(), new Unit(), new Unit(), new Unit(), new Unit())));
        assertTrue(combat1.hasCombat());
        // assertFalse(combat1.combatEnd());

    }

    @Test
    public void test_doOneTurnCombat(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player groupC = mock(Player.class);
        Territory tScadrial = mock(Territory.class);

        // CIVILIAN(0),
        // INFANTRY(1), 
        // CAVALRY(2), 
        // TROOPER(3), 
        // ARTILLERY(4), 
        // AIRFORCE(5), 
        // ULTRON(6);

        ArrayList<Unit> unitstoAdd1 = new ArrayList<>(Arrays.asList(new Unit(), new Unit(), new Unit(Level.INFANTRY), new Unit(Level.CAVALRY), new Unit(Level.TROOPER), new Unit(Level.ULTRON)));
        ArrayList<Unit> unitstoAdd2 = new ArrayList<>(Arrays.asList(new Unit(), new Unit(Level.INFANTRY), new Unit(Level.INFANTRY), new Unit(Level.TROOPER), new Unit(Level.TROOPER), new Unit(Level.AIRFORCE), new Unit(Level.AIRFORCE)));


        Combat combat = new Combat(tScadrial);
        combat.pushAttack(groupA, unitstoAdd1);
        combat.pushAttack(groupB, unitstoAdd2);


        for(int i = 0; i< 4; i++){
            int r = combat.doOneTurnCombat(groupA, groupB);
            System.out.println(combat.getAttackUnitsbyPlayer(groupA).size());
            System.out.println(combat.getAttackUnitsbyPlayer(groupB).size());
            if(r == 0){
                unitstoAdd2.remove(0);
                unitstoAdd2.remove(unitstoAdd2.size()-1);
                assertEquals(combat.getAttackUnitsbyPlayer(groupA), unitstoAdd1);
                assertEquals(combat.getAttackUnitsbyPlayer(groupB), unitstoAdd2);
                // assertEquals(u1,combat.getAttackUnitofPlayer(groupA));
                // assertEquals(u2,combat.getAttackUnitofPlayer(groupB));
            }else if(r == 1){
                unitstoAdd1.remove(0);
                unitstoAdd2.remove(0);
                assertEquals(combat.getAttackUnitsbyPlayer(groupA), unitstoAdd1);
                assertEquals(combat.getAttackUnitsbyPlayer(groupB), unitstoAdd2);

            }else if(r == 2){
                unitstoAdd1.remove(unitstoAdd1.size()-1);
                unitstoAdd2.remove(unitstoAdd2.size()-1);
                assertEquals(combat.getAttackUnitsbyPlayer(groupA), unitstoAdd1);
                assertEquals(combat.getAttackUnitsbyPlayer(groupB), unitstoAdd2);
            }else if(r == 3){
                unitstoAdd1.remove(0);
                unitstoAdd1.remove(unitstoAdd1.size()-1);
                assertEquals(combat.getAttackUnitsbyPlayer(groupA), unitstoAdd1);
                assertEquals(combat.getAttackUnitsbyPlayer(groupB), unitstoAdd2);
            }
        }

        Territory t2 = mock(Territory.class);

        // when(t2.getUnits()).thenReturn(0);
        // when(t2.getOwner()).thenReturn(groupC);

        ArrayList<Unit> unitstoAdd3 = new ArrayList<>(Arrays.asList(new Unit(), new Unit(), new Unit(Level.INFANTRY), new Unit(Level.CAVALRY), new Unit(Level.TROOPER), new Unit(Level.ULTRON)));
        Combat combat2 = new Combat(t2);
        combat2.pushAttack(groupC, new ArrayList<>());
        combat2.pushAttack(groupA, unitstoAdd3);

        assertTrue(combat2.doOneTurnCombat(groupA, groupC) == 4 || combat2.doOneTurnCombat(groupA, groupC) == 5 );
    }

    @Test
    public void test_updateParticipantList(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player groupC = mock(Player.class);
        Player groupD = mock(Player.class);
        Player groupE = mock(Player.class);
        Player groupF = mock(Player.class);
        Territory tScadrial = mock(Territory.class);
        // when(tScadrial.getUnits()).thenReturn(5);
        // when(tScadrial.getOwner()).thenReturn(groupA);


        ArrayList<Unit> unitstoAdd = new ArrayList<>(Arrays.asList(new Unit(), new Unit(), new Unit(Level.INFANTRY), new Unit(Level.CAVALRY), new Unit(Level.TROOPER), new Unit(Level.ULTRON)));
        Combat combat = new Combat(tScadrial);
        combat.pushAttack(groupB, unitstoAdd);
        combat.pushAttack(groupC, unitstoAdd);
        combat.pushAttack(groupD, unitstoAdd);
        combat.pushAttack(groupE, unitstoAdd);
        combat.pushAttack(groupF, unitstoAdd);

        assertEquals(1, combat.updateParticipantList(0, 1));
        assertEquals(2, combat.updateParticipantList(1, 2));
        assertEquals(3, combat.updateParticipantList(2, 3));
        assertEquals(4, combat.updateParticipantList(3, 4));
        // assertEquals(5, combat.updateParticipantList(4, 5));
        assertEquals(0, combat.updateParticipantList(4, 0));


        Combat combat1 = new Combat(tScadrial);
        combat1.pushAttack(groupB, unitstoAdd);
        combat1.pushAttack(groupC, new ArrayList<>());
        combat1.pushAttack(groupD, unitstoAdd);
        combat1.pushAttack(groupE, unitstoAdd);
        combat1.pushAttack(groupF, unitstoAdd);

        assertEquals(1, combat1.updateParticipantList(0, 1));
        assertEquals(2, combat1.updateParticipantList(1, 2));
        assertEquals(3, combat1.updateParticipantList(2, 3));
        // assertEquals(5, combat.updateParticipantList(4, 5));
        assertEquals(0, combat1.updateParticipantList(3, 0));

        Combat combat2 = new Combat(tScadrial);
        combat2.pushAttack(groupB, unitstoAdd);
        combat2.pushAttack(groupC, new ArrayList<>());
        combat2.pushAttack(groupD, unitstoAdd);
        combat2.pushAttack(groupE, unitstoAdd);
        combat2.pushAttack(groupF, new ArrayList<>());

        assertEquals(1, combat2.updateParticipantList(0, 1));
        assertEquals(2, combat2.updateParticipantList(1, 2));
        assertEquals(0, combat2.updateParticipantList(2, 3));


        Combat combat3 = new Combat(tScadrial);
        combat3.pushAttack(groupB, new ArrayList<>());
        combat3.pushAttack(groupC, unitstoAdd);
        combat3.pushAttack(groupD, unitstoAdd);
        combat3.pushAttack(groupE, unitstoAdd);
        combat3.pushAttack(groupF, unitstoAdd);

        assertEquals(0, combat3.updateParticipantList(0, 1));
        assertEquals(1, combat3.updateParticipantList(0, 1));
        assertEquals(2, combat3.updateParticipantList(1, 2));
        assertEquals(3, combat3.updateParticipantList(2, 3));
        assertEquals(0, combat.updateParticipantList(3, 0));

    
    }

    @Test
    public void test_resolveCombat(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Player groupC = mock(Player.class);
        Player groupD = mock(Player.class);
        Player groupE = mock(Player.class);
        Player groupF = mock(Player.class);

        when(groupA.getName()).thenReturn("A");
        when(groupB.getName()).thenReturn("B");
        when(groupC.getName()).thenReturn("C");
        when(groupD.getName()).thenReturn("D");
        when(groupE.getName()).thenReturn("E");
        when(groupF.getName()).thenReturn("F");

        ArrayList<Player> parray = new ArrayList<>(Arrays.asList(groupA,groupB, groupC, groupD, groupE, groupF));

        ArrayList<Unit> unitstoAdd = new ArrayList<>(Arrays.asList(new Unit(), new Unit(), new Unit(Level.INFANTRY), new Unit(Level.CAVALRY), new Unit(Level.TROOPER), new Unit(Level.ULTRON)));

        Territory tScadrial = mock(Territory.class);
        
        when(tScadrial.getName()).thenReturn("Scadrial");

        when(tScadrial.removeAllUnits()).thenReturn(unitstoAdd);
        when(tScadrial.getOwner()).thenReturn(groupA);

        Combat combat = new Combat(tScadrial);
        assertNull(combat.resolveCombat());
        combat.pushAttack(groupB, unitstoAdd);
        combat.pushAttack(groupC, unitstoAdd);
        combat.pushAttack(groupD, unitstoAdd);
        combat.pushAttack(groupE, unitstoAdd);
        combat.pushAttack(groupF, unitstoAdd);

        Player winner = combat.resolveCombat();
        assertEquals(combat.getAttackPoolSize(), combat.getParticipantsSize());
        assertEquals(1,combat.getAttackPoolSize());
        
        assertTrue(combat.getAttackUnitofPlayer(winner) > 0);
        parray.remove(winner);

        for(Player p : parray){
            assertEquals(-1, combat.getAttackUnitofPlayer(p));
        }
        verify(tScadrial).setOwner(winner);
        verify(winner).addTerritory(tScadrial);
    }



}
