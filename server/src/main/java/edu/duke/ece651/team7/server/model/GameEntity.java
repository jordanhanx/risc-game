package edu.duke.ece651.team7.server.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.duke.ece651.team7.shared.*;

/**
 * The GameEntity class represents a game object that implements the RemoteGame
 * interface.
 * The class provides methods for managing game state and interaction with
 * remote clients.
 */
public class GameEntity extends UnicastRemoteObject implements RemoteGame {

    private static final Logger logger = LoggerFactory.getLogger(GameEntity.class);

    private final String host;
    private final int port;
    private final String name;
    private final int capacity;
    private final int initUnits;
    private GameMap gameMap;
    private OrderExecuteVisitor ox;
    private Map<String, Player> playerMap;
    private Map<String, GamePhase> phaseMap;
    private Map<String, RemoteClient> clientMap;
    private Set<String> commitSet;
    private CountDownLatch commitSignal;

    /**
     * Constructor for GameEntity class.
     * 
     * @param host      the host address of the game
     * @param port      the port number of the game
     * @param name      the name of the game
     * @param capacity  the maximum number of players that can join the game
     * @param initUnits the initial number of units each player starts with
     * @throws RemoteException       if there is an issue with remote invocation
     * @throws IllegalStateException if the capacity or initUnits values are invalid
     */
    public GameEntity(String host, int port, String name, int capacity, int initUnits) throws RemoteException {
        if (capacity < 2) {
            throw new IllegalStateException("capacity cannot be less than 2");
        }
        if (initUnits < 1) {
            throw new IllegalStateException("initUnits cannot be less than 1");
        }
        this.host = host;
        this.port = port;
        this.name = name;
        this.capacity = capacity;
        this.initUnits = initUnits;
        this.gameMap = new GUIMapFactory().createPlayerMap(capacity);
        this.ox = new OrderExecuteVisitor(gameMap);
        this.playerMap = new HashMap<>();
        this.phaseMap = new HashMap<>();
        this.clientMap = new HashMap<>();
        this.commitSet = new HashSet<>();
        setCountDownLatch(capacity);
        logger.info(name + " is ready");
    }

    /**
     * Set up the CountDownLatch objects that will be used to manage synchronization
     * of game events.
     *
     * @param num the number of latches to set up
     */
    protected void setCountDownLatch(int num) {
        this.commitSignal = new CountDownLatch(num);
        commitSet.clear();
    }

    /**
     * Start the game by executing the main game loop.
     *
     * @throws RemoteException      if there is an issue with remote invocation
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void start() throws RemoteException, InterruptedException {
        /* Pick Group & Place Units Phase */
        commitSignal.await(); // waits for all players placing their initial units
        setCountDownLatch(capacity); // reset countDownLatch
        /* Game Start Phase */
        ox.collectAllResource();
        sendGameMapToClients();
        while (true) {
            commitSignal.await();
            setCountDownLatch(countNotLostPlayers()); // reset countDownLatch
            ox.resolveOneRound();
            if (countNotLostPlayers() != 1) {
                sendGameMapToClients();
                sendPlayersToClients();
                sendMessageToClients("NEXT TURN: GAMEMAP UPDATED!");
            } else {
                sendMessageToClients("GAME OVER! Winner is " + findWinner());
                UnicastRemoteObject.unexportObject(this, true); // close this game
                logger.info(name + " is over");
                break;
            }
        }
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

    @Override
    public int getGameInitUnits() throws RemoteException {
        return this.initUnits;
    }

    public Set<String> getUsers() {
        return this.playerMap.keySet();
    }

    /**
     * Add a player with the given username to the game.
     *
     * @param username the username of the player to add
     * @throws IllegalStateException if the player is already in the game or if the
     *                               game is full
     */
    public void addUser(String username) {
        if (playerMap.containsKey(username)) {
            throw new IllegalStateException("The Player" + username + " already joined");
        } else if (playerMap.size() == capacity) {
            throw new IllegalStateException("The Game:" + name + " is already full");
        } else {
            playerMap.put(username, new Player(username));
            phaseMap.put(username, RemoteGame.GamePhase.PICK_GROUP);
        }
    }

    @Override
    public synchronized String tryRegisterClient(String username, RemoteClient client) throws RemoteException {
        if (playerMap.containsKey(username)) {
            clientMap.put(username, client);
            return null;
        } else {
            return "Username didn't match";
        }
    }

    @Override
    public synchronized GamePhase getGamePhase(String username) throws RemoteException {
        return phaseMap.get(username);
    }

    @Override
    public synchronized GameMap getGameMap() throws RemoteException {
        return gameMap;
    }

    @Override
    public synchronized Player getSelfStatus(String username) throws RemoteException {
        return playerMap.get(username);
    }

