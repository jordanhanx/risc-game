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

public class PlaceUnitsController implements Initializable {

    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/place-units-page");
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

    @FXML
    public void clickOnPlace(ActionEvent action) throws RemoteException, InterruptedException {
        String response = server.tryPlaceUnitsOn(UserSession.getInstance().getUsername(),
                territorySelector.getSelectionModel().getSelectedItem(), Integer.parseInt(unitsInputer.getText()));
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
        self = server.getSelfStatus(UserSession.getInstance().getUsername());
        remainUnits.setText(String.valueOf(initUnits - self.getTotalUnits()));
    }

    @FXML
    public void clickOnFinish(ActionEvent action) throws InterruptedException, IOException {
        doFinish();
    }

    public void doFinish() throws InterruptedException, IOException {
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
