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

/**
 * The OrderManufactureController class controls the "manufacture order" page, which allows
 * players to build bomb or air plane
 */
public class OrderManufactureController implements Initializable {

    /**
     * Returns a Scene object to display the order-manufacture page.
     * @param server a RemoteGame object used to communicate with the server
     * @return a Scene object representing the order-manufacture page
     * @throws IOException if there is an error reading the FXML file
     */
    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = OrderManufactureController.class.getResource("/fxml/order-manufacture-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderManufactureController(server));
        return new Scene(loader.load(), 600, 400);
    }

    @FXML
    private TextField amount;
    @FXML
    private CheckBox isBomb;
    @FXML
    private CheckBox isAirPlane;
    private RemoteGame server;

    /**
     * Constructs a new OrderManufactureController object.
     * @param server a RemoteGame object used to communicate with the server
     * @throws RemoteException if there is an error with the remote method invocation
     */
    public OrderManufactureController(RemoteGame server) throws RemoteException {
        this.server=server;
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

    /**
     * Sends a request to the server to manufacture an order based on the user's inputs.
     * @param action an ActionEvent object representing the user's click on the manufacture button
     * @throws RemoteException if there is an error with the remote method invocation
     * @throws IllegalArgumentException if the response from the server is not null
     */
    @FXML
    public void clickOnManufacture(ActionEvent action) throws RemoteException{
        String response=server.tryManufactureOrder(UserSession.getInstance().getUsername(),
                isBomb.isSelected(),
                Integer.parseInt(amount.getText())
        );

        if (response != null) {
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }

        //set the manfacture sound
        MediaPlayer manufacturePlayer = MusicFactory.createManufacturePlayer();
        manufacturePlayer.play();
    }

    /**
     * Closes the current stage.
     * @param action an ActionEvent object representing the user's click on the finish button
     */
    @FXML
    public void clickOnFinish(ActionEvent action){
        Stage currStage = (Stage) amount.getScene().getWindow();
        currStage.close();
    }

}
