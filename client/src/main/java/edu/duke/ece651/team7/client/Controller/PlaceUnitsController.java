package edu.duke.ece651.team7.client.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
public class PlaceUnitsController implements Initializable{
    @FXML
    ListView<String> UnitPlacementList;
    @FXML
    ChoiceBox<String>territorySelect;
    @FXML
    ChoiceBox<Integer>numberSelect;
    @FXML
    Text errorMsg;
    @FXML
    Text unitsLeft;


    private final Stage window;

    // these two lists are used in choiceBox
    private ObservableList<String> terrList;
    private ObservableList<Integer> numberList;
    private int deployNum;

    private ObservableList<String> list;


    public PlaceUnitsController(Stage window){
        this.window = window;
        terrList = FXCollections.observableArrayList();
        numberList = FXCollections.observableArrayList();


    }

    @FXML
    public void clickOnPlace(){

        errorMsg.setText("");
        // Get user input
        String selectTerr = territorySelect.getValue();
        Integer selectNumber = numberSelect.getValue();
        if(selectNumber==null || selectTerr==null){
            errorMsg.setText("Please select the territory and units");
            return;
        }
        int units = Integer.parseInt(unitsLeft.getText()) - selectNumber;
        if(units<0){
            errorMsg.setText("Do not have enough units to place!");
            return;
        }
        //update the number of units left for the player
        unitsLeft.setText(String.valueOf(units));

        String record = "Choose to place "+ selectNumber+ " units on "+selectTerr;
        list.add(record);

        //pass data to the model

    }

    @FXML
    public void clickFinishButton() throws IOException {

        String selectTerr = territorySelect.getValue();
        Integer selectNumber = numberSelect.getValue();

        if(selectNumber==null || selectTerr==null){
            errorMsg.setText("Please select the territory and units");
            return;
        }
        window.close();
        showTwoPlayersMap();
    }

    public void showTwoPlayersMap() throws IOException{
        URL xmlResource = getClass().getResource("/ui/MapTwoPlayersTest.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(MapController.class, new MapController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();
        Scene scene = new Scene(gp, 850, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }

    private void setTerritoryList(ObservableList<String>l){
        // Get available territories from server and then add them into observableArrayList

        l.add("Dorne");
        l.add("Mordor");
    }


    private void createAvailalbeNumList(ObservableList<Integer> l, int totalUnits){
        l.clear();
        for(int i=1; i<=totalUnits; i++){
            l.add(i);
        }
    }


    private int getInitialUnits(){
        //get the total units number
        return 20;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int initNum = getInitialUnits();
        createAvailalbeNumList(numberList, initNum);
        setTerritoryList(terrList);
        territorySelect.setItems(terrList);
        numberSelect.setItems(numberList);
        list = FXCollections.observableArrayList();
        UnitPlacementList.setItems(list);
        unitsLeft.setText(String.valueOf(initNum));
    }


}
