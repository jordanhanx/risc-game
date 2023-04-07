package edu.duke.ece651.team7.server.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class InMemoryUserRepoTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private InMemoryUserRepo inMemoryUserRepo;

    @Test
    public void testInit() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        assertFalse(inMemoryUserRepo.userExists("admin"));
        inMemoryUserRepo.init();
        assertTrue(inMemoryUserRepo.userExists("admin"));
        verify(passwordEncoder,times(1)).encode(anyString());
    }

    @Test
    public void testCreateUser() {
        UserDetails player1 = User.withUsername("player").password("pswd").roles("USER").build();
        UserDetails player2 = User.withUsername("player").password("pswd").roles("USER").build();
        assertDoesNotThrow(() -> inMemoryUserRepo.createUser(player1));
        assertThrows(IllegalStateException.class, () -> inMemoryUserRepo.createUser(player2));
        assertEquals(inMemoryUserRepo.loadUserByUsername("player"), player1);
    }
}