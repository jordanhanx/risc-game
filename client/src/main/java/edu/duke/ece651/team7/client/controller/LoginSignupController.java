package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
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

/**
 * This class serves as a controller for login and signup page.
 */
public class LoginSignupController {

    /**
     * The getScene method loads an FXML file for the login-signup-page and
     * returns a new Scene object.
     * 
     * @throws IOException if the FXML file for the login-signup-page is not found.
     */
    public static Scene getScene() throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/login-signup-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 640, 480);
    }

    @FXML
    private TextField host, port, username;
    @FXML
    PasswordField password;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * The clickOnLogin method is event handlers that is called when the user clicks
     * the login button on the page. It calls the doLogin method, passing in the
     * user input values.
     * 
     * @param event the event that triggered the login action.
     * @throws IOException if the game lobby page cannot be loaded.
     */
    @FXML
    public void clickOnLogin(ActionEvent event) throws IOException {
        doLogin(host.getText(), port.getText(), username.getText(), password.getText());
        loadGameLobbyPage();
    }

    /**
     * The clickOnSignup method is event handlers that is called when the user
     * clicks the signup button on the page. It calls the doSignup method, passing
     * in the user input values.
     * 
     * @param event the event that triggered the signup action.
     * @throws IOException if the game lobby page cannot be loaded.
     */
    @FXML
    public void clickOnSignup(ActionEvent event) throws IOException {
        doSignup(host.getText(), port.getText(), username.getText(), password.getText());
        doLogin(host.getText(), port.getText(), username.getText(), password.getText());
        loadGameLobbyPage();
    }

    /**
     * The doLogin() method sends a POST request to an login API endpoint with
     * the username and password as form data. If login successfully, it will store
     * the user information in the singleton UserSession object.
     * 
     * @param host     the hostname of the backend API.
     * @param port     the port number of the backend API.
     * @param username the username of the user.
     * @param password the password of the user.
     * @throws IllegalArgumentException if the login fails due to incorrect
     *                                  credentials or other errors.
     */
    public void doLogin(String host, String port, String username, String password) {
        String url = "http://" + host + ":" + port + "/api/login";
        ResponseEntity<String> response = getHttpPostResponse(username, password, url);
        if (response.getStatusCode() != HttpStatus.FOUND) {
            throw new IllegalArgumentException(response.getBody());
        }
        UserSession.getInstance().setHost(host);
        UserSession.getInstance().setPort(port);
        UserSession.getInstance().setUsername(username);
        UserSession.getInstance().setSession(response.getHeaders().getFirst("Set-Cookie"));
    }

    /**
     * The doSignup() method sends a POST request to an signup API endpoint with
     * the username and password as form data.
     * 
     * @param host     the hostname of the backend API.
     * @param port     the port number of the backend API.
     * @param username the username of the user.
     * @param password the password of the user.
     * @throws IllegalArgumentException if the registration fails due to errors such
     *                                  as the username already being taken.
     */
    public void doSignup(String host, String port, String username, String password) {
        String url = "http://" + host + ":" + port + "/api/signup";
        ResponseEntity<String> response = getHttpPostResponse(username, password, url);
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new IllegalArgumentException(response.getBody());
        }
    }

    /**
     * Sends an HTTP POST request to the backend API with the user's credentials and
     * returns the response.
     * 
     * @param username the username of the user.
     * @param password the password of the user.
     * @param apiUrl   the URL of the backend API.
     * @return the HTTP response from the backend API.
     */
    public ResponseEntity<String> getHttpPostResponse(String username, String password, String apiUrl) {

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

    /**
     * Loads the game lobby page and displays it on the current window
     * 
     * @throws IOException if the FXML file for the game-lobby-page is not found.
     */
    public void loadGameLobbyPage() throws IOException {
        Scene newScene = GameLobbyController.getScene();
        Stage primaryStage = (Stage) username.getScene().getWindow();
        primaryStage.setScene(newScene);
        primaryStage.show();
    }
}
