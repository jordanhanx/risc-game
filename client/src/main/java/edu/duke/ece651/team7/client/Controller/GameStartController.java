package edu.duke.ece651.team7.client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import edu.duke.ece651.team7.client.Model.LoginModel;
public class GameStartController {
    private final Stage window;
    public GameStartController(Stage window){
        this.window = window;
    }

    @FXML
    public void clickOnContinue() throws IOException {
        //jump to continue game page
        showContinueView();
    }

    public void showContinueView() throws IOException{

        URL xmlResource = getClass().getResource("/ui/ContinueGame.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(ContinueGameController.class, new ContinueGameController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();
        Scene scene = new Scene(gp, 640, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }

    @FXML
    public void clickOnNewGame() throws IOException{
        showNewGame();
    }

    public void showNewGame() throws IOException{

        //temporary show the test map

        URL xmlResource = getClass().getResource("/ui/NewGame.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(NewGameController.class, new NewGameController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();
        Scene scene = new Scene(gp, 830, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }

    @FXML
    public void clickOnExit() throws IOException{
        URL xmlResource = getClass().getResource("/ui/LoginSignup.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(LoginSignupController.class, new LoginSignupController(new LoginModel(),window));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
        GridPane gp = loader.load();

        Scene scene = new Scene(gp, 640, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        window.setScene(scene);
        window.show();
    }

    @FXML
    public void clickOnJoin() throws IOException{
        URL xmlResource = getClass().getResource("/ui/JoinGame.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(JoinGameController.class, new JoinGameController(window));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });
        GridPane gp = loader.load();

        Scene scene = new Scene(gp, 640, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        window.setScene(scene);
        window.show();
    }



}
