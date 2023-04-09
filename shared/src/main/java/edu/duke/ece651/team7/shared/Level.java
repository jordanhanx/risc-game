package edu.duke.ece651.team7.shared;

public enum Level {
    CIVILIAN(0),
    INFANTRY(1), 
    CAVALRY(2), 
    TROOPER(3), 
    ARTILLERY(4), 
    AIRFORCE(5), 
    ULTRON(6);

    public final int label;

    private Level(int label) {
        this.label = label;
    }

    /**
     * 
     * @param lab the label of a level want to get
     * @return the level with lable(value)
     */
    public static Level valueOfLabel(int lab){
        for (Level l: values()){
            if(l.label == lab){
                return l;
            }
        }
        return null;
    }
}
