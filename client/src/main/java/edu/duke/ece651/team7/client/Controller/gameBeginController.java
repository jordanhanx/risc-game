package edu.duke.ece651.team7.client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.duke.ece651.team7.client.Model.loginModel;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;

public class gameBeginController {
    @FXML
    TextField userName;
    @FXML
    TextField passWord;
    @FXML
    Text error_msg;

    private final loginModel logModel;
    private final Stage window;

    public gameBeginController() {
        window = null;
        logModel=null;
    }

    public gameBeginController(loginModel logModel, Stage window) {
        this.logModel=logModel;
        this.window = window;

    }

    @FXML
    public void clickLoginSignup() throws IOException {
        if(logModel.loginSignup(userName.getText(),passWord.getText())){
            showMapTwoPlayers();
        }else{
            error_msg.setText("Invalid username or password");
        }
    }

    private void showMapTwoPlayers() throws IOException{
        URL xmlResource = getClass().getResource("/ui/mapTwoPlayers.fxml");
//        FXMLLoader loader = new FXMLLoader(xmlResource);
//
//        HashMap<Class<?>,Object> controllers = new HashMap<>();
//        controllers.put(mapTwoPlayersController.class, new mapTwoPlayersController());
//        loader.setControllerFactory(controllers::get);
//        GridPane gp = loader.load();

        GridPane gp = FXMLLoader.load(xmlResource);
        Scene scene = new Scene(gp, 640, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }

}
