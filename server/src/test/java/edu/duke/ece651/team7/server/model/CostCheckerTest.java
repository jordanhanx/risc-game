package edu.duke.ece651.team7.server.model;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import edu.duke.ece651.team7.shared.*;


public class CostCheckerTest {

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

        OrderCostVisitor costvisitor = mock(OrderCostVisitor.class);

        when(costvisitor.visit(m1)).thenReturn(new FoodResource(300));
        when(gameMap.findShortestPath(t1, t2)).thenReturn(100);
        when(p3.getFood()).thenReturn(new FoodResource(200));

        CostChecker checker = new CostChecker(null, costvisitor);
        assertEquals("CostCheker error: No enough food.",checker.checkOrderValidity(gameMap, m1));

        when(p3.getFood()).thenReturn(new FoodResource(500));
        assertNull(checker.checkOrderValidity(gameMap, m1));

        MoveOrder m2 = new MoveOrder(p3, true, t1, t2, Level.AIRBORNE, 3);
        when(p3.getAircraft()).thenReturn(new LinkedList<>());
        when(costvisitor.visit(m2)).thenReturn(new FoodResource(300));
        assertEquals("CostCheker error: No enough aircraft.",checker.checkOrderValidity(gameMap, m2));
    }

    @Test
    public void test_AttackOrder(){
        Player p3 = mock(Player.class);
        Territory t1 = mock(Territory.class);
        Territory t2 = mock(Territory.class);

        // MoveOrder m2 = mock(MoveOrder.class);

        AttackOrder m1 = new AttackOrder(p3, false, 10, t1, t2, Level.ULTRON, 3);


        OrderCostVisitor costvisitor = mock(OrderCostVisitor.class);

        when(costvisitor.visit(m1)).thenReturn(new FoodResource(300));
        when(p3.getFood()).thenReturn(new FoodResource(500));

        CostChecker checker = new CostChecker(null, costvisitor);
        assertEquals("CostCheker error: No enough Bomb.",checker.checkOrderValidity(gameMap, m1));
    }

    @Test
    public void test_ResearchOrder(){
        Player p3 = mock(Player.class);

        // MoveOrder m2 = mock(MoveOrder.class);

        ResearchOrder m1 = new ResearchOrder(p3);

        OrderCostVisitor costvisitor = mock(OrderCostVisitor.class);

        when(costvisitor.visit(m1)).thenReturn(new TechResource(300));
        when(p3.getTech()).thenReturn(new TechResource(200));

        CostChecker checker = new CostChecker(null, costvisitor);
        assertEquals("CostCheker error: No enough Tech.",checker.checkOrderValidity(gameMap, m1));

        when(p3.getTech()).thenReturn(new TechResource(500));
        assertNull(checker.checkOrderValidity(gameMap, m1));
    }


    @Test
    public void test_upgradeOrder(){
        Player p3 = mock(Player.class);
        Territory t1 = mock(Territory.class);

        UpgradeOrder m1 = new UpgradeOrder(p3,t1, Level.CIVILIAN,Level.AIRBORNE, 10);

        OrderCostVisitor costvisitor = mock(OrderCostVisitor.class);

        when(costvisitor.visit(m1)).thenReturn(new TechResource(300));
        when(p3.getTech()).thenReturn(new TechResource(200));

        CostChecker checker = new CostChecker(null, costvisitor);
        assertEquals("CostCheker error: No enough Tech.",checker.checkOrderValidity(gameMap, m1));

        when(p3.getTech()).thenReturn(new TechResource(500));
        assertNull(checker.checkOrderValidity(gameMap, m1));
    }

    @Test
    public void test_manufactureOrder(){
        Player p3 = mock(Player.class);
        Territory t1 = mock(Territory.class);

        // UpgradeOrder m1 = new UpgradeOrder(p3,t1, Level.CIVILIAN,Level.AIRBORNE, 10);
        ManufactureOrder m1 = new ManufactureOrder(p3, false, 1);

        OrderCostVisitor costvisitor = mock(OrderCostVisitor.class);

        when(costvisitor.visit(m1)).thenReturn(new TechResource(300));
        when(p3.getTech()).thenReturn(new TechResource(200));

        CostChecker checker = new CostChecker(null, costvisitor);
        assertEquals("CostCheker error: No enough Tech.",checker.checkOrderValidity(gameMap, m1));

        when(p3.getTech()).thenReturn(new TechResource(500));
        assertNull(checker.checkOrderValidity(gameMap, m1));
    }
}
