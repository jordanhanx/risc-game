package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.model.UserSession;

public class ReqNewGameController {

    public static Scene getScene() throws IOException {
        URL xmlResource = ReqNewGameController.class.getResource("/fxml/req-new-game-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 640, 480);
    }

    @FXML
    private TextField capacity;

    @FXML
    private TextField initUnits;

    @FXML
    public void clickOnCreate(ActionEvent event) throws IOException {
        requestNewGame("http://localhost:8080/api/riscgame/new",
                Integer.parseInt(capacity.getText()),
                Integer.parseInt(initUnits.getText()));
        Stage currStage = (Stage) capacity.getScene().getWindow();
        currStage.close();
    }

    @FXML
    public void clickOnCancel(ActionEvent event) throws IOException {
        Stage currStage = (Stage) capacity.getScene().getWindow();
        currStage.close();
    }

    public void requestNewGame(String url, int capacity, int initUnits) {
        // create a RestTemplate object
        RestTemplate restTemplate = new RestTemplate();

        // create a headers object with the session cookie
        HttpHeaders header = new HttpHeaders();
        header.add("Cookie", UserSession.getInstance().getSession());
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // create the request body as a MultiValueMap
        MultiValueMap<String, Integer> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("capacity", capacity);
        requestBody.add("initUnits", initUnits);

        // send a request with the session cookie in the headers and get the response
        HttpEntity<MultiValueMap<String, Integer>> requestEntity = new HttpEntity<>(requestBody, header);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new IllegalStateException(response.getStatusCode().getReasonPhrase());
        }
    }
}
