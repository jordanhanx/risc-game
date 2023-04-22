package edu.duke.ece651.team7.shared;

/**
 * Represents the levels that a player's units can have in the game.
 */
public enum Level {
    CIVILIAN(0),
    INFANTRY(1),
    CAVALRY(2),
    TROOPER(3),
    ARTILLERY(4),
    AIRBORNE(5),
    ULTRON(6);

    public final int label;

    /**
     * Constructs a new level with the specified label.
     * 
     * @param label the label of the level
     */
    private Level(int label) {
        this.label = label;
    }

    /**
     * Returns the level with the specified label.
     * 
     * @param lab the label of a level want to get
     * @return the level with lable(value)
     */
    public static Level valueOfLabel(int lab) {
        for (Level l : values()) {
            if (l.label == lab) {
                return l;
            }
        }
        return null;
    }

}
