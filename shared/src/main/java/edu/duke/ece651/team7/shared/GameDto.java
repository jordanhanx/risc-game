package edu.duke.ece651.team7.shared;

import java.util.HashSet;
import java.util.Set;

/**
 * The GameDto class represents a data transfer object for a game.
 */
public class GameDto {
    /**
     * The host of the game.
     */
    private String host;
    /**
     * The port of the game.
     */
    private int port;
    /**
     * The name of the game.
     */
    private String name;
    /**
     * The maximum number of players allowed in the game.
     */
    private int capacity;
    /**
     * The initial number of units each player has in the game.
     */
    private int initUnits;
    /**
     * The set of usernames of players currently in the game.
     */
    private Set<String> inGameUsers;

    /**
     * Default constructor for GameDto.
     * Initializes all fields to default values.
     */
    public GameDto() {
        this.host = null;
        this.port = 0;
        this.name = null;
        this.capacity = 0;
        this.initUnits = 0;
        this.inGameUsers = new HashSet<>();
    }

    /**
     * Constructor for GameDto.
     * Initializes all fields to the specified values.
     *
     * @param host        The host of the game.
     * @param port        The port of the game.
     * @param name        The name of the game.
     * @param capacity    The maximum number of players allowed in the game.
     * @param initUnits   The initial number of units each player has in the game.
     * @param inGameUsers The set of usernames of players currently in the game.
     */
    public GameDto(String host, int port, String name, int capacity, int initUnits, Set<String> inGameUsers) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.capacity = capacity;
        this.initUnits = initUnits;
        this.inGameUsers = inGameUsers;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setInitUnits(int initUnits) {
        this.initUnits = initUnits;
    }

    public int getInitUnits() {
        return this.initUnits;
    }

    public void setInGameUsers(Set<String> inGameUsers) {
        for (String username : inGameUsers) {
            this.inGameUsers.add(username);
        }
    }

    public Set<String> getInGameUsers() {
        return this.inGameUsers;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("host: " + host + ", ");
        string.append("port: " + port + ", ");
        string.append("name: " + name + ", ");
        string.append("capacity: " + capacity + ", ");
        string.append("initUnits: " + initUnits + ", ");
        string.append("inGameUsers: [ ");
        for (String username : inGameUsers) {
            string.append(username + ", ");
        }
        string.append("]");
        return string.toString();
    }
}
