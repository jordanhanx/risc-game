package edu.duke.ece651.team7.client.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
        GameStartController mp = new GameStartController(window);
        try {
            mp.showMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        attackList.setItems(list);
    }

}

