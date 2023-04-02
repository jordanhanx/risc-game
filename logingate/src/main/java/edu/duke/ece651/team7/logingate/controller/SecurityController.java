package edu.duke.ece651.team7.logingate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.duke.ece651.team7.logingate.dto.UserForm;
import edu.duke.ece651.team7.logingate.service.UserService;

@RestController
public class SecurityController {

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
    public String requestCreateUser(@RequestBody UserForm userForm) {
        userService.createUser(userForm.getUsername(), userForm.getPassword());
        return "user added to system";
    }
}
