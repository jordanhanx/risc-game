package edu.duke.ece651.team7.shared;

public class Unit implements Comparable<Unit> {
    private Level level;

    public Unit(){
        this.level = Level.CIVILIAN;
    }

    public Level getLevel(){
        return level;
    }

    public void upgrade(int num){
        if(num < 1){
            throw new IllegalArgumentException("Upgrade not valid");
        }
        int value = level.label;
        value += num;
        Level newlevel = Level.valueOfLabel(value);
        if(newlevel != null){
            this.level = newlevel;
        }else{
            throw new IllegalArgumentException("Upgrade not valid");
        }
    }

    @Override
    public int compareTo(Unit u) {
       return u.level.compareTo(level);
    }
}
