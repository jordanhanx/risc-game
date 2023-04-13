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

/**
 * The OrderMoveController class controls the "move order" page, which allows
 * players to create a move order to move armies from one territory to another.
 */
public class OrderMoveController implements Initializable {

    /**
     * Creates and returns the Scene for the OrderMove page.
     * 
     * @param server The RemoteGame object representing the game server.
     * @return The Scene object for the OrderMove page.
     * @throws IOException If there is an error loading the FXML file.
     */
    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = OrderMoveController.class.getResource("/fxml/order-move-page.fxml");
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

    /**
     * Constructs an OrderMoveController object.
     * 
     * @param server The RemoteGame object representing the game server.
     * @throws RemoteException If there is an error communicating with the server.
     */
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

    /**
     * Handles the click on the Move button.
     * 
     * @param action The ActionEvent object representing the click event.
     * @throws RemoteException          If there is an error communicating with the
     *                                  server.
     * @throws IllegalArgumentException If there is an invalid input from the user.
     */
    @FXML
    public void clickOnMove(ActionEvent action) throws RemoteException {
        String response = server.tryMoveOrder(UserSession.getInstance().getUsername(),
                srcSelector.getSelectionModel().getSelectedItem(), destSelector.getSelectionModel().getSelectedItem(),
                Integer.parseInt(levelSelector.getSelectionModel().getSelectedItem()),
                Integer.parseInt(numInputer.getText()));
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
    }

    /**
     * Handles the click on the Finish button.
     * 
     * @param action The ActionEvent object representing the click event.
     */
    @FXML
    public void clickOnFinish(ActionEvent action) {
        Stage currStage = (Stage) srcSelector.getScene().getWindow();
        currStage.close();
    }

}