package edu.duke.ece651.team7.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import edu.duke.ece651.team7.client.model.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class LoginSignupControllerTest {

        @Mock
        private RestTemplate restTemplate;

        private LoginSignupController loginSignupController;

        @Start
        public void start(Stage stage) throws IOException {

                URL xmlResource = LoginSignupController.class.getResource("/fxml/login-signup-page.fxml");
                FXMLLoader loader = new FXMLLoader(xmlResource);
                Scene scene = new Scene(loader.load(), 640, 480);
                loginSignupController = loader.getController();

                loginSignupController.restTemplate = restTemplate;
        }

        @Test
        public void test_getScene() throws IOException {
                assertTrue(LoginSignupController.getScene() instanceof Scene);
        }

        @Test
        @SuppressWarnings("unchecked")
        public void test_doLogin() {
                ResponseEntity<String> response302 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                                HttpStatus.FOUND);
                ResponseEntity<String> response403 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                                HttpStatus.FORBIDDEN);
                when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                                .thenReturn(response302, response403);
                assertDoesNotThrow(() -> loginSignupController.doLogin("anyhost", "0", "username", "password"));
                assertEquals("username", UserSession.getInstance().getUsername());
                assertThrows(IllegalArgumentException.class,
                                () -> loginSignupController.doSignup("anyhost", "0", "username", "password"));
        }

        @Test
        @SuppressWarnings("unchecked")
        public void test_doSignup() {
                ResponseEntity<String> response201 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                                HttpStatus.CREATED);
                ResponseEntity<String> response403 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                                HttpStatus.FORBIDDEN);
                when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                                .thenReturn(response201, response403);
                assertDoesNotThrow(() -> loginSignupController.doSignup("anyhost", "0", "username", "password"));
                assertThrows(IllegalArgumentException.class,
                                () -> loginSignupController.doSignup("anyhost", "0", "username", "password"));
        }

        @Test
        @SuppressWarnings("unchecked")
        public void test_getHttpPostResponse() {
                ResponseEntity<String> response = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                                HttpStatus.OK);
                when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                                .thenReturn(response);
                assertEquals(response, loginSignupController.getHttpPostResponse("username", "password", "apiUrl"));
        }

}
