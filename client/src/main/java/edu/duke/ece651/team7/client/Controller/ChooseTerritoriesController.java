package edu.duke.ece651.team7.client.Controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ChooseTerritoriesController implements Initializable {
    @FXML
    Text TerritoriesGroupNum;
    @FXML
    ListView<String> territoriesList;
    @FXML
    Text errorMsg;

    private final Stage window;
    private ObservableList<String> list;



    public ChooseTerritoriesController(Stage window){this.window = window;}

    @FXML
    public void clickChooseTerritoryGroupA(ActionEvent ae){
        territoriesList.getItems().clear();
        Object source = ae.getSource();
        Button btn = (Button) source;
        String record = "Choose Territories : " + btn.getText();
        list.add(record);
    }

    @FXML
    public void clickChooseTerritoryGroupB(ActionEvent ae){
        territoriesList.getItems().clear();
        Object source = ae.getSource();
        Button btn = (Button) source;
        String record = "Choose Territories : " + btn.getText();
        list.add(record);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        territoriesList.setItems(list);
    }

}
