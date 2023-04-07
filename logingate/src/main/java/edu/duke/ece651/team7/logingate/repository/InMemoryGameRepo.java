package edu.duke.ece651.team7.logingate.repository;

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

import edu.duke.ece651.team7.logingate.model.GameEntity;

@Repository
public class InMemoryGameRepo {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryGameRepo.class);

    @Autowired
    private Registry registry;

    private AtomicLong gameCounter = new AtomicLong();
    private Map<String, GameEntity> allGames = new ConcurrentHashMap<>();

    public List<GameEntity> getAllGames() {
        return allGames.values().stream().collect(Collectors.toList());
    }

    public List<GameEntity> getGamesByUser(String username) {
        return allGames.values().stream()
                .filter(e -> e.getUsers().contains(username))
                .collect(Collectors.toList());
    }

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
                allGames.remove(gameName);
            }
        }).start();
    }

    public void joinGame(String username, String game) {
        if (!allGames.containsKey(game)) {
            throw new IllegalStateException("Game:" + game + "doesn't exist");
        } else {
            allGames.get(game).addUser(username);
        }
    }
}
