package edu.duke.ece651.team7.shared;

import java.io.Serializable;

public class Unit implements Comparable<Unit>, Serializable {
    private Level level;
    private final Player owner;
    private boolean carryBomb;

    public Unit() {
        this.level = Level.CIVILIAN;
        this.owner = null;
        this.carryBomb = false;
    }

    public Unit(Player p){
        this.level = Level.CIVILIAN;
        this.owner = p;
        this.carryBomb = false;
    }

    public Unit(Level l, Player p) {
        this.level = l;
        this.owner = p;
        this.carryBomb = false;
    }

    public Unit(Level l) {
        this.level = l;
        this.owner = null;
        this.carryBomb = false;
    }

    public Level getLevel() {
        return level;
    }

    public void upgrade(int num) {
        if (num < 1) {
            throw new IllegalArgumentException("Upgrade not valid");
        }
        int value = level.label;
        value += num;
        Level newlevel = Level.valueOfLabel(value);
        if (newlevel != null) {
            this.level = newlevel;
        } else {
            throw new IllegalArgumentException("Upgrade not valid");
        }
    }

    public void upgrade(Level newlevel) {
        this.level = newlevel;
    }

    public Player getOwner(){
        return this.owner;
    }

    public boolean hasBomb(){
        return this.carryBomb;
    }

    public void equipBomb(){
        if(this.level != Level.ULTRON){
            throw new IllegalArgumentException("Can only equip Ultron units, not " + this.level);
        }
        this.carryBomb = true;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass().equals(getClass())) {
            Unit otherUnit = (Unit) other;
            return this == otherUnit;
        }
        return false;
    }

    @Override
    public int compareTo(Unit u) {
        return level.compareTo(u.level);
    }

}
