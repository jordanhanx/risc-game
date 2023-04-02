package edu.duke.ece651.team7.logingate.controller;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    // @Autowired
    // private GameService gameService;

    // @GetMapping("/query")
    // public List<RemoteGameServer> requestUserGameList() {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // return gameService.getGamesByUsername(auth.getName());
    // }

    // @GetMapping("/join")
    // public RemoteGameServer requestJoinGame() throws RemoteException {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // return gameService.joinWatingGame(auth.getName());
    // }
}
