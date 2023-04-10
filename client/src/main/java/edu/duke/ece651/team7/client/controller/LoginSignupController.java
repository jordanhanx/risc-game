package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

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
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    public static Scene getScene() throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/LoginSignup.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 640, 480);
    }

    @FXML
    public void onClickLogin() {
        doLogin();
    }

    @FXML
    public void onClickSignup() {
        doSignup();
        doLogin();
    }

    public void doLogin() {
        ResponseEntity<String> response = getHttpPostResponse(username.getText(), password.getText(),
                "http://localhost:8080/api/login");
        if (response.getStatusCode() != HttpStatus.FOUND) {
            throw new IllegalArgumentException(response.getBody());
        }
        UserSession.getInstance().setUsername(username.getText());
        UserSession.getInstance().setSession(response.getHeaders().getFirst("Set-Cookie"));
    }

    public void doSignup() {
        ResponseEntity<String> response = getHttpPostResponse(username.getText(), password.getText(),
                "http://localhost:8080/api/signup");
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // create the request entity with the headers and request body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // send the login request and get the response, do not need
        return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
    }

}
