package edu.duke.ece651.team7.server.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepo extends InMemoryUserDetailsManager {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();
        this.createUser(admin);
    }

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new IllegalStateException("User[" + user.getUsername() + "] already exists");
        } else {
            super.createUser(user);
        }
    }
}
