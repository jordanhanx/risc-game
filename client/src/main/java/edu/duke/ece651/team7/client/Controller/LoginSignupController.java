package edu.duke.ece651.team7.client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.duke.ece651.team7.client.Model.LoginModel;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;


import java.util.HashMap;

public class LoginSignupController {
    @FXML
    TextField userName;
    @FXML
    TextField passWord;
    @FXML
    Text error_msg;

    private final LoginModel logModel;
    private final Stage window;

    public LoginSignupController() {
        window = null;
        logModel=null;
    }

    public LoginSignupController(LoginModel logModel, Stage window) {
        this.logModel=logModel;
        this.window = window;

    }

    @FXML
    public void clickLoginSignup() throws IOException {
        if(logModel.loginSignup(userName.getText(),passWord.getText())){
            showMainPage();
        }else{
            error_msg.setText("Invalid username or password");
        }



    }

    public void showMainPage() throws IOException{
        URL xmlResource = getClass().getResource("/ui/GameStart.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        // use hashMap to collect controllers.
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(GameStartController.class, new GameStartController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();
        // create scene and load css
        Scene scene = new Scene(gp, 700, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }

    public String getUserName(){
        return userName.getText();
    }

    public String getPassWord(){
        return passWord.getText();
    }

}
