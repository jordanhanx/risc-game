package edu.duke.ece651.team7.logingate.service;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    // @Autowired
    // private InMemoryGameRepo inMemoryGameRepo;

    // public RemoteGameServer joinWatingGame(String username) throws
    // RemoteException {
    // RemoteGameServer game = inMemoryGameRepo.joinFirstWaitingGame(username);
    // if (game == null) {
    // return inMemoryGameRepo.createNewGame(username);
    // } else {
    // return game;
    // }
    // }

    // public List<RemoteGameServer> getGamesByUsername(String username) {
    // return inMemoryGameRepo.getGameListByUser(username);
    // }
}
