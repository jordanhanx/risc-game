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

public class OrderAllyController implements Initializable {

    public static Scene getScene(RemoteGame server, GameMap gameMap) throws IOException {
        URL xmlResource = OrderAllyController.class.getResource("/fxml/order-ally-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new OrderAllyController(server, gameMap));
        return new Scene(loader.load(), 600, 400);
    }

    private RemoteGame server;
    @FXML
    private ChoiceBox<String> allySelector;
    private ObservableList<String> allyList;

    @FXML private ImageView player0;
    @FXML private ImageView player1;
    @FXML private ImageView player2;
    @FXML private ImageView player3;

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
    @FXML
    public void clickOnAlly(ActionEvent action) throws RemoteException{
//        String response = server.tryAllianceOrder(UserSession.getInstance().getUsername(),
//                allySelector.getValue()
//                );
//
        String response = null;
        if (response != null) {
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }

        //set the ally sound
        MediaPlayer allyPlayer = MusicFactory.createAllyPlayer();
        allyPlayer.play();
    }

    @FXML
    public void clickOnFinish(ActionEvent action){
        Stage currStage = (Stage) allySelector.getScene().getWindow();
        currStage.close();
    }

}
