package edu.duke.ece651.team7.logingate.repository;

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
import edu.duke.ece651.team7.server.Server;

@Repository
public class InMemoryGameRepo {

    @Autowired
    private Registry registry;

    private AtomicLong gameCounter = new AtomicLong();
    private Map<String, GameEntity> allGames = new ConcurrentHashMap<>();

    public List<GameEntity> getAllGames() {
        return allGames.values().stream().collect(Collectors.toList());
    }

    public List<GameEntity> getGamesByUser(String username) {
        return allGames.values().stream()
                .filter(e -> e.getInGameUsers().contains(username))
                .collect(Collectors.toList());
    }

    public void createNewGame(String host, int port, String username, int capacity, int initUnits) {
        String gameName = "RiscGame" + gameCounter.addAndGet(1);

        Runnable runnable = () -> {
            try {
                Server server = new Server(System.out, port, capacity, initUnits) {
                    @Override
                    protected void bindGameOnPort(int port) throws RemoteException {
                        registry.rebind(gameName, this);
                    }
                };
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                allGames.remove(gameName);
            }
        };

        GameEntity newGameEntity = new GameEntity(host, port, gameName, capacity, initUnits, runnable);
        newGameEntity.addUser(username);
        allGames.put(gameName, newGameEntity);
    }

    public void joinGame(String username, String game) {
        if (!allGames.containsKey(game)) {
            throw new IllegalArgumentException("Game:" + game + "doesn't exist");
        }
        allGames.get(game).addUser(username);
    }

    public void removeGame(String game) {
        if (!allGames.containsKey(game)) {
            throw new IllegalArgumentException("Game:" + game + "doesn't exist");
        }
        allGames.remove(game);
    }
}
