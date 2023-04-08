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
public class ResearchController implements Initializable{
    @FXML
    TextField upgradedTechLevel;
    @FXML
    ListView<String> researchList;

    private final Stage window;
    private ObservableList<String> list;

    public ResearchController(Stage window){this.window = window;}

    @FXML
    public void clickResearchButton(){
        String researchInfo = "Choose to upgrade technology level to Level : " + upgradedTechLevel.getText();
        list.add(researchInfo);
        upgradedTechLevel.clear();
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
        researchList.setItems(list);
    }

}
