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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

/**
 * The PickGroupController class implements the functionality for the "pick
 * group" screen of the RISC game. This screen allows the user to select a group
 * to join from the list of available groups. Once a group is selected, the user
 * can confirm their choice and proceed to the "place units" scene.
 */
public class PickGroupController implements Initializable {
    /**
     * Returns a new Scene object that displays the "pick group" screen.
     * 
     * @param server the remote game server object
     * @return a new Scene object for the "pick group" screen
     * @throws IOException if there is an error loading the FXML file
     */
    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = PickGroupController.class.getResource("/fxml/pick-group-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new PickGroupController(server));
        return new Scene(loader.load(), 669, 457);
    }

    @FXML
    private ChoiceBox<String> selector;
    @FXML
    private Button confirmButton;

    private final RemoteGame server;
    private ObservableList<String> groupList;

    /**
     * Constructor for the PickGroupController class.
     * 
     * @param server the remote game server object
     * @throws RemoteException if there is a problem communicating with the remote
     *                         game server
     */
    public PickGroupController(RemoteGame server) throws RemoteException {
        this.server = server;
        this.groupList = FXCollections
                .observableArrayList(server.getGameMap().getInitGroupOwners().stream().map(p -> p.getName()).toList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selector.setItems(groupList);
    }

    /**
     * Event handler for the confirm button. When the confirm button is clicked, the
     * selected group is sent to the server and the user is taken to the "place
     * units" scene.
     * 
     * @param action the ActionEvent object representing the click event
     * @throws IOException if there is an error communicating with the
     *                     remote game server
     */
    @FXML
    public void clickOnConfirm(ActionEvent action) throws IOException {
        String groupName = selector.getSelectionModel().getSelectedItem();
        String response = server.tryPickTerritoryGroupByName(UserSession.getInstance().getUsername(), groupName);
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
        Scene newScene = PlaceUnitsController.getScene(server);
        Stage currStage = (Stage) confirmButton.getScene().getWindow();
        currStage.setScene(newScene);
        currStage.show();
    }

}
