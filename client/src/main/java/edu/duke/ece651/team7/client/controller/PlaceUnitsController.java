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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

/**
 * The PlaceUnitsController class is responsible for managing the "place units"
 * page in the Risk game.
 */
public class PlaceUnitsController implements Initializable {
    /**
     * Returns the scene of the Place Units page.
     * 
     * @param server the RemoteGame object to be used in the page
     * @return the Scene object representing the Place Units page
     * @throws IOException if the fxml file is not found or the connection with
     *                     server is close.
     */
    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = PlaceUnitsController.class.getResource("/fxml/place-units-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new PlaceUnitsController(server));
        return new Scene(loader.load(), 669, 457);
    }

    @FXML
    private Text remainUnits;
    @FXML
    private ChoiceBox<String> territorySelector;
    @FXML
    private TextField unitsInputer;
    @FXML
    private Button placeButton;
    @FXML
    private Button finishButton;
    @FXML
    private ListView<String> UnitPlacementList;

    private final RemoteGame server;
    private Player self;
    private ObservableList<String> territoryList;
    private int initUnits;

    /**
     * Constructs a PlaceUnitsController object.
     * 
     * @param server the RemoteGame object to be used in the page
     * @throws RemoteException if an error occurs when communicating with the server
     */
    public PlaceUnitsController(RemoteGame server) throws RemoteException {
        this.server = server;
        this.self = server.getSelfStatus(UserSession.getInstance().getUsername());
        this.territoryList = FXCollections
                .observableArrayList(self.getTerritories().stream().map(t -> t.getName()).toList());
        this.initUnits = server.getGameInitUnits();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        remainUnits.setText(String.valueOf(initUnits - self.getTotalUnits()));
        territorySelector.setItems(territoryList);
    }

    /**
     * Handles the action of clicking on the place button.
     * Places the entered number of units in the selected territory and updates the
     * remaining number of units to be placed.
     * 
     * @param action the action event
     * @throws RemoteException if an error occurs when communicating with the
     *                         server
     */
    @FXML
    public void clickOnPlace(ActionEvent action) throws RemoteException {
        String response = server.tryPlaceUnitsOn(UserSession.getInstance().getUsername(),
                territorySelector.getSelectionModel().getSelectedItem(), Integer.parseInt(unitsInputer.getText()));
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
        self = server.getSelfStatus(UserSession.getInstance().getUsername());
        remainUnits.setText(String.valueOf(initUnits - self.getTotalUnits()));
    }

    /**
     * Handles the action of clicking on the finish button.
     * Finishes the placement phase by committing the order and goes to the Play
     * Game page.
     * 
     * @param action the action event
     * @throws IOException if an error occurs when changing the scene to
     *                     the Play Game page or an error occurs when
     *                     communicating with the server.
     */
    @FXML
    public void clickOnFinish(ActionEvent action) throws IOException {
        doFinish();
    }

    /**
     * Finishes placing the units and proceeds to the game play page.
     * 
     * @throws IOException if an error occurs when changing the scene to
     *                     the Play Game page or an error occurs when
     *                     communicating with the server.
     */
    public void doFinish() throws IOException {
        String response = server.doCommitOrder(UserSession.getInstance().getUsername());
        if (response != null) {
            throw new IllegalArgumentException(response);
        } else {
            Scene newScene = PlayGameController.getScene(server);
            Stage currStage = (Stage) placeButton.getScene().getWindow();
            currStage.setScene(newScene);
            currStage.show();
        }
    }

}
