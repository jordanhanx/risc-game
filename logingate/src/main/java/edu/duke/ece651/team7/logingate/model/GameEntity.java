package edu.duke.ece651.team7.logingate.model;

import java.util.HashSet;
import java.util.Set;

public class GameEntity {
    private final String host;
    private final int port;
    private final String name;
    private final int capacity;
    private final int initUnits;
    private Set<String> inGameUsers;
    private Thread thread;

    public GameEntity(String host, int port, String name, int capacity, int initUnits, Runnable runnable) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.capacity = capacity;
        this.initUnits = initUnits;
        this.inGameUsers = new HashSet<String>();
        this.thread = new Thread(runnable);
        thread.start();
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getName() {
        return this.name;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getInitUnits() {
        return this.initUnits;
    }

    public Set<String> getInGameUsers() {
        return this.inGameUsers;
    }

    public void addUser(String username) {
        if (inGameUsers.contains(username)) {
            throw new IllegalStateException("The Player" + username + " already joined");
        }
        if (inGameUsers.size() == capacity) {
            throw new IllegalStateException("The Game:" + name + " is already full");
        }
        inGameUsers.add(username);
    }
}
