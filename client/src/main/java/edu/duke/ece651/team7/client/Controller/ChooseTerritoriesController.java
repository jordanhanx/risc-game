package edu.duke.ece651.team7.client.Controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseTerritoriesController implements Initializable {
    @FXML
    Text TerritoriesGroupNum;
    @FXML
    ListView<String> territoriesList;
    @FXML
    Text errorMsg;
    @FXML
    ChoiceBox<List<String>> territoryGroupSelect;

    private final Stage window;
    private ObservableList<String> list;

    private ObservableList<List<String>> terrGroupList;



    public ChooseTerritoriesController(Stage window){
        this.window = window;
        terrGroupList = FXCollections.observableArrayList();
    }


    @FXML
    public void clickOnChoose(){
        list.clear();
        errorMsg.setText("");
        // Get user input
        List<String> selectTerr = territoryGroupSelect.getValue();
        if(selectTerr.isEmpty()){
            errorMsg.setText("Please Choose one territory group.");
            return;
        }
        String record="Choose territories: ";
        for(String terr: selectTerr) {
            record += " " + terr+",";
        }
        list.add(record);

        //pass data to the model
    }


    @FXML
    public void clickFinishButton() throws IOException {

        if( territoriesList.getItems().isEmpty()){
            errorMsg.setText("Please Choose One Territory Group");
            return;
        }

        window.close();

        if(TerritoriesGroupNum.getText().equals("2")){

            //go to place units page.
            URL xmlResource = getClass().getResource("/ui/PlaceUnits.fxml");
            FXMLLoader loader = new FXMLLoader(xmlResource);
            HashMap<Class<?>,Object> controllers = new HashMap<>();
            controllers.put(PlaceUnitsController.class, new PlaceUnitsController(window));
            loader.setControllerFactory((c) -> {
                return controllers.get(c);
            });
            GridPane gp = loader.load();
            Scene scene = new Scene(gp, 640, 480);
            URL cssResource = getClass().getResource("/ui/buttons.css");
            scene.getStylesheets().add(cssResource.toString());
            window.setScene(scene);
            window.show();


        }else if(TerritoriesGroupNum.getText().equals("3")){

            //have not built choose territories group for three players yet

            URL xmlResource = getClass().getResource("/ui/mapThreePlayers.fxml");
            GridPane gp = FXMLLoader.load(xmlResource);
            Scene scene = new Scene(gp, 640, 480);
            URL cssResource = getClass().getResource("/ui/buttons.css");
            scene.getStylesheets().add(cssResource.toString());

            this.window.setScene(scene);
            this.window.show();
        }else if(TerritoriesGroupNum.getText().equals("4")){

            //have not built choose territories group for three players yet

            URL xmlResource = getClass().getResource("/ui/mapFourPlayers.fxml");
            GridPane gp = FXMLLoader.load(xmlResource);
            Scene scene = new Scene(gp, 640, 480);
            URL cssResource = getClass().getResource("/ui/buttons.css");
            scene.getStylesheets().add(cssResource.toString());
            this.window.setScene(scene);
            this.window.show();
        }

    }


    private void setTerritoryList(ObservableList<List<String>>l){
        // Get available territories from server and then add them into observableArrayList
        List<String> l1 = new ArrayList<>();
        l1.add("Dorne");
        l1.add("Mordor");
        l1.add("Roshar");
        List<String> l2 = new ArrayList<>();
        l2.add("Oz");
        l2.add("Gondor");
        l2.add("Hogwarts");
        l.add(l1);
        l.add(l2);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //get the player num from the server
        int playerNum = 2;
        TerritoriesGroupNum.setText(Integer.toString(playerNum));

        list = FXCollections.observableArrayList();
        territoriesList.setItems(list);

        setTerritoryList(terrGroupList);
        territoryGroupSelect.setItems(terrGroupList);
    }

}
