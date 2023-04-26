package edu.duke.ece651.team7.client.controller;

import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;
import java.util.stream.Collectors;

import edu.duke.ece651.team7.client.MusicFactory;
import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

/**
 * The OrderAllyController class controls the "ally order" page, which allows
 * players to ally with other players
 */
public class OrderAllyController implements Initializable {

    /**
     * Returns the scene for the "Order Ally" page.
     * @param server the RemoteGame instance representing the game server
     * @param gameMap the GameMap instance representing the game map
     * @return the Scene for the "Order Ally" page
     * @throws IOException if an I/O error occurs while loading the FXML file
     */
    public static Scene getScene(RemoteGame server, GameMap gameMap) throws IOException {
        URL xmlResource = OrderAllyController.class.getResource("/fxml/order-ally-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderAllyController(server, gameMap));
        return new Scene(loader.load(), 600, 400);
    }

    @FXML
    private ChoiceBox<String> allySelector;
    @FXML private ImageView player0;
    @FXML private ImageView player1;
    @FXML private ImageView player2;
    @FXML private ImageView player3;
    private ObservableList<String> allyList;
    private RemoteGame server;

    /**
     * Constructs an instance of OrderAllyController.
     * @param server the RemoteGame instance representing the game server
     * @param gameMap the GameMap instance representing the game map
     * @throws RemoteException if there is a remote communication error
     */
    public OrderAllyController(RemoteGame server, GameMap gameMap) throws RemoteException{
        this.server=server;
        this.allyList = FXCollections.observableArrayList(gameMap.getTerritories().stream()
                        .map(Territory::getOwner)
                        .map(Player::getName)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allySelector.setItems(allyList);
        setImage();
    }

    /**
     * Sets up the images corresponding to each player, displaying their corresponding image if they are in the game, and a locked image otherwise.
     */
    private void setImage(){
        Map<Integer, ImageView> ImageViewMap = new HashMap<>();
        ImageViewMap.put(0,player0);
        ImageViewMap.put(1,player1);
        ImageViewMap.put(2,player2);
        ImageViewMap.put(3,player3);
        for(int i = 0; i < 4; i++){
            if(i < allyList.size()){
                Image validImage = new Image(getClass().getResourceAsStream("/image/player" + i + ".png"));
                ImageViewMap.get(i).setImage(validImage);
            }
            else{
                Image lockedImage = new Image(getClass().getResourceAsStream("/image/player" + i + "_lock.png"));
                ImageViewMap.get(i).setImage(lockedImage);
            }
        }

    }

    /**
     * Handles the action of clicking on the "Ally" button, sending an alliance request to the chosen player.
     * @param action the event triggered by clicking on the "Ally" button
     * @throws RemoteException if there is a remote communication error
     */
    @FXML
    public void clickOnAlly(ActionEvent action) throws RemoteException{
        String response = server.tryAllianceOrder(UserSession.getInstance().getUsername(),
                allySelector.getValue()
                );
        if (response != null) {
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }
        //set the ally sound
        MediaPlayer allyPlayer = MusicFactory.createAllyPlayer();
        allyPlayer.play();
    }

    /**
     * Handles the action of clicking on the "Finish" button, closing the "Order Ally" page.
     * @param action the event triggered by clicking on the "Finish" button
     */
    @FXML
    public void clickOnFinish(ActionEvent action){
        Stage currStage = (Stage) allySelector.getScene().getWindow();
        currStage.close();
    }

}
