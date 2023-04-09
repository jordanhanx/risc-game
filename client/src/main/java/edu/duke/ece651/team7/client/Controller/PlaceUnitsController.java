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
        // Get user input
        String selectTerr = territorySelect.getValue();
        Integer selectNumber = numberSelect.getValue();

        if(selectNumber==null || selectTerr==null){
            errorMsg.setText("You choose wrong value.");
            return;
        }

        String record = "Choose to place "+ selectNumber+ " units on "+selectTerr;

        list.add(record);

        //pass data to the model

        //update the number of units left for the player
        int units = Integer.parseInt(unitsLeft.getText()) - selectNumber;
        unitsLeft.setText(String.valueOf(units));

    }

    @FXML
    public void clickFinishButton() throws IOException {

        String selectTerr = territorySelect.getValue();
        Integer selectNumber = numberSelect.getValue();

        if(selectNumber==null || selectTerr==null){
            errorMsg.setText("You choose wrong value.");
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

        l.add("a");
        l.add("b");
    }


    private void createAvailalbeNumList(ObservableList<Integer> l, int deployNum){
        l.clear();
        for(int i=1; i<=deployNum; i++){
            l.add(i);
        }
    }


    private int getInitialUnitsDeployNumber(){
        // Get total deployment number from model
        return 20;
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int initNum = getInitialUnitsDeployNumber();
        createAvailalbeNumList(numberList, initNum);
        setTerritoryList(terrList);
        territorySelect.setItems(terrList);
        numberSelect.setItems(numberList);


        list = FXCollections.observableArrayList();
        UnitPlacementList.setItems(list);

        //get the units from the model
        int totalUnit =20;
        unitsLeft.setText(String.valueOf(totalUnit));
    }


}
