package edu.duke.ece651.team7.logingate.dto;

import java.util.Set;

public class GameDto {
    private final String host;
    private final int port;
    private final String name;
    private final int capacity;
    private final int initUnits;
    private final Set<String> inGameUsers;

    public GameDto(String host, int port, String name, int capacity, int initUnits, Set<String> inGameUsers) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.capacity = capacity;
        this.initUnits = initUnits;
        this.inGameUsers = inGameUsers;
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
}
