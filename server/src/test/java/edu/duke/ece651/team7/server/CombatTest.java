package edu.duke.ece651.team7.server;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
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

        when(tMordor.getUnits()).thenReturn(5);
        when(tHogwarts.getUnits()).thenReturn(5);
        when(tNarnia.getUnits()).thenReturn(5);
        when(tMidkemia.getUnits()).thenReturn(5);

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


        when(tNarnia.getUnits()).thenReturn(5);
        when(tElantris.getUnits()).thenReturn(5);
        when(tMidkemia.getUnits()).thenReturn(5);
        when(tScadrial.getUnits()).thenReturn(5);
        when(tRoshar.getUnits()).thenReturn(5);
        when(tOz.getUnits()).thenReturn(5);
        when(tMordor.getUnits()).thenReturn(5);
        when(tHogwarts.getUnits()).thenReturn(5);
        when(tGondor.getUnits()).thenReturn(5);
        
        Combat combat1 = new Combat(tScadrial);
        assertEquals(tScadrial.getUnits(),combat1.getAttackUnitofPlayer(groupB));
        assertEquals(1, combat1.getParticipantsSize());

        combat1.pushAttack(groupA, 3);
        assertEquals(3, combat1.getAttackUnitofPlayer(groupA));
        assertEquals(2, combat1.getParticipantsSize());

        combat1.pushAttack(groupA, 4);
        assertEquals(7, combat1.getAttackUnitofPlayer(groupA));
        assertEquals(2, combat1.getParticipantsSize());

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


        when(tNarnia.getUnits()).thenReturn(5);
        when(tElantris.getUnits()).thenReturn(5);
        when(tMidkemia.getUnits()).thenReturn(5);
        when(tScadrial.getUnits()).thenReturn(5);
        when(tRoshar.getUnits()).thenReturn(5);
        when(tOz.getUnits()).thenReturn(5);
        when(tMordor.getUnits()).thenReturn(5);
        when(tHogwarts.getUnits()).thenReturn(5);
        when(tGondor.getUnits()).thenReturn(5);
        
        Combat combat1 = new Combat(tScadrial);
        assertFalse(combat1.hasCombat());
        assertTrue(combat1.combatEnd());

        combat1.pushAttack(groupC, 3);
        assertTrue(combat1.hasCombat());
        assertFalse(combat1.combatEnd());

    }

    @Test
    public void test_doOneUnitCombat(){
        Player groupA = mock(Player.class);
        Player groupB = mock(Player.class);
        Territory tScadrial = mock(Territory.class);
        int u1 = 5;
        int u2 = 5;
        when(tScadrial.getUnits()).thenReturn(u1);
        when(tScadrial.getOwner()).thenReturn(groupA);

        Combat combat = new Combat(tScadrial);
        combat.pushAttack(groupB, u2);

        for(int i = 0; i< 5; i++){
            if(combat.doOneUnitCombat(groupA, groupB)){
                u1--;
                assertEquals(u1,combat.getAttackUnitofPlayer(groupA));
                assertEquals(u2,combat.getAttackUnitofPlayer(groupB));
            }else{
                u2--;
                assertEquals(u1,combat.getAttackUnitofPlayer(groupA));
                assertEquals(u2,combat.getAttackUnitofPlayer(groupB));
            }
        }

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
        when(tScadrial.getUnits()).thenReturn(5);
        when(tScadrial.getOwner()).thenReturn(groupA);

        Combat combat = new Combat(tScadrial);
        combat.pushAttack(groupB, 5);
        combat.pushAttack(groupC, 5);
        combat.pushAttack(groupD, 5);
        combat.pushAttack(groupE, 5);
        combat.pushAttack(groupF, 5);

        assertEquals(1, combat.updateParticipantList(0, 1));
        assertEquals(2, combat.updateParticipantList(1, 2));
        assertEquals(3, combat.updateParticipantList(2, 3));
        assertEquals(4, combat.updateParticipantList(3, 4));
        assertEquals(5, combat.updateParticipantList(4, 5));
        assertEquals(0, combat.updateParticipantList(5, 0));

        combat.pushAttack(groupF, -5);
        assertEquals(0, combat.updateParticipantList(5, 0));
        assertEquals(5, combat.getParticipantsSize());

        combat.pushAttack(groupE, -5);
        assertEquals(0, combat.updateParticipantList(3, 4));
        assertEquals(4, combat.getParticipantsSize());

        combat.pushAttack(groupE, 5);
        assertEquals(5, combat.getParticipantsSize());
        combat.pushAttack(groupF, 5);
        assertEquals(6, combat.getParticipantsSize());
        
        //[A,B,C,D,E,F]
        combat.pushAttack(groupD, -5);
        assertEquals(3, combat.updateParticipantList(3, 4));
        assertEquals(5, combat.getParticipantsSize());

        //[A,B,C,E,F] -> [A,B,E,F]
        combat.pushAttack(groupC, -5);
        assertEquals(2, combat.updateParticipantList(2, 3));
        assertEquals(4, combat.getParticipantsSize());

        assertEquals(5, combat.getAttackUnitofPlayer(groupA));
        assertEquals(5, combat.getAttackUnitofPlayer(groupB));
        assertEquals(-1, combat.getAttackUnitofPlayer(groupC));
        assertEquals(-1, combat.getAttackUnitofPlayer(groupD));
        assertEquals(5, combat.getAttackUnitofPlayer(groupE));
        assertEquals(5, combat.getAttackUnitofPlayer(groupF));
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

        Territory tScadrial = mock(Territory.class);
        
        when(tScadrial.getName()).thenReturn("Scadrial");

        when(tScadrial.getUnits()).thenReturn(5);
        when(tScadrial.getOwner()).thenReturn(groupA);

        Combat combat = new Combat(tScadrial);
        assertNull(combat.resolveCombat());
        combat.pushAttack(groupB, 5);
        combat.pushAttack(groupC, 5);
        combat.pushAttack(groupD, 5);
        combat.pushAttack(groupE, 5);
        combat.pushAttack(groupF, 5);

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
