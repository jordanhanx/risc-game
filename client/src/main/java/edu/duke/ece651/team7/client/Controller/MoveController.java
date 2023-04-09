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

import javafx.scene.Node;
import javafx.event.ActionEvent;
public class MoveController implements Initializable{
    @FXML
    TextField terrMoveFrom,terrMoveTo,selectMoveLevel,selectMoveNum;
    @FXML
    ListView<String> moveList;

    private final Stage window;
    private ObservableList<String> list;

    public MoveController(Stage window){this.window = window;}

    @FXML
    public void clickMoveButton(){
        String moveInfo = "Choose to move "+ selectMoveNum.getText() + " level: "+selectMoveLevel.getText() + " units from Territory: " + terrMoveFrom.getText() + " to Territory: "+terrMoveTo.getText();
        list.add(moveInfo);
        terrMoveFrom.clear();
        terrMoveTo.clear();
        selectMoveNum.clear();
        selectMoveLevel.clear();
    }

    @FXML
    public void clickFinishButton() throws IOException {
        window.close();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        moveList.setItems(list);
    }

}
