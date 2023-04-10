package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.GameDto;

public class HomePageController implements Initializable {

    public static Scene getScene() throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/home-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 1000, 680);
    }

    private ObservableList<GameDto> gameList = FXCollections.observableArrayList();

    @FXML
    private TableView<GameDto> allGamesTable = new TableView<>();
    @FXML
    private TableColumn<GameDto, String> allGamesName = new TableColumn<>();
    @FXML
    private TableColumn<GameDto, Integer> allGamesCapacity = new TableColumn<>();
    @FXML
    private TableColumn<GameDto, Integer> allGamesCurrentPlayers = new TableColumn<>();

    @FXML
    private TableView<GameDto> myGamesTable = new TableView<>();
    @FXML
    private TableColumn<GameDto, String> myGamesName = new TableColumn<>();
    @FXML
    private TableColumn<GameDto, Integer> myGamesCapacity = new TableColumn<>();
    @FXML
    private TableColumn<GameDto, Integer> myGamesCurrentPlayers = new TableColumn<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allGamesName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allGamesCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        allGamesCurrentPlayers.setCellValueFactory(data -> {
            int curr = data.getValue().getInGameUsers().size();
            return new SimpleIntegerProperty(curr).asObject();
        });
        allGamesTable.setItems(
                gameList.filtered(game -> !game.getInGameUsers().contains(UserSession.getInstance().getUsername())));

        myGamesName.setCellValueFactory(new PropertyValueFactory<>("name"));
        myGamesCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        myGamesCurrentPlayers.setCellValueFactory(data -> {
            int curr = data.getValue().getInGameUsers().size();
            return new SimpleIntegerProperty(curr).asObject();
        });

        updateGameList("http://localhost:8080/api/riscgame/all");
        myGamesTable.setItems(
                gameList.filtered(game -> game.getInGameUsers().contains(UserSession.getInstance().getUsername())));
    }

    @FXML
    public void clickOnNew(ActionEvent event) throws IOException {
        Scene newScene = ReqNewGameController.getScene();
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.showAndWait();
        updateGameList("http://localhost:8080/api/riscgame/all");
    }

    @FXML
    public void clickOnJoin(ActionEvent event) {
        String gamename = allGamesTable.getSelectionModel().getSelectedItem().getName();
        requestJoinGame("http://localhost:8080/api/riscgame/join", gamename);
        updateGameList("http://localhost:8080/api/riscgame/all");
    }

    @FXML
    public void clickOnEnter(ActionEvent event) {

    }

    @FXML
    public void clickOnRefresh(ActionEvent event) {
        updateGameList("http://localhost:8080/api/riscgame/all");
    }

    public void requestJoinGame(String url, String gamename) {
        // create a RestTemplate object
        RestTemplate restTemplate = new RestTemplate();

        // create a headers object with the session cookie
        HttpHeaders header = new HttpHeaders();
        header.add("Cookie", UserSession.getInstance().getSession());
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // create the request body as a MultiValueMap
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("gamename", gamename);

        // send a request with the session cookie in the headers and get the response
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, header);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new IllegalStateException(response.getStatusCode().getReasonPhrase());
        }
    }

    public void updateGameList(String url) {
        // create a RestTemplate object
        RestTemplate restTemplate = new RestTemplate();

        // create a headers object with the session cookie
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", UserSession.getInstance().getSession());

        // send a request with the session cookie in the headers and get the response
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<GameDto>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<GameDto>>() {
                });

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(response.getStatusCode().getReasonPhrase());
        }
        // update ObservableList
        gameList.clear();
        gameList.addAll(response.getBody());
    }

}
