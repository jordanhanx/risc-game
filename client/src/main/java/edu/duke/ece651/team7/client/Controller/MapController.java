package edu.duke.ece651.team7.client.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.HashMap;
public class MapController implements Initializable {
    @FXML
    Label terrName,level0,level1,level2,level3,level4,level5,level6;

    private Stage window;

    public MapController(Stage window) {
        this.window = window;
    }

    @FXML
    public void clickOnTerr(ActionEvent ae){
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            terrName.setText(btn.getText());
            setTerritoryLevelUnit();
        }
        else {
            throw new IllegalArgumentException("Invalid source " +
                    source +
                    " for ActionEvent");
        }
    }

    private void setTerritoryLevelUnit(){
        //temporary initialize all the territories' units to 0
        level0.setText("0");
        level1.setText("0");
        level2.setText("0");
        level3.setText("0");
        level4.setText("0");
        level5.setText("0");
        level6.setText("0");
    }

    /*
    private void setTerritoryLevelUnit(String terrName) {

        //should get the level unit data (ans) from GameModel

        ArrayList<Integer> ans = new ArrayList<Integer>();

        level0.setText(String.valueOf(ans.get(0)));
        level1.setText(String.valueOf(ans.get(1)));
        level2.setText(String.valueOf(ans.get(2)));
        level3.setText(String.valueOf(ans.get(3)));
        level4.setText(String.valueOf(ans.get(4)));
        level5.setText(String.valueOf(ans.get(5)));
        level6.setText(String.valueOf(ans.get(6)));
    }
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        terrName.setText("");
        level0.setText("");
        level1.setText("");
        level2.setText("");
        level3.setText("");
        level4.setText("");
        level5.setText("");
        level6.setText("");
    }


    @FXML
    public void clickOnAttack() throws IOException{
        showAttack();
    }

    private void showAttack() throws IOException{
        URL xmlResource = getClass().getResource("/ui/Attack.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(AttackController.class, new AttackController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();

//       GridPane gp = FXMLLoader.load(xmlResource);
        Scene scene = new Scene(gp, 840, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }


    @FXML
    public void clickOnMove() throws IOException {
        showMove();
    }

    private void showMove() throws IOException {
        URL xmlResource = getClass().getResource("/ui/Move.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(MoveController.class, new MoveController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();

//       GridPane gp = FXMLLoader.load(xmlResource);
        Scene scene = new Scene(gp, 840, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();

    }


    @FXML
    public void clickOnUpgrade() throws IOException{
        showUpgrade();
    }

    private void showUpgrade() throws IOException{
        URL xmlResource = getClass().getResource("/ui/Upgrade.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(UpgradeController.class, new UpgradeController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();

//       GridPane gp = FXMLLoader.load(xmlResource);
        Scene scene = new Scene(gp, 840, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }

    @FXML
    public void clickOnResearch() throws IOException{
        showResearch();
    }

    private void showResearch() throws IOException{
        URL xmlResource = getClass().getResource("/ui/Research.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>,Object> controllers = new HashMap<>();
        controllers.put(ResearchController.class, new ResearchController(window));
        loader.setControllerFactory(controllers::get);
        GridPane gp = loader.load();

//       GridPane gp = FXMLLoader.load(xmlResource);
        Scene scene = new Scene(gp, 840, 480);
        URL cssResource = getClass().getResource("/ui/buttons.css");
        scene.getStylesheets().add(cssResource.toString());

        this.window.setScene(scene);
        this.window.show();
    }




}

