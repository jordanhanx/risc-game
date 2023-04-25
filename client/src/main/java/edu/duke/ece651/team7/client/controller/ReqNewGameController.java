package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;

import javafx.collections.ObservableList;
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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import java.util.HashMap;

import edu.duke.ece651.team7.client.model.UserSession;

/**
 * The ReqNewGameController class is responsible for handling requests for
 * creating a new game.
 */
public class ReqNewGameController {

    /**
     * Returns the scene for the request new game page.
     * 
     * @return the scene for the request new game page.
     * @throws IOException if there is an error loading the FXML file for the scene.
     */
    public static Scene getScene() throws IOException {
        URL xmlResource = ReqNewGameController.class.getResource("/fxml/req-new-game-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 640, 480);
    }
    @FXML
    ComboBox<String> capacity;

    @FXML
    TextField initUnits;

    RestTemplate restTemplate = new RestTemplate();


    @FXML private ImageView player0;
    @FXML private ImageView player1;
    @FXML private ImageView player2;
    @FXML private ImageView player3;
    private HashMap<Integer, ImageView> ImageViewMap;

    @FXML
    public void initialize(){
        setUpImageView();
        ObservableList<String> ChoiceNumber = FXCollections.observableArrayList("2","3","4");
        capacity.setItems(ChoiceNumber);

    }

    private void setUpImageView(){
        ImageViewMap = new HashMap<>();
        ImageViewMap.put(0,player0);
        ImageViewMap.put(1,player1);
        ImageViewMap.put(2,player2);
        ImageViewMap.put(3,player3);
    }

    @FXML
    public void DisplayImage(ActionEvent event){
        int playerNum = Integer.parseInt(this.capacity.getValue());
        for(int i = 0; i < 4; i++){
            if(i < playerNum){
                Image validImage = new Image(getClass().getResourceAsStream("/image/player" + i + ".png"));
                ImageViewMap.get(i).setImage(validImage);
            }
            else{
                Image lockedImage = new Image(getClass().getResourceAsStream("/image/player" + i + "_lock.png"));
                ImageViewMap.get(i).setImage(lockedImage);
            }
        }
    }


    /**
     * Handles the click event for the Create button.
     * 
     * @param event the action event.
     */
    @FXML
    public void clickOnCreate(ActionEvent event) {
        requestNewGame("http://" + UserSession.getInstance().getHost() + ":" + UserSession.getInstance().getPort()
                + "/api/riscgame/new",
                Integer.parseInt(capacity.getValue()),
                Integer.parseInt(initUnits.getText()));
        Stage currStage = (Stage) capacity.getScene().getWindow();
        currStage.close();
    }

    /**
     * Handles the click event for the Cancel button.
     * 
     * @param event the action event.
     */
    @FXML
    public void clickOnCancel(ActionEvent event) {
        Stage currStage = (Stage) capacity.getScene().getWindow();
        currStage.close();
    }

    /**
     * Sends a request to the server to create a new game.
     * 
     * @param url       the URL for the new game API.
     * @param capacity  the capacity of the game.
     * @param initUnits the number of initial units in the game.
     * @throws IllegalStateException if the response from the server indicates that
     *                               the request was not successful.
     */
    public void requestNewGame(String url, int capacity, int initUnits) {
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
