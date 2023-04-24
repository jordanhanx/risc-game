package edu.duke.ece651.team7.client.controller;

import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;
import java.util.stream.Collectors;

import edu.duke.ece651.team7.client.MusicFactory;
import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

import javax.swing.*;

public class OrderAllyController implements Initializable {

    public static Scene getScene(RemoteGame server, GameMap gameMap) throws IOException {
        URL xmlResource = OrderAllyController.class.getResource("/fxml/order-ally-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderAllyController(server, gameMap));
        return new Scene(loader.load(), 600, 400);
    }

    private RemoteGame server;
    @FXML
    private ChoiceBox<String> allySelector;
    private ObservableList<String> allyList;

    public OrderAllyController(RemoteGame server, GameMap gameMap) throws RemoteException{
        this.server=server;
//        this.allyList=FXCollections.observableArrayList();
//        Set<String> playerSet = new TreeSet<>();
//        for (Territory t : gameMap.getTerritories()) {
//            playerSet.add(t.getOwner().getName());
//        }
//        for(String p: playerSet){
//            allyList.add(p);
//        }
        this.allyList = FXCollections.observableArrayList(gameMap.getTerritories().stream()
                        .map(Territory::getOwner)
                        .map(Player::getName)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allySelector.setItems(allyList);
    }

    @FXML
    public void clickOnAlly(ActionEvent action) throws RemoteException{
//        String response = server.tryAllianceOrder(UserSession.getInstance().getUsername(),
//                allySelector.getValue()
//                );
//
        String response = null;
        if (response != null) {
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }
//
//        //set the ally sound
//        MediaPlayer movePlayer = MusicFactory.createMovePlayer();
//        movePlayer.play();
    }

    @FXML
    public void clickOnFinish(ActionEvent action){
        Stage currStage = (Stage) allySelector.getScene().getWindow();
        currStage.close();
    }

}
