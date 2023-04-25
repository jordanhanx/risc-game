package edu.duke.ece651.team7.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;

import javafx.scene.control.ComboBox;
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
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class ReqNewGameControllerTest {
    @Mock
    private RestTemplate restTemplate;

    private ComboBox capacity;
    private TextField initUnits;
    private ReqNewGameController reqNewGameController;

    @Start
    public void start(Stage stage) throws IOException {

        URL xmlResource = ReqNewGameController.class.getResource("/fxml/req-new-game-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        Scene scene = new Scene(loader.load(), 640, 480);
        reqNewGameController = loader.getController();

        reqNewGameController.restTemplate = restTemplate;
        reqNewGameController.capacity = capacity;
        reqNewGameController.initUnits = initUnits;
    }

    @Test
    public void test_getScene() throws IOException {
        assertTrue(ReqNewGameController.getScene() instanceof Scene);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_requestNewGame() {
        ResponseEntity<String> response201 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                HttpStatus.CREATED);
        ResponseEntity<String> response403 = new ResponseEntity<String>("Hello World", new HttpHeaders(),
                HttpStatus.FORBIDDEN);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(response201, response403);
        assertDoesNotThrow(() -> reqNewGameController.requestNewGame("url", 2, 10));
        assertThrows(IllegalStateException.class, () -> reqNewGameController.requestNewGame("url", 1, 10));
    }

}
