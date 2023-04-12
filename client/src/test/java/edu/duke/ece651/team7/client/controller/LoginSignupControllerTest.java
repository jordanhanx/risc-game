package edu.duke.ece651.team7.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import edu.duke.ece651.team7.client.model.UserSession;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class LoginSignupControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LoginSignupController loginSignupController;

    private TextField username;
    private PasswordField password;

    @Start
    private void start(Stage stage) {
        username = new TextField();
        password = new PasswordField();
        username.setText("username");
        password.setText("password");
    }

    @Test
    public void test_getScene() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> LoginSignupController.getScene());
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_doLogin() {
        Platform.runLater(() -> {
            ResponseEntity<String> response302 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                    HttpStatus.FOUND);
            ResponseEntity<String> response403 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                    HttpStatus.FORBIDDEN);
            when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                    .thenReturn(response302, response403);
            assertDoesNotThrow(() -> loginSignupController.doLogin("url", "username", "password"));
            assertEquals("username", UserSession.getInstance().getUsername());
            assertThrows(IllegalArgumentException.class,
                    () -> loginSignupController.doSignup("url", "username", "password"));
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_doSignup() {
        Platform.runLater(() -> {
            ResponseEntity<String> response201 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                    HttpStatus.CREATED);
            ResponseEntity<String> response403 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                    HttpStatus.FORBIDDEN);
            when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                    .thenReturn(response201, response403);
            assertDoesNotThrow(() -> loginSignupController.doSignup("url", "username", "password"));
            assertThrows(IllegalArgumentException.class,
                    () -> loginSignupController.doSignup("url", "username", "password"));
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_getHttpPostResponse() {
        Platform.runLater(() -> {
            ResponseEntity<String> response = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                    HttpStatus.OK);
            when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                    .thenReturn(response);
            assertEquals(response, loginSignupController.getHttpPostResponse("username", "password", "apiUrl"));
        });
    }

    @Test
    public void test_loadGameLobbyPage() {
        Platform.runLater(() -> assertDoesNotThrow(() -> loginSignupController.loadGameLobbyPage()));
    }

}