    @Override
    public synchronized String tryPickTerritoryGroupByName(String username, String groupName) throws RemoteException {
        String response = null;
        try {
            gameMap.assignGroup(groupName, playerMap.get(username));
            phaseMap.put(username, RemoteGame.GamePhase.PLACE_UNITS);
            response = null;
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public synchronized String tryPlaceUnitsOn(String username, String territory, int units) throws RemoteException {
        String response = null;
        try {
            Territory t = gameMap.getTerritoryByName(territory);
            Player p = playerMap.get(username);
            int remainingUnits = initUnits - p.getTotalUnits();
            if (!t.getOwner().equals(p)) {
                response = "Permission denied";
            } else if (units > remainingUnits) {
                response = "Too many units";
            } else if (units < 0) {
                response = "units cannot be less than 0";
            } else {
                for (int i = 0; i < units; i++) {
                    t.addUnits(new Unit());
                }
            }
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public synchronized String tryMoveOrder(String username, String src, String dest, int level, int units)
            throws RemoteException {
        String response = null;
        try {
            if (commitSet.contains(username)) {
                response = "Please wait for other players to commit";
            } else {
                MoveOrder mo = new MoveOrder(playerMap.get(username), gameMap.getTerritoryByName(src),
                        gameMap.getTerritoryByName(dest), Level.valueOfLabel(level), units);
                mo.accept(ox);
                clientMap.get(username).updatePlayer(playerMap.get(username));
            }
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public synchronized String tryAttackOrder(String username, String src, String dest, int level, int units)
            throws RemoteException {
        String response = null;
        try {
            if (commitSet.contains(username)) {
                response = "Please wait for other players to commit";
            } else {
                AttackOrder ao = new AttackOrder(playerMap.get(username), gameMap.getTerritoryByName(src),
                        gameMap.getTerritoryByName(dest), Level.valueOfLabel(level), units);
                ao.accept(ox);
                clientMap.get(username).updatePlayer(playerMap.get(username));
            }
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public synchronized String tryUpgradeOrder(String username, String target, int fromlevel, int tolevel, int units)
            throws RemoteException {
        String response = null;
        try {
            if (commitSet.contains(username)) {
                response = "Please wait for other players to commit";
            } else {
                UpgradeOrder uo = new UpgradeOrder(playerMap.get(username), gameMap.getTerritoryByName(target),
                        Level.valueOfLabel(fromlevel), Level.valueOfLabel(tolevel), units);
                uo.accept(ox);
            }
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public synchronized String tryResearchOrder(String username) throws RemoteException {
        String response = null;
        try {
            if (commitSet.contains(username)) {
                response = "Please wait for other players to commit";
            } else {
                ResearchOrder ro = new ResearchOrder(playerMap.get(username));
                ro.accept(ox);
            }
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public synchronized String doCommitOrder(String username) throws RemoteException, InterruptedException {
        String response = null;
        if (playerMap.get(username).isLose()) {
            response = "Lost user cannot commit";
        } else if (commitSet.contains(username)) {
            response = "Please wait for other players to commit";
        } else {
            commitSignal.countDown();
            commitSet.add(username);
            phaseMap.put(username, RemoteGame.GamePhase.PLAY_GAME);
        }
        return response;
    }

    int countNotLostPlayers() {
        int counter = 0;
        for (Player p : playerMap.values()) {
            if (!p.isLose()) {
                ++counter;
            }
        }
        return counter;
    }

    String findWinner() {
        String winner = null;
        int winnerCounter = 0;
        for (Map.Entry<String, Player> e : playerMap.entrySet()) {
            if (!e.getValue().isLose()) {
                winner = e.getKey();
                ++winnerCounter;
            }
        }
        if (winnerCounter == 1) {
            return winner;
        } else {
            return null;
        }
    }

    /**
     * Notifies clients who have lost the game to switch to watcher mode using RMI.
     */
    void sendGameMapToClients() {
        for (Map.Entry<String, RemoteClient> pair : clientMap.entrySet()) {
            try {
                pair.getValue().updateGameMap(gameMap);
            } catch (RemoteException e) {
                /*
                 * RemoteException because the remote Client has disconnected, can be ignored
                 */
            }
        }
    }

    void sendPlayersToClients() {
        for (Map.Entry<String, RemoteClient> pair : clientMap.entrySet()) {
            try {
                pair.getValue().updatePlayer(playerMap.get(pair.getKey()));
            } catch (RemoteException e) {
                /*
                 * RemoteException because the remote Client has disconnected, can be ignored
                 */
            }
        }
    }

    /**
     * Notifies all clients that the game is over and displays the winner using RMI.
     */
    void sendMessageToClients(String msg) {
        for (RemoteClient client : clientMap.values()) {
            try {
                client.showPopupWindow(msg);
            } catch (RemoteException e) {
                /*
                 * RemoteException because the remote Client has disconnected, can be ignored
                 */
            }
        }
    }
}

/* EOF */