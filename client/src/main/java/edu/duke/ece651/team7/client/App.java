/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team7.client;

import edu.duke.ece651.team7.client.Controller.PlaceUnitsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import edu.duke.ece651.team7.client.Model.LoginModel;
import edu.duke.ece651.team7.client.Controller.LoginSignupController;

public class App extends Application {
  public static void main(String[] args) {
    // if (args.length == 2) {
    //   try {
    //     Client client = new Client(args[0], Integer.parseInt(args[1]),
    //         new BufferedReader(new InputStreamReader(System.in)), System.out);
    //     client.start();
    //   } catch (Exception e) {
    //     System.err.println("Exception: " + e);
    //   }
    // } else {
    //   System.err.println("Usage: client <host> <port>");
    // }

    System.out.println("GUI begin running.");
    launch(args);
  }

  @Override
  public void start(Stage window){
    try {
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


    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Failed to load UI: " + e.getMessage());

    }
  }


}
