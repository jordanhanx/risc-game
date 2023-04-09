package edu.duke.ece651.team7.server.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.duke.ece651.team7.server.model.GameEntity;

/**
 * This class represents an in-memory repository for RISC game entities.
 */
@Repository
public class InMemoryGameRepo {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryGameRepo.class);

    @Autowired
    private Registry registry;

    private AtomicLong gameCounter = new AtomicLong();
    private Map<String, GameEntity> allGames = new ConcurrentHashMap<>();

    /**
     * Returns a list of all game entities stored in the repository.
     * 
     * @return a list of all game entities
     */
    public List<GameEntity> getAllGames() {
        return allGames.values().stream().collect(Collectors.toList());
    }

    /**
     * Returns a list of all game entities stored in the repository that have the
     * specified user as a player.
     * 
     * @param username the username of the player
     * @return a list of all game entities the player is a part of
     */
    public List<GameEntity> getGamesByUser(String username) {
        return allGames.values().stream()
                .filter(e -> e.getUsers().contains(username))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new game entity with the specified parameters, adds the user as the
     * host and player, and starts the game.
     * 
     * @param host      the host of the game
     * @param port      the port number of the game
     * @param username  the username of the host and player
     * @param capacity  the capacity of the game
     * @param initUnits the number of initial units in the game
     * @throws RemoteException if a communication-related exception occurs
     */
    public void createNewGame(String host, int port, String username, int capacity, int initUnits)
            throws RemoteException {
        String gameName = "RiscGame" + gameCounter.addAndGet(1);
        GameEntity newGameEntity = new GameEntity(host, port, gameName, capacity, initUnits);
        newGameEntity.addUser(username);
        registry.rebind(gameName, newGameEntity);
        allGames.put(gameName, newGameEntity);
        // start
        new Thread(() -> {
            try {
                newGameEntity.start();
            } catch (Exception e) {
                logger.error("Game[" + gameName + "] aborted because: ", e);
            } finally {
                // remove GameEntity when finished
                allGames.remove(gameName);
            }
        }).start();
    }

    /**
     * Adds the specified user to the specified game entity.
     * 
     * @param username the username of the user to add
     * @param game     the name of the game entity to add the user to
     * @throws IllegalStateException if the specified game entity does not exist in
     *                               the repository
     */
    public void joinGame(String username, String game) {
        if (!allGames.containsKey(game)) {
            throw new IllegalStateException("Game:" + game + "doesn't exist");
        } else {
            allGames.get(game).addUser(username);
        }
    }
}
