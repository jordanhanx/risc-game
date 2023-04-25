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
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.MusicFactory;
import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

/**
 * This class controls the Attack Order page of the game, where the player can
 * choose a source territory, a target territory, a level, and the number of
 * armies to use in the attack order.
 */
public class OrderAttackController implements Initializable {

    /**
     * Returns a new Scene object containing the Attack Order UI layout.
     * 
     * @param server  the RemoteGame object representing the game server
     * @param gameMap the GameMap object representing the game map
     * @return a new Scene object containing the Attack Order UI layout
     * @throws IOException if the FXML file for the Attack Order UI layout cannot be
     *                     found
     */
    public static Scene getScene(RemoteGame server, GameMap gameMap, String srcName, String destName) throws IOException {
        URL xmlResource = OrderAttackController.class.getResource("/fxml/order-attack-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderAttackController(server, gameMap,srcName,destName));
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
    private String srcName;
    private String destName;

    /**
     * Constructs a new instance of OrderAttackController with the specified server
     * and game map.
     * 
     * @param server  the RemoteGame object representing the game server
     * @param gameMap the GameMap object representing the game map
     * @throws RemoteException if a remote method call fails
     */
    public OrderAttackController(RemoteGame server, GameMap gameMap, String srcName,String destName) throws RemoteException {
        this.server = server;
        Player self = server.getSelfStatus(UserSession.getInstance().getUsername());
        this.srcList = FXCollections.observableList(self.getTerritories().stream().map(t -> t.getName()).toList());
        this.destList = FXCollections.observableList(
                gameMap.getTerritories().stream().map(t -> t.getName()).filter((t) -> !srcList.contains(t)).toList());
        this.levList = FXCollections.observableArrayList();
        for (int lev = 0; lev < self.getCurrentMaxLevel().label; ++lev) {
            levList.add(String.valueOf(lev));
        }
        this.srcName=srcName;
        this.destName=destName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        srcSelector.setItems(srcList);
        destSelector.setItems(destList);
        levelSelector.setItems(levList);
        srcSelector.setValue(srcName);
        destSelector.setValue(destName);
    }

    /**
     * Handles the event when the player clicks the "Attack" button.
     * Calls the corresponding remote method on the game server with the selected
     * values.
     * 
     * @param action the event triggered by clicking the "Attack" button
     * @throws RemoteException          if a remote method call fails
     * @throws IllegalArgumentException if the attack order is invalid
     */
    @FXML
    public void clickOnAttack(ActionEvent action) throws RemoteException {
        String response = server.tryAttackOrder(UserSession.getInstance().getUsername(),false, 0, 
                srcSelector.getSelectionModel().getSelectedItem(), destSelector.getSelectionModel().getSelectedItem(),
                Integer.parseInt(levelSelector.getSelectionModel().getSelectedItem()),
                Integer.parseInt(numInputer.getText()));
        if (response != null) {
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }

        //set the attack music
        MediaPlayer attackPlayer = MusicFactory.createAttackPlayer();
        attackPlayer.play();
    }

    /**
     * Handles the event when the player clicks the "Finish" button.
     * 
     * @param action the triggered event.
     */
    @FXML
    public void clickOnFinish(ActionEvent action) {
        Stage currStage = (Stage) srcSelector.getScene().getWindow();
        currStage.close();
    }
}