package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import edu.duke.ece651.team7.client.model.UserSession;

public class LoginSignupController {

    public static Scene getScene() throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/login-signup-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 640, 480);
    }

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    public void clickOnLogin(ActionEvent event) throws IOException {
        doLogin("http://localhost:8080/api/login");
        loadGameLobbyPage();
    }

    @FXML
    public void clickOnSignup(ActionEvent event) throws IOException {
        doSignup("http://localhost:8080/api/signup");
        doLogin("http://localhost:8080/api/login");
        loadGameLobbyPage();
    }

    public void doLogin(String url) {
        ResponseEntity<String> response = getHttpPostResponse(username.getText(), password.getText(), url);
        if (response.getStatusCode() != HttpStatus.FOUND) {
            throw new IllegalArgumentException(response.getBody());
        }
        UserSession.getInstance().setUsername(username.getText());
        UserSession.getInstance().setSession(response.getHeaders().getFirst("Set-Cookie"));
    }

    public void doSignup(String url) {
        ResponseEntity<String> response = getHttpPostResponse(username.getText(), password.getText(), url);
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new IllegalArgumentException(response.getBody());
        }
    }

    public ResponseEntity<String> getHttpPostResponse(String username, String password, String apiUrl) {
        // create a RestTemplate object
        RestTemplate restTemplate = new RestTemplate();

        // create the request body as a MultiValueMap
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", username);
        requestBody.add("password", password);

        // set the Content-Type header to application/x-www-form-urlencoded
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // create the request entity with the headers and request body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, header);

        // send the login request and get the response, do not need
        return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
    }

    public void loadGameLobbyPage() throws IOException {
        Scene newScene = GameLobbyController.getScene();
        Stage primaryStage = (Stage) username.getScene().getWindow();
        primaryStage.setScene(newScene);
        primaryStage.show();
    }
}
