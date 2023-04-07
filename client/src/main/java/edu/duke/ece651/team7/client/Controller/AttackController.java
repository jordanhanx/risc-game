package edu.duke.ece651.team7.client.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
public class AttackController implements Initializable{
    @FXML
    TextField terrAttackFrom,terrAttackTo,selectAttackLevel,selectAttackNum;
    @FXML
    ListView<String> attackList;

    private final Stage window;
    private ObservableList<String> list;

    public AttackController(Stage window){this.window = window;}

    @FXML
    public void clickAttackButton(){
        String attackInfo = "Choose to attack with "+ selectAttackNum.getText()+" level: "+ selectAttackLevel.getText() + " units from Territory: " + terrAttackFrom.getText() + " to Territory: "+terrAttackTo.getText();
        list.add(attackInfo);
        terrAttackFrom.clear();
        terrAttackTo.clear();
        selectAttackNum.clear();
        selectAttackLevel.clear();
    }

    @FXML
    public void clickFinishButton() throws IOException {

        window.close();

        //go back to the map page
        URL xmlResource = getClass().getResource("/ui/MapTwoPlayersTest.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(MapController.class, new MapController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();

        Scene scene = new Scene(gp, 840, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        attackList.setItems(list);
    }

}

