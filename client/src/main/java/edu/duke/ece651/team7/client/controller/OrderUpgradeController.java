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
import javafx.scene.media.MediaPlayer;

import edu.duke.ece651.team7.client.MusicFactory;
import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

/**
 * The OrderUpgradeController class represents the controller for the order
 * upgrade page.
 */
public class OrderUpgradeController implements Initializable {

    /**
     * Gets the JavaFX Scene for the order upgrade page with the given RemoteGame
     * server.
     * 
     * @param server The RemoteGame server to be used for the upgrade order.
     * @return The JavaFX Scene for the order upgrade page.
     * @throws IOException if there is an error loading the FXML file.
     */
    public static Scene getScene(RemoteGame server, String terrName) throws IOException {
        URL xmlResource = OrderUpgradeController.class.getResource("/fxml/order-upgrade-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderUpgradeController(server, terrName));
        return new Scene(loader.load(), 600, 400);
    }

    @FXML
    private ChoiceBox<String> terrSelector, srcLevSelector, destLevSelector;

    @FXML
    private TextField numInputer;

    private RemoteGame server;

    private String terrName;
    private ObservableList<String> terrList;
    private ObservableList<String> levList;

    /**
     * Constructs an OrderUpgradeController object with the given RemoteGame server.
     * The constructor
     * initializes the ObservableList of territories and levels that are displayed
     * in the ChoiceBoxes.
     * 
     * @param server The RemoteGame server to be used for the upgrade order.
     * @param terrName, if the territory is specified when the attack page is open
     * @throws RemoteException if there is an error with the RemoteGame server.
     */
    public OrderUpgradeController(RemoteGame server, String terrName) throws RemoteException {
        this.server = server;
        Player self = server.getSelfStatus(UserSession.getInstance().getUsername());
        this.terrList = FXCollections.observableList(self.getTerritories().stream().map(t -> t.getName()).toList());
        this.levList = FXCollections.observableArrayList();
        for (int lev = 0; lev < 7; ++lev) {
            levList.add(String.valueOf(lev));
        }
        this.terrName=terrName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        terrSelector.setItems(terrList);
        srcLevSelector.setItems(levList);
        destLevSelector.setItems(levList);

        terrSelector.setValue(terrName);
    }

    /**
     * Handles the event when the user clicks on the "Upgrade" button. It sends an
     * upgrade order to the RemoteGame server with the selected territory, source
     * level, destination level, and number of units. If there is an error with the
     * order, it throws an IllegalArgumentException with the error message.
     * 
     * @param action The event triggered by the user clicking on the "Upgrade"
     *               button.
     * @throws RemoteException          if there is an error with the RemoteGame
     *                                  server.
     * @throws IllegalArgumentException if there is an error with the upgrade order.
     */
    @FXML
    public void clickOnUpgrade(ActionEvent action) throws RemoteException {
        String response = server.tryUpgradeOrder(UserSession.getInstance().getUsername(),
                terrSelector.getSelectionModel().getSelectedItem(),
                Integer.parseInt(srcLevSelector.getSelectionModel().getSelectedItem()),
                Integer.parseInt(destLevSelector.getSelectionModel().getSelectedItem()),
                Integer.parseInt(numInputer.getText()));
        if (response != null) {
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }

        //set the upgrade music
        MediaPlayer upgradePlayer = MusicFactory.createUpgradePlayer();
        upgradePlayer.play();
    }

    /**
     * Handles the event when the user clicks on the "Finish" button. It closes the
     * current window.
     * 
     * @param action The event triggered by the user clicking on the "Finish"
     *               button.
     */
    @FXML
    public void clickOnFinish(ActionEvent action) {
        Stage currStage = (Stage) terrSelector.getScene().getWindow();
        currStage.close();
    }

}
