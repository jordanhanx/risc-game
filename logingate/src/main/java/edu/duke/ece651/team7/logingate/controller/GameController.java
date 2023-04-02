package edu.duke.ece651.team7.logingate.controller;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.duke.ece651.team7.logingate.dto.GameDto;
import edu.duke.ece651.team7.logingate.model.GameEntity;
import edu.duke.ece651.team7.logingate.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/query/all")
    public List<GameDto> requestAllGames() {
        return gameService.findAllGames();
    }

    @GetMapping("/query/self")
    public List<GameDto> requestGamesByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return gameService.findGamesByUser(auth.getName());
    }

    @PostMapping("/new")
    public String requestCreateNewGame(@RequestParam(value = "capacity") int capacity,
            @RequestParam(value = "initUnits") int initUnits) throws UnknownHostException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        gameService.createNewGame(auth.getName(), capacity, initUnits);
        return "creat a new game";
    }

    @PostMapping("/join")
    public String requestJoinGame(@RequestParam(value = "gamename") String game) throws RemoteException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        gameService.joinGame(auth.getName(), game);
        return "joined the game:" + game;
    }
}
