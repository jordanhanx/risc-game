package edu.duke.ece651.team7.logingate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.duke.ece651.team7.logingate.service.UserService;

@RestController
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String requestIndex() {
        return "Welcome this endpoint is not secure";
    }

    @GetMapping("/admin")
    public String requestAll() {
        return "Admin page";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> requestCreateUser(@RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        userService.createUser(username, password);
        String msg = "New user[" + username + "] registered";
        logger.info(msg);
        return new ResponseEntity<String>(msg, HttpStatus.CREATED);
    }
}
