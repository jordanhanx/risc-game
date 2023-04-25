package edu.duke.ece651.team7.client.controller;

import edu.duke.ece651.team7.client.MusicFactory;
import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.RemoteGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class OrderManufactureController implements Initializable {
    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = OrderManufactureController.class.getResource("/fxml/order-manufacture-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderManufactureController(server));
        return new Scene(loader.load(), 600, 400);
    }

    private RemoteGame server;
    @FXML
    private TextField amount;
    @FXML
    private CheckBox isBomb;
    @FXML
    private CheckBox isAirPlane;

    public OrderManufactureController(RemoteGame server) throws RemoteException {
        this.server=server;
    }

    @FXML
    public void clickOnManufacture(ActionEvent action) throws RemoteException{
//        String response=server.tryManfactureOrder(UserSession.getInstance().getUsername(),
//                isBomb.isSelected(),
//                Integer.parseInt(amount.getText())
//        );

        String response = null;
        if (response != null) {
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }

        //set the manfacture sound
        MediaPlayer manufacturePlayer = MusicFactory.createManufacturePlayer();
        manufacturePlayer.play();
    }

    @FXML
    public void clickOnFinish(ActionEvent action){
        Stage currStage = (Stage) amount.getScene().getWindow();
        currStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        // Bind the selectedProperty of the checkboxes
        isBomb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                isAirPlane.setSelected(false);
            }
        });
        isAirPlane.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                isBomb.setSelected(false);
            }
        });

        // By default, select isBomb checkbox
        isBomb.setSelected(true);
    }
}
