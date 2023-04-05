package edu.duke.ece651.team7.logingate.controller;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.duke.ece651.team7.logingate.dto.GameDto;
import edu.duke.ece651.team7.logingate.service.GameService;

@RestController
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @GetMapping("/api/riscgame/all")
    public List<GameDto> requestAllGames() {
        return gameService.findAllGames();
    }

    @GetMapping("/api/riscgame/my")
    public List<GameDto> requestGamesByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return gameService.findGamesByUser(auth.getName());
    }

    @PostMapping("/api/riscgame/new")
    public ResponseEntity<String> requestCreateNewGame(@RequestParam(value = "capacity") int capacity,
            @RequestParam(value = "initUnits") int initUnits) throws UnknownHostException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        gameService.createNewGame(auth.getName(), capacity, initUnits);
        String msg = "User[" + auth.getName() + "] created a new game";
        logger.info(msg);
        return new ResponseEntity<String>(msg, HttpStatus.CREATED);
    }

    @PostMapping("/api/riscgame/join")
    public ResponseEntity<String> requestJoinGame(@RequestParam(value = "gamename") String game)
            throws RemoteException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        gameService.joinGame(auth.getName(), game);
        String msg = "User[" + auth.getName() + "] joined game[" + game + "]";
        logger.info(msg);
        return new ResponseEntity<String>(msg, HttpStatus.ACCEPTED);
    }
}
