package edu.duke.ece651.team7.server.model;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import edu.duke.ece651.team7.shared.*;


public class LevelCheckerTest {

    @Mock
    private GameMap gameMap = mock(GameMap.class);

    // @Mock
    // private 

    // @Mock
    // private OrderCostVisitor ordercostvisitor;

    @Test
    public void test_BasicOrder(){
        Player p3 = mock(Player.class);
        Territory t1 = mock(Territory.class);
        Territory t2 = mock(Territory.class);

        // MoveOrder m2 = mock(MoveOrder.class);

        MoveOrder m1 = new MoveOrder(p3, t1, t2, 10);

        LevelChecker checker = new LevelChecker(null);
        assertNull(checker.checkOrderValidity(gameMap, m1));
    }

    @Test
    public void test_ResearchOrder(){
        Player p3 = mock(Player.class);
        ResearchOrder m1 = new ResearchOrder(p3);

        when(p3.getCurrentMaxLevel()).thenReturn(Level.CAVALRY);

        LevelChecker checker = new LevelChecker(null);
        assertNull(checker.checkOrderValidity(gameMap, m1));

        when(p3.getCurrentMaxLevel()).thenReturn(Level.ULTRON);
        assertEquals("LevelChecker error: Already the highest level.", checker.checkOrderValidity(gameMap, m1));
    }


    @Test
    public void test_upgradeOrder(){
        Player p3 = mock(Player.class);
        Territory t1 = mock(Territory.class);
        LevelChecker checker = new LevelChecker(null);
        
        UpgradeOrder m1 = new UpgradeOrder(p3, t1, Level.AIRFORCE, Level.ULTRON, 1);
        when(p3.getCurrentMaxLevel()).thenReturn(Level.ULTRON);
        assertNull(checker.checkOrderValidity(gameMap, m1));

        UpgradeOrder m2 = new UpgradeOrder(p3, t1, Level.INFANTRY, Level.ULTRON, 1);
        when(p3.getCurrentMaxLevel()).thenReturn(Level.CAVALRY);
        assertEquals("LevelChecker error: Your current Maximum level is " + p3.getCurrentMaxLevel(), checker.checkOrderValidity(gameMap, m2));

        UpgradeOrder m3 = new UpgradeOrder(p3, t1, Level.INFANTRY, Level.CIVILIAN, 1);
        when(p3.getCurrentMaxLevel()).thenReturn(Level.CAVALRY);
        assertEquals("LevelChecker error: Cannot upgrade to a lower or equal level", checker.checkOrderValidity(gameMap, m3));

    }
}

