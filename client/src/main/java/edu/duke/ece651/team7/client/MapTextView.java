package edu.duke.ece651.team7.client;

import java.io.PrintStream;
import java.util.TreeSet;

import edu.duke.ece651.team7.shared.*;

/**
 * This class provides a text-based view of the game map, including the
 * territories owned by each player and the number of units in each territory.
 */
public class MapTextView {
    /**
     * The PrintStream used for outputting information to the user.
     */
    private final PrintStream out;

    /**
     * Creates a new MapTextView instance with the specified output stream.
     *
     * @param out the PrintStream to which the map text will be printed
     */
    public MapTextView(PrintStream out) {
        this.out = out;
    }

    /**
     * Displays the game map as a text-based view, including the territories
     * owned by each player and the number of units in each territory.
     *
     * @param map the GameMap to be displayed
     */
    public void display(String msg) {
        out.println(msg);
    }

    /**
     * Displays the game map as a text-based view, including the territories
     * owned by each player and the number of units in each territory.
     *
     * @param map the GameMap to be displayed
     */
    public void display(GameMap map) {
        TreeSet<Player> playerSet = new TreeSet<>();
        for (Territory t : map.getTerritories()) {
            playerSet.add(t.getOwner());
        }
        for (Player p : playerSet) {
            display(map, p);
        }
    }

    /**
     * Displays the collection of Territories owned by a Player as a text-based
     *
     * @param map    the unique GameMap
     * @param player the displayed territories belonging to
     */
    public void display(GameMap map, Player p) {
        StringBuilder text = new StringBuilder("");
        String playerDecl = p.getName() + " player:";
        text.append(playerDecl + "\n");
        text.append("-".repeat(playerDecl.length()) + "\n");
        for (Territory t : p.getTerritories()) {
            text.append(String.format("%3s", t.getUnits()) + " units in " + t.getName() +
                    " (next to: ");
            String sep = "";
            for (Territory n : map.getNeighbors(t.getName())) {
                text.append(sep);
                text.append(n.getName());
                sep = ", ";
            }
            text.append(")\n");
        }
        text.append("\n");
        out.print(text.toString());
    }
}
