package edu.duke.ece651.team7.server.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.duke.ece651.team7.server.dto.GameDto;
import edu.duke.ece651.team7.server.repository.InMemoryGameRepo;

@Service
public class GameService {

    @Value("${rmi.registry.port}")
    int port;

    @Autowired
    private InMemoryGameRepo inMemoryGameRepo;

    public List<GameDto> findAllGames() {
        return inMemoryGameRepo.getAllGames().stream()
                .map((g) -> new GameDto(g.getHost(), g.getPort(), g.getName(), g.getCapacity(), g.getInitUnits(),
                        g.getUsers()))
                .collect(Collectors.toList());
    }

    public List<GameDto> findGamesByUser(String username) {
        return inMemoryGameRepo.getGamesByUser(username).stream()
                .map((g) -> new GameDto(g.getHost(), g.getPort(), g.getName(), g.getCapacity(), g.getInitUnits(),
                        g.getUsers()))
                .collect(Collectors.toList());
    }

    public void createNewGame(String username, int capacity, int initUnits)
            throws UnknownHostException, RemoteException {
        String hostname = InetAddress.getLocalHost().getHostName();
        inMemoryGameRepo.createNewGame(hostname, port, username, capacity, initUnits);
    }

    public void joinGame(String username, String game) {
        inMemoryGameRepo.joinGame(username, game);
    }
}
