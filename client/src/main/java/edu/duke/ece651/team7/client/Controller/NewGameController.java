package edu.duke.ece651.team7.client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import edu.duke.ece651.team7.client.Model.LoginModel;
public class NewGameController {
    private final Stage window;

    @FXML
    TextField playerNum, InitialUnits;
    public NewGameController(Stage window){
        this.window = window;
    }

    @FXML
    public void clickOnStart() throws IOException {
        showMap(Integer.parseInt(playerNum.getText()));

        //pass playerNum.getText() to GameModel
    }

    public void showMap(int num) throws IOException {
        if(num==2){

            URL xmlResource = getClass().getResource("/ui/ChooseTerritories.fxml");
            FXMLLoader loader = new FXMLLoader(xmlResource);
            HashMap<Class<?>,Object> controllers = new HashMap<>();
            controllers.put(ChooseTerritoriesController.class, new ChooseTerritoriesController(window));
            loader.setControllerFactory(controllers::get);
            GridPane gp = loader.load();
            Scene scene = new Scene(gp, 850, 480);
            URL cssResource = getClass().getResource("/ui/buttons.css");
            scene.getStylesheets().add(cssResource.toString());

            this.window.setScene(scene);
            this.window.show();


        }else if(num==3){

            //have not built choose territories group for three players yet

            URL xmlResource = getClass().getResource("/ui/mapThreePlayers.fxml");

            GridPane gp = FXMLLoader.load(xmlResource);
            Scene scene = new Scene(gp, 640, 480);
            URL cssResource = getClass().getResource("/ui/buttons.css");
            scene.getStylesheets().add(cssResource.toString());

            this.window.setScene(scene);
            this.window.show();
        }else if(num==4){

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



    @FXML
    public void clickOnExit() throws IOException{
        LoginSignupController ls = new LoginSignupController(new LoginModel(),window);
        ls.showMainPage();
    }





}
