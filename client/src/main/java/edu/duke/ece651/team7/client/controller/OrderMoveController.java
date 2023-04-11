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

public class OrderMoveController implements Initializable {

    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/pick-group-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderMoveController(server));
        return new Scene(loader.load(), 600, 400);
    }

    @FXML
    private ChoiceBox<String> srcSelector, destSelector, levelSelector;

    @FXML
    private TextField numInputer;

    private RemoteGame server;
    private ObservableList<String> terrList;
    private ObservableList<String> levList;

    public OrderMoveController(RemoteGame server) throws RemoteException {
        this.server = server;
        Player self = server.getSelfStatus(UserSession.getInstance().getUsername());
        this.terrList = FXCollections.observableList(self.getTerritories().stream().map(t -> t.getName()).toList());
        this.levList = FXCollections.observableArrayList();
        for (int lev = 0; lev < self.getCurrentMaxLevel().label; ++lev) {
            levList.add(String.valueOf(lev));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        srcSelector.setItems(terrList);
        destSelector.setItems(terrList);
        levelSelector.setItems(levList);
    }

    @FXML
    public void clickOnMove(ActionEvent action) throws RemoteException {
        String response = server.tryMoveOrder(UserSession.getInstance().getUsername(),
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
