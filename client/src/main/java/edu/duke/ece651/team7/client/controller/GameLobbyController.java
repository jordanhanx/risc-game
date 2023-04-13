package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

/**
 * This class is responsible for the game lobby page of a Risk-like game. The
 * class defines a number of instance variables, including tables and columns to
 * display information about available games and the user's games.
 */
public class GameLobbyController implements Initializable {

    /**
     * The getScene method loads an FXML file for the game-lobby-page and
     * returns a new Scene object.
     */
    public static Scene getScene() throws IOException {
        URL xmlResource = GameLobbyController.class.getResource("/fxml/game-lobby-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 1000, 680);
    }

    @FXML
    TableView<GameDto> allGamesTable = new TableView<>();
    @FXML
    TableColumn<GameDto, String> allGamesName = new TableColumn<>();
    @FXML
    TableColumn<GameDto, Integer> allGamesCapacity = new TableColumn<>();
    @FXML
    TableColumn<GameDto, Integer> allGamesCurrentPlayers = new TableColumn<>();

    @FXML
    TableView<GameDto> myGamesTable = new TableView<>();
    @FXML
    TableColumn<GameDto, String> myGamesName = new TableColumn<>();
    @FXML
    TableColumn<GameDto, Integer> myGamesCapacity = new TableColumn<>();
    @FXML
    TableColumn<GameDto, Integer> myGamesCurrentPlayers = new TableColumn<>();

    ObservableList<GameDto> gameList = FXCollections.observableArrayList();

    RestTemplate restTemplate = new RestTemplate();

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

        updateGameList("http://" + UserSession.getInstance().getHost() + ":" + UserSession.getInstance().getPort()
                + "/api/riscgame/all");
        myGamesTable.setItems(
                gameList.filtered(game -> game.getInGameUsers().contains(UserSession.getInstance().getUsername())));
    }

    /**
     * The clickOnNew method is called when the user clicks the "NewGame" button. It
     * creates a new game by opening a popup window that allows the user to enter
     * the number of players and initial units per player. It then sends an HTTP
     * POST request to the server to create the new game and updates the list of
     * games.
     * 
     * @param event the event that triggered the action.
     */
    @FXML
    public void clickOnNew(ActionEvent event) throws IOException {
        Scene newScene = ReqNewGameController.getScene();
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.initOwner(allGamesTable.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.showAndWait();
        updateGameList("http://" + UserSession.getInstance().getHost() + ":" + UserSession.getInstance().getPort()
                + "/api/riscgame/all");
    }

    /**
     * The clickOnJoin method is called when the user clicks the "Join" button. It
     * sends an HTTP POST request to the server to join the selected game and
     * updates the list of games.
     * 
     * @param event the event that triggered the action.
     */
    @FXML
    public void clickOnJoin(ActionEvent event) {
        String gamename = allGamesTable.getSelectionModel().getSelectedItem().getName();
        requestJoinGame("http://" + UserSession.getInstance().getHost() + ":" + UserSession.getInstance().getPort()
                + "/api/riscgame/join", gamename);
        updateGameList("http://" + UserSession.getInstance().getHost() + ":" + UserSession.getInstance().getPort()
                + "/api/riscgame/all");
    }

    /**
     * The clickOnEnter method is called when the user clicks the "Enter" button. It
     * gets the selected game from the user's game table, looks up the server for
     * the game, and creates a new scene to display the game. The scene depends on
     * the game phase.
     * 
     * @param event the event that triggered the action.
     */
    @FXML
    public void clickOnEnter(ActionEvent event) throws NotBoundException, IOException {
        String host = myGamesTable.getSelectionModel().getSelectedItem().getHost();
        int port = myGamesTable.getSelectionModel().getSelectedItem().getPort();
        String gamename = myGamesTable.getSelectionModel().getSelectedItem().getName();
        RemoteGame server = (RemoteGame) LocateRegistry.getRegistry(host, port).lookup(gamename);
        RemoteGame.GamePhase phase = server.getGamePhase(UserSession.getInstance().getUsername());
        Scene newScene = null;
        if (phase == RemoteGame.GamePhase.PICK_GROUP) {
            newScene = PickGroupController.getScene(server);
        } else if (phase == RemoteGame.GamePhase.PLACE_UNITS) {
            newScene = PlaceUnitsController.getScene(server);
        } else {
            newScene = PlayGameController.getScene(server);
        }
        Stage gameStage = new Stage();
        gameStage.setScene(newScene);
        gameStage.show();
    }

    /**
     * The clickOnRefresh method is called when the user clicks the "Refresh"
     * button. It sends an HTTP GET request to the server to update the list of
     * games.
     * 
     * @param event the event that triggered the action.
     */
    @FXML
    public void clickOnRefresh(ActionEvent event) {
        updateGameList("http://" + UserSession.getInstance().getHost() + ":" + UserSession.getInstance().getPort()
                + "/api/riscgame/all");
    }

    /**
     * The requestJoinGame method is called by clickOnJoin to send an HTTP POST
     * request to the server to join the game.
     * 
     * @param url      is the join a game service endpoint.
     * @param gamename is the target game.
     */
    public void requestJoinGame(String url, String gamename) {
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

    /**
     * The updateGameList method sends an HTTP GET request to the server to get a
     * list of all available games, and updates the list of games in the GUI.
     * 
     * @param url is the query all games service endpoint.
     */
    public void updateGameList(String url) {
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
