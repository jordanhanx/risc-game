package edu.duke.ece651.team7.server.controller;

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

import edu.duke.ece651.team7.server.service.GameService;
import edu.duke.ece651.team7.shared.GameDto;

/**
 * This is the controller class for the RISC game application. It provides
 * RESTful API endpoints to interact with the game service.
 */
@RestController
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    /**
     * Autowired instance of GameService to perform game-related operations.
     */
    @Autowired
    private GameService gameService;

    /**
     * RESTful API endpoint to request all the games.
     * 
     * @return List of GameDto objects representing all the games.
     */
    @GetMapping("/api/riscgame/all")
    public List<GameDto> requestAllGames() {
        return gameService.findAllGames();
    }

    /**
     * RESTful API endpoint to request games owned by the authenticated user.
     * 
     * @return List of GameDto objects representing the games owned by the
     *         authenticated user.
     */
    @GetMapping("/api/riscgame/my")
    public List<GameDto> requestGamesByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return gameService.findGamesByUser(auth.getName());
    }

    /**
     * RESTful API endpoint to create a new game with the specified capacity and
     * initial units.
     * 
     * @param capacity  The capacity of the game.
     * @param initUnits The initial number of units in the game.
     * @return ResponseEntity object representing the response of the request.
     * @throws UnknownHostException if the specified host is unknown.
     * @throws RemoteException      if there is an error with the remote method
     *                              invocation.
     */
    @PostMapping("/api/riscgame/new")
    public ResponseEntity<String> requestCreateNewGame(@RequestParam(value = "capacity") int capacity,
            @RequestParam(value = "initUnits") int initUnits) throws UnknownHostException, RemoteException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        gameService.createNewGame(auth.getName(), capacity, initUnits);
        String msg = "User[" + auth.getName() + "] created a new game";
        logger.info(msg);
        return new ResponseEntity<String>(msg, HttpStatus.CREATED);
    }

    /**
     * RESTful API endpoint to join a game with the specified name.
     * 
     * @param game The name of the game to join.
     * @return ResponseEntity object representing the response of the request.
     * @throws RemoteException if there is an error with the remote method
     *                         invocation.
     */
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
