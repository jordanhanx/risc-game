package edu.duke.ece651.team7.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.duke.ece651.team7.server.repository.InMemoryUserRepo;

@Service
public class UserService {

    @Autowired
    private InMemoryUserRepo inMemoryUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(String username, String password) {
        UserDetails user = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        inMemoryUserRepo.createUser(user);
    }
}
