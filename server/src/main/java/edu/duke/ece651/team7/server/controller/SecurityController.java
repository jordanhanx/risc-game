package edu.duke.ece651.team7.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.duke.ece651.team7.server.service.UserService;

/**
 * This class serves as a RESTful API controller for the user authentication and
 * authorization operations.
 */
@RestController
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private UserService userService;

    /**
     * Returns a welcome message to the users
     * 
     * @return A welcome message.
     */
    @GetMapping("/")
    public String requestIndex() {
        return "Welcome this endpoint is not secure";
    }

    /**
     * Returns an admin page to the authorized users.
     * 
     * @return An admin page.
     */
    @GetMapping("/admin")
    public String requestAll() {
        return "Admin page";
    }

    /**
     * Registers a new user with the given username and password.
     * 
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @return A response entity indicating the success or failure of the user
     *         creation request.
     */
    @PostMapping("/api/signup")
    public ResponseEntity<String> requestCreateUser(@RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        userService.createUser(username, password);
        String msg = "New user[" + username + "] registered";
        logger.info(msg);
        return new ResponseEntity<String>(msg, HttpStatus.CREATED);
    }
}
