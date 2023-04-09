package edu.duke.ece651.team7.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.duke.ece651.team7.server.repository.InMemoryUserRepo;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private InMemoryUserRepo inMemoryUserRepo;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        assertDoesNotThrow(() -> userService.createUser("player", "pswd"));
        verify(passwordEncoder,times(1)).encode("pswd");
    }
}
