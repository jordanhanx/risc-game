package edu.duke.ece651.team7.client.model;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.team7.shared.GameDto;

public class GameList extends ArrayList<GameDto> {

    private static GameList obj = new GameList();

    private GameList() {
        super();
    }

    public static GameList getInstance() {
        return obj;
    }

    public void update(List<GameDto> games) {
        this.clear();
        for (GameDto g : games) {
            this.add(g);
        }
    }
}
