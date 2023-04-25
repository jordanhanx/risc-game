package edu.duke.ece651.team7.server.model;

import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team7.shared.FoodResource;
import edu.duke.ece651.team7.shared.GameMap;
import edu.duke.ece651.team7.shared.Level;
import edu.duke.ece651.team7.shared.Resource;
import edu.duke.ece651.team7.shared.TechResource;

public class OrderCostVisitor implements OrderVisitor<Resource>{
    private GameMap map;
    private Map<Level, Integer> unitUpgradeCost;
    private Map<Level, Integer> levelUpgradeCost;
    // CIVILIAN(0),
    // INFANTRY(1), 
    // CAVALRY(2), 
    // TROOPER(3), 
    // ARTILLERY(4), 
    // AIRFORCE(5), 
    // ULTRON(6);

    public OrderCostVisitor(GameMap map){
        this.map = map;
        unitUpgradeCost = new HashMap<>();
        this.unitUpgradeCost.put(Level.valueOfLabel(0), 0);
        this.unitUpgradeCost.put(Level.valueOfLabel(1), 3);
        this.unitUpgradeCost.put(Level.valueOfLabel(2), 11);
        this.unitUpgradeCost.put(Level.valueOfLabel(3), 30);
        this.unitUpgradeCost.put(Level.valueOfLabel(4), 55);
        this.unitUpgradeCost.put(Level.valueOfLabel(5), 90);
        this.unitUpgradeCost.put(Level.valueOfLabel(6), 140);

        levelUpgradeCost = new HashMap<>();

        this.levelUpgradeCost.put(Level.valueOfLabel(1), 0);
        this.levelUpgradeCost.put(Level.valueOfLabel(2), 20);
        this.levelUpgradeCost.put(Level.valueOfLabel(3), 40);
        this.levelUpgradeCost.put(Level.valueOfLabel(4), 80);
        this.levelUpgradeCost.put(Level.valueOfLabel(5), 160);
        this.levelUpgradeCost.put(Level.valueOfLabel(6), 320);
    }


    @Override
    public Resource visit(MoveOrder order) {
        int distance = 0;
        if(order.useAircraft){
            distance = 8;
        }else{
            distance = map.findShortestPath(order.src, order.dest);
        }
        int C = 2;
        int totalUnits = 0;
        for(Level l: order.units.keySet()){
            totalUnits += order.units.get(l);
        }
        Resource food = new FoodResource(C * distance * totalUnits);
        return food;
        
    }

    /**
     * An attack order now costs food units, as some function of the number and type of attacking units and distance between the territories.
     */
    @Override
    public Resource visit(AttackOrder order) {
        int distance = 0;
        if(order.useAircraft){
            distance = 8;
        }else{
            distance = map.getCostBetween(order.src, order.dest);
        }
        int C = 3;
        int totalUnits = 0;
        for(Level l: order.units.keySet()){
            totalUnits += order.units.get(l);
        }
        Resource food = new FoodResource(C * distance * totalUnits);
        return food;
    }

    @Override
    public Resource visit(ResearchOrder order) {
        if(order.issuer.getCurrentMaxLevel().label == 6){
            throw new IllegalArgumentException("Upgrade MaxLevel Error: Already the highest level");
        }
        Level nextLevel = Level.valueOfLabel(order.issuer.getCurrentMaxLevel().label + 1);
        return new TechResource(levelUpgradeCost.get(nextLevel));
    }

    @Override
    public Resource visit(UpgradeOrder order) {
        int percost = unitUpgradeCost.get(order.to) - unitUpgradeCost.get(order.from);
        return new TechResource(percost * order.units);
    }


    @Override
    public Resource visit(AllianceOrder order) {
        return null;
    }

    @Override
    public Resource visit(ManufactureOrder order) {
        if (order.bomb){
            return new TechResource(30 * order.amount);
        }else{
            return new TechResource(1000 * order.amount);
        }
    }
    
}
