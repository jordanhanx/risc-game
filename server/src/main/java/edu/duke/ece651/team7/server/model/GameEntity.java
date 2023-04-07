package edu.duke.ece651.team7.server.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.duke.ece651.team7.shared.*;

public class GameEntity extends UnicastRemoteObject implements RemoteGame {

    private static final Logger logger = LoggerFactory.getLogger(GameEntity.class);

    private final String host;
    private final int port;
    private final String name;
    private final int capacity;
    private final int initUnits;
    private int lostCounter;
    protected Map<String, Player> playerMap;
    protected Map<String, RemoteClient> clientMap;
    protected GameMap gameMap;
    protected OrderExecuter ox;
    protected CountDownLatch commitSignal;
    protected CyclicBarrier returnSignal;

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
        this.lostCounter = 0;
        this.playerMap = new HashMap<>();
        this.clientMap = new HashMap<>();
        this.gameMap = new TextMapFactory().createPlayerMap(capacity);
        this.ox = new OrderExecuter(gameMap);
        setupCountDownLatches(capacity);
        logger.info(name + " is ready");
    }

    protected void setupCountDownLatches(int num) {
        this.commitSignal = new CountDownLatch(num);
        this.returnSignal = new CyclicBarrier(num + 1);
    }

    public void start() throws RemoteException, InterruptedException, BrokenBarrierException {
        /* Placement Phase */
        commitSignal.await(); // waits for all players picking groups of territories
        returnSignal.await(); // ready to next state
        setupCountDownLatches(capacity); // reset locks
        commitSignal.await(); // waits for all players placing their initial units
        returnSignal.await(); // ready to next state
        setupCountDownLatches(capacity); // reset locks
        /* GamePlay phase */
        while (true) {
            commitSignal.await();
            ox.doAllCombats();
            updateLostCounter();
            returnSignal.await(); // ready to next state
            if (!isGameOver()) {
                notifyGameMapToLostClients(); // send game status to all watching clients
                setupCountDownLatches(capacity - lostCounter); // reset locks
            } else {
                notifyWinnerToAllClients(); // send game result to all clients
                closeAllClients(); // close all clients
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
    public int remoteGetInitUnits() throws RemoteException {
        return this.initUnits;
    }

    public Set<String> getUsers() {
        return this.playerMap.keySet();
    }

    public void addUser(String username) {
        if (playerMap.containsKey(username)) {
            throw new IllegalStateException("The Player" + username + " already joined");
        }
        if (playerMap.size() == capacity) {
            throw new IllegalStateException("The Game:" + name + " is already full");
        }
        playerMap.put(username, new Player(username));
    }

    @Override
    public String tryRegisterClient(String username, RemoteClient client) throws RemoteException {
        if (playerMap.containsKey(username)) {
            clientMap.put(username, client);
            return null;
        } else {
            return "Username didn't match";
        }
    }

    @Override
    public GameMap getGameMap() throws RemoteException {
        return gameMap;
    }

    @Override
    public Player getSelfStatus(String username) throws RemoteException {
        return playerMap.get(username);
    }

    @Override
    public String tryPickTerritoryGroupByName(String username, String groupName) throws RemoteException {
        try {
            gameMap.assignGroup(groupName, playerMap.get(username));
            return null;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Override
    public String tryPlaceUnitsOn(String username, String territory, int units) throws RemoteException {
        try {
            Territory t = gameMap.getTerritoryByName(territory);
            Player p = playerMap.get(username);
            int remainingUnits = initUnits - p.getTotalUnits();
            if (!t.getOwner().equals(p)) {
                return "Permission denied";
            } else if (units > remainingUnits) {
                return "Too many units";
            } else {
                t.increaseUnits(units);
                return null;
            }
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Override
    public String tryMoveOrder(String username, String src, String dest, int units) throws RemoteException {
        String response = null;
        try {
            MoveOrder mo = new MoveOrder(playerMap.get(username), gameMap.getTerritoryByName(src),
                    gameMap.getTerritoryByName(dest), units);
            ox.doOneMove(mo);
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public String tryAttackOrder(String username, String src, String dest, int units) throws RemoteException {
        String response = null;
        try {
            AttackOrder ao = new AttackOrder(playerMap.get(username), gameMap.getTerritoryByName(src),
                    gameMap.getTerritoryByName(dest), units);
            ox.pushCombat(ao);
        } catch (RuntimeException e) {
            response = e.getMessage();
        }
        return response;
    }

    @Override
    public void doCommitOrder(String username) throws RemoteException, InterruptedException, BrokenBarrierException {
        if (!playerMap.get(username).isLose()) {
            commitSignal.countDown();
            returnSignal.await(); // ready to next state
        }
    }

    @Override
    public boolean isGameOver() throws RemoteException {
        return (lostCounter == capacity - 1) ? true : false;
    }

    public void updateLostCounter() {
        int counter = 0;
        for (Player p : playerMap.values()) {
            if (p.isLose()) {
                ++counter;
            }
        }
        this.lostCounter = counter;
    }

    void notifyGameMapToLostClients() {
        for (String username : playerMap.keySet()) {
            try {
                if (playerMap.get(username).isLose()) {
                    clientMap.get(username).doDisplay(gameMap);
                    clientMap.get(username).doDisplay("Watcher Mode: Click [x] to exit");
                }
            } catch (RemoteException e) {
                /*
                 * RemoteException because the remote Client has disconnected, can be ignored
                 */
            }
        }
    }

    void notifyWinnerToAllClients() throws RemoteException {
        /*
         * Precondition: isGameOver() == true
         */
        String winner = "Not found";
        for (String username : playerMap.keySet()) {
            if (!playerMap.get(username).isLose()) {
                winner = username;
                break;
            }
        }
        for (RemoteClient client : clientMap.values()) {
            try {
                client.doDisplay("Game over! Winner is " + winner);
            } catch (RemoteException e) {
                /*
                 * RemoteException because the remote Client has disconnected, can be ignored
                 */
            }
        }
    }

    void closeAllClients() {
        for (RemoteClient client : clientMap.values()) {
            try {
                client.close();
            } catch (RemoteException e) {
                /*
                 * RemoteException because the remote Client has disconnected, can be ignored
                 */
            }
        }
    }
}
