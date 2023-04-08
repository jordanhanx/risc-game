package edu.duke.ece651.team7.server.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.duke.ece651.team7.server.repository.InMemoryGameRepo;
import edu.duke.ece651.team7.shared.GameDto;

/**
 * This class that provides services to manage games.
 */
@Service
public class GameService {

    @Value("${rmi.registry.port}")
    int port;

    @Autowired
    private InMemoryGameRepo inMemoryGameRepo;

    /**
     * Returns a list of all games.
     *
     * @return List<GameDto> A list of all games.
     */
    public List<GameDto> findAllGames() {
        return inMemoryGameRepo.getAllGames().stream()
                .map((g) -> new GameDto(g.getHost(), g.getPort(), g.getName(), g.getCapacity(), g.getInitUnits(),
                        g.getUsers()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of games that the given user has joined.
     *
     * @param username A string representing the username of the user.
     * @return List<GameDto> A list of games that the given user has joined.
     */
    public List<GameDto> findGamesByUser(String username) {
        return inMemoryGameRepo.getGamesByUser(username).stream()
                .map((g) -> new GameDto(g.getHost(), g.getPort(), g.getName(), g.getCapacity(), g.getInitUnits(),
                        g.getUsers()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new game with the specified capacity and initial units.
     *
     * @param username  A string representing the username of the user creating the
     *                  game.
     * @param capacity  An integer representing the capacity of the game.
     * @param initUnits An integer representing the initial units of the game.
     * @throws UnknownHostException If an unknown host exception occurs.
     * @throws RemoteException      If a remote exception occurs.
     */
    public void createNewGame(String username, int capacity, int initUnits)
            throws UnknownHostException, RemoteException {
        String hostname = InetAddress.getLocalHost().getHostName();
        inMemoryGameRepo.createNewGame(hostname, port, username, capacity, initUnits);
    }

    /**
     * Joins the specified game.
     *
     * @param username A string representing the username of the user joining the
     *                 game.
     * @param game     A string representing the name of the game to join.
     */
    public void joinGame(String username, String game) {
        inMemoryGameRepo.joinGame(username, game);
    }
}
