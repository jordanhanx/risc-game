package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import edu.duke.ece651.team7.client.model.GameList;
import edu.duke.ece651.team7.shared.GameDto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class HomePageController {

    public static Scene getScene() throws IOException {
        URL xmlResource = LoginSignupController.class.getResource("/fxml/HomePage.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 640, 480);
    }

}
