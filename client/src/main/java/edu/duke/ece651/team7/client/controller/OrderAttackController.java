package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

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

import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

public class OrderAttackController implements Initializable {

    public static Scene getScene(RemoteGame server, GameMap gameMap) throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/pick-group-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderAttackController(server, gameMap));
        return new Scene(loader.load(), 600, 400);
    }

    @FXML
    private ChoiceBox<String> srcSelector, destSelector, levelSelector;

    @FXML
    private TextField numInputer;

    private RemoteGame server;
    private ObservableList<String> srcList;
    private ObservableList<String> destList;
    private ObservableList<String> levList;

    public OrderAttackController(RemoteGame server, GameMap gameMap) throws RemoteException {
        this.server = server;
        Player self = server.getSelfStatus(UserSession.getInstance().getUsername());
        this.srcList = FXCollections.observableList(self.getTerritories().stream().map(t -> t.getName()).toList());
        this.destList = FXCollections.observableList(
                gameMap.getTerritories().stream().map(t -> t.getName()).filter((t) -> !srcList.contains(t)).toList());
        this.levList = FXCollections.observableArrayList();
        for (int lev = 0; lev < self.getCurrentMaxLevel().label; ++lev) {
            levList.add(String.valueOf(lev));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        srcSelector.setItems(srcList);
        destSelector.setItems(destList);
        levelSelector.setItems(levList);
    }

    @FXML
    public void clickOnAttack(ActionEvent action) throws RemoteException {
        String response = server.tryAttackOrder(UserSession.getInstance().getUsername(),
                srcSelector.getSelectionModel().getSelectedItem(), destSelector.getSelectionModel().getSelectedItem(),
                levelSelector.getSelectionModel().getSelectedItem(), Integer.parseInt(numInputer.getText()));
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
    }

    @FXML
    public void clickOnFinish(ActionEvent action) {
        Stage currStage = (Stage) srcSelector.getScene().getWindow();
        currStage.close();
    }
}