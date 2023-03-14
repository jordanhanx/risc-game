package edu.duke.ece651.team7.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

public class MapTextViewTest {
    @Test
    public void test_display() {
        // Setup GameMap
        GameMap mockMap = mock(GameMap.class);
        Player pBlue = mock(Player.class);
        Player pGreen = mock(Player.class);
        Player pRed = mock(Player.class);

        Territory tNarnia = mock(Territory.class);
        Territory tMidkemia = mock(Territory.class);
        Territory tOz = mock(Territory.class);
        Territory tElantris = mock(Territory.class);
        Territory tScadrial = mock(Territory.class);
        Territory tRoshar = mock(Territory.class);
        Territory tGondor = mock(Territory.class);
        Territory tMordor = mock(Territory.class);
        Territory tHogwarts = mock(Territory.class);

        when(pBlue.getName()).thenReturn("Blue");
        when(pGreen.getName()).thenReturn("Green");
        when(pRed.getName()).thenReturn("Red");

        when(pBlue.compareTo(pGreen)).thenReturn(-1);
        when(pBlue.compareTo(pBlue)).thenReturn(0);
        when(pBlue.compareTo(pRed)).thenReturn(-1);

        when(pGreen.compareTo(pGreen)).thenReturn(0);
        when(pGreen.compareTo(pBlue)).thenReturn(1);
        when(pGreen.compareTo(pRed)).thenReturn(-1);

        when(pRed.compareTo(pGreen)).thenReturn(1);
        when(pRed.compareTo(pBlue)).thenReturn(1);
        when(pRed.compareTo(pRed)).thenReturn(0);

        when(tNarnia.getName()).thenReturn("Narnia");
        when(tMidkemia.getName()).thenReturn("Midkemia");
        when(tOz.getName()).thenReturn("Oz");
        when(tElantris.getName()).thenReturn("Elantris");
        when(tScadrial.getName()).thenReturn("Scadrial");
        when(tRoshar.getName()).thenReturn("Roshar");
        when(tGondor.getName()).thenReturn("Gondor");
        when(tMordor.getName()).thenReturn("Mordor");
        when(tHogwarts.getName()).thenReturn("Hogwarts");

        when(tNarnia.getUnits()).thenReturn(10);
        when(tMidkemia.getUnits()).thenReturn(12);
        when(tOz.getUnits()).thenReturn(8);
        when(tElantris.getUnits()).thenReturn(6);
        when(tScadrial.getUnits()).thenReturn(5);
        when(tRoshar.getUnits()).thenReturn(3);
        when(tGondor.getUnits()).thenReturn(13);
        when(tMordor.getUnits()).thenReturn(14);
        when(tHogwarts.getUnits()).thenReturn(3);

        when(tNarnia.getOwner()).thenReturn(pGreen);
        when(tMidkemia.getOwner()).thenReturn(pGreen);
        when(tOz.getOwner()).thenReturn(pGreen);
        when(tElantris.getOwner()).thenReturn(pBlue);
        when(tScadrial.getOwner()).thenReturn(pBlue);
        when(tRoshar.getOwner()).thenReturn(pBlue);
        when(tGondor.getOwner()).thenReturn(pRed);
        when(tMordor.getOwner()).thenReturn(pRed);
        when(tHogwarts.getOwner()).thenReturn(pRed);
        LinkedList<Territory> blueTerritories = new LinkedList<Territory>() {
            {
                add(tElantris);
                add(tScadrial);
                add(tRoshar);
            }
        };
        LinkedList<Territory> greenTerritories = new LinkedList<Territory>() {
            {
                add(tNarnia);
                add(tMidkemia);
                add(tOz);
            }
        };
        LinkedList<Territory> redTerritories = new LinkedList<Territory>() {
            {
                add(tGondor);
                add(tMordor);
                add(tHogwarts);
            }
        };

        when(pBlue.getTerritories()).thenReturn(blueTerritories);
        when(pGreen.getTerritories()).thenReturn(greenTerritories);
        when(pRed.getTerritories()).thenReturn(redTerritories);

        LinkedList<Territory> allTerritories = new LinkedList<Territory>() {
            {
                add(tNarnia);
                add(tMidkemia);
                add(tOz);
                add(tElantris);
                add(tScadrial);
                add(tRoshar);
                add(tHogwarts);
                add(tMordor);
                add(tGondor);
            }
        };
        when(mockMap.getTerritories()).thenReturn(allTerritories);

        LinkedList<Territory> tNarniaNeighbors = new LinkedList<Territory>() {
            {
                add(tMidkemia);
                add(tElantris);
            }
        };
        LinkedList<Territory> tMidkemiaNeighbors = new LinkedList<Territory>() {
            {
                add(tNarnia);
                add(tElantris);
                add(tScadrial);
                add(tOz);
            }
        };
        LinkedList<Territory> tOzNeighbors = new LinkedList<Territory>() {
            {
                add(tMidkemia);
                add(tScadrial);
                add(tMordor);
                add(tGondor);
            }
        };
        LinkedList<Territory> tElantrisNeighbors = new LinkedList<Territory>() {
            {
                add(tNarnia);
                add(tMidkemia);
                add(tScadrial);
                add(tRoshar);
            }
        };
        LinkedList<Territory> tScadrialNeighbors = new LinkedList<Territory>() {
            {
                add(tElantris);
                add(tMidkemia);
                add(tOz);
                add(tMordor);
                add(tHogwarts);
                add(tRoshar);
            }
        };
        LinkedList<Territory> tRosharNeighbors = new LinkedList<Territory>() {
            {
                add(tElantris);
                add(tScadrial);
                add(tHogwarts);
            }
        };
        LinkedList<Territory> tGondorNeighbors = new LinkedList<Territory>() {
            {
                add(tOz);
                add(tMordor);
            }
        };
        LinkedList<Territory> tMordorNeighbors = new LinkedList<Territory>() {
            {
                add(tGondor);
                add(tOz);
                add(tScadrial);
                add(tHogwarts);
            }
        };
        LinkedList<Territory> tHogwartsNeighbors = new LinkedList<Territory>() {
            {
                add(tMordor);
                add(tScadrial);
                add(tRoshar);
            }
        };
        when(mockMap.getNeighbors("Narnia")).thenReturn(tNarniaNeighbors);
        when(mockMap.getNeighbors("Midkemia")).thenReturn(tMidkemiaNeighbors);
        when(mockMap.getNeighbors("Oz")).thenReturn(tOzNeighbors);
        when(mockMap.getNeighbors("Elantris")).thenReturn(tElantrisNeighbors);
        when(mockMap.getNeighbors("Scadrial")).thenReturn(tScadrialNeighbors);
        when(mockMap.getNeighbors("Roshar")).thenReturn(tRosharNeighbors);
        when(mockMap.getNeighbors("Gondor")).thenReturn(tGondorNeighbors);
        when(mockMap.getNeighbors("Mordor")).thenReturn(tMordorNeighbors);
        when(mockMap.getNeighbors("Hogwarts")).thenReturn(tHogwartsNeighbors);

        // test part
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        MapTextView view = new MapTextView(out);

        StringBuilder outputs = new StringBuilder();
        outputs.append("Blue player:\n");
        outputs.append("------------\n");
        outputs.append("  6 units in Elantris (next to: Narnia, Midkemia, Scadrial, Roshar)\n");
        outputs.append("  5 units in Scadrial (next to: Elantris, Midkemia, Oz, Mordor, Hogwarts, Roshar)\n");
        outputs.append("  3 units in Roshar (next to: Elantris, Scadrial, Hogwarts)\n");
        outputs.append("\n");

        outputs.append("Green player:\n");
        outputs.append("-------------\n");
        outputs.append(" 10 units in Narnia (next to: Midkemia, Elantris)\n");
        outputs.append(" 12 units in Midkemia (next to: Narnia, Elantris, Scadrial, Oz)\n");
        outputs.append("  8 units in Oz (next to: Midkemia, Scadrial, Mordor, Gondor)\n");
        outputs.append("\n");

        outputs.append("Red player:\n");
        outputs.append("-----------\n");
        outputs.append(" 13 units in Gondor (next to: Oz, Mordor)\n");
        outputs.append(" 14 units in Mordor (next to: Gondor, Oz, Scadrial, Hogwarts)\n");
        outputs.append("  3 units in Hogwarts (next to: Mordor, Scadrial, Roshar)\n");
        outputs.append("\n");

        bytes.reset();
        assertDoesNotThrow(() -> view.display(mockMap));
        assertEquals(outputs.toString(), bytes.toString());

        verify(pGreen, atLeastOnce()).getName();
        verify(pBlue, atLeastOnce()).getName();
        verify(pRed, atLeastOnce()).getName();

        verify(pGreen, atLeastOnce()).getTerritories();
        verify(pBlue, atLeastOnce()).getTerritories();
        verify(pRed, atLeastOnce()).getTerritories();

        verify(tNarnia, atLeastOnce()).getUnits();
        verify(tMidkemia, atLeastOnce()).getUnits();
        verify(tOz, atLeastOnce()).getUnits();
        verify(tElantris, atLeastOnce()).getUnits();
        verify(tScadrial, atLeastOnce()).getUnits();
        verify(tRoshar, atLeastOnce()).getUnits();
        verify(tGondor, atLeastOnce()).getUnits();
        verify(tMordor, atLeastOnce()).getUnits();
        verify(tHogwarts, atLeastOnce()).getUnits();

        verify(tNarnia, atLeastOnce()).getName();
        verify(tMidkemia, atLeastOnce()).getName();
        verify(tOz, atLeastOnce()).getName();
        verify(tElantris, atLeastOnce()).getName();
        verify(tScadrial, atLeastOnce()).getName();
        verify(tRoshar, atLeastOnce()).getName();
        verify(tGondor, atLeastOnce()).getName();
        verify(tMordor, atLeastOnce()).getName();
        verify(tHogwarts, atLeastOnce()).getName();

        verify(tNarnia, atLeastOnce()).getOwner();
        verify(tMidkemia, atLeastOnce()).getOwner();
        verify(tOz, atLeastOnce()).getOwner();
        verify(tElantris, atLeastOnce()).getOwner();
        verify(tScadrial, atLeastOnce()).getOwner();
        verify(tRoshar, atLeastOnce()).getOwner();
        verify(tGondor, atLeastOnce()).getOwner();
        verify(tMordor, atLeastOnce()).getOwner();
        verify(tHogwarts, atLeastOnce()).getOwner();

        verify(mockMap, times(1)).getTerritories();
        verify(mockMap, atLeastOnce()).getNeighbors("Narnia");
        verify(mockMap, atLeastOnce()).getNeighbors("Midkemia");
        verify(mockMap, atLeastOnce()).getNeighbors("Oz");
        verify(mockMap, atLeastOnce()).getNeighbors("Elantris");
        verify(mockMap, atLeastOnce()).getNeighbors("Scadrial");
        verify(mockMap, atLeastOnce()).getNeighbors("Roshar");
        verify(mockMap, atLeastOnce()).getNeighbors("Gondor");
        verify(mockMap, atLeastOnce()).getNeighbors("Mordor");
        verify(mockMap, atLeastOnce()).getNeighbors("Hogwarts");
    }
}
