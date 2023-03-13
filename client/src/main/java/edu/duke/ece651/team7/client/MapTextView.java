package edu.duke.ece651.team7.client;

import java.io.PrintStream;
import java.util.HashSet;

import edu.duke.ece651.team7.shared.*;

public class MapTextView {

    public void display(PrintStream out, GameMap map) {
        HashSet<Player> playerSet = new HashSet<>();
        // for (Territory t : map.getTerritories()) {
        // playerSet.add(t.getOwner());
        // }
        // StringBuilder text = new StringBuilder("");
        // for (Player p : playerSet) {
        // String playerDecl = p.getName() + "player:";
        // text.append(playerDecl + "\n");
        // text.append("_".repeat(playerDecl.length()) + "\n");
        // for (Territory t : p.getTerritories()) {
        // text.append(String.format("%3s", t.getUnits()) + " units in " + t.getName() +
        // " (next to: ");
        // String sep = "";
        // for (Territory n : t.getNeighbors()) {
        // text.append(sep);
        // text.append(n.getName());
        // sep = ", ";
        // }
        // text.append(")\n");
        // }
        // text.append("\n");
        // }
    }
}
