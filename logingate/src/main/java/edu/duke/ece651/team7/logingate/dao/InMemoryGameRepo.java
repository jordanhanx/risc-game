package edu.duke.ece651.team7.logingate.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryGameRepo {

    // private Map<RemoteGameServer, List<String>> games = new HashMap<>();

    // public RemoteGameServer createNewGame(String username) throws RemoteException
    // {
    // List<String> users = new ArrayList<>();
    // users.add(username);
    // RemoteGameServer game = new RemoteGameServerImpl();
    // games.put(game, users);
    // return game;
    // }

    // public RemoteGameServer joinFirstWaitingGame(String username) {
    // for (RemoteGameServer g : games.keySet()) {
    // List<String> users = games.get(g);
    // if (!users.contains(username) && users.size() < 2) {
    // users.add(username);
    // return g;
    // }
    // }
    // return null;
    // }

    // public List<RemoteGameServer> getGameListByUser(String username) {
    // return games.entrySet()
    // .stream()
    // .filter(entry -> entry.getValue().contains(username))
    // .map(Map.Entry::getKey)
    // .collect(Collectors.toList());
    // }
}
