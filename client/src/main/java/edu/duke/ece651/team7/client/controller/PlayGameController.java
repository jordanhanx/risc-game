package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

public class PlayGameController extends UnicastRemoteObject implements RemoteClient, Initializable {

    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = PlayGameController.class.getResource("/fxml/play-game-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new PlayGameController(server));
        return new Scene(loader.load(), 1325, 607);
    }

    @FXML
    private Label playername, food, techResource, techLevel;
    @FXML
    private Button moveButton, attackButton, upgradeButton, researchButton;
    @FXML
    private Label terrName, ownerName;
    @FXML
    private Text levelName0, levelName1, levelName2, levelName3, levelName4, levelName5, levelName6;
    @FXML
    private Label levelValue0, levelValue1, levelValue2, levelValue3, levelValue4, levelValue5, levelValue6;

    @FXML
    private Button Midkemia, Narnia, Oz, Westeros, Gondor, Elantris, Scadrial, Roshar;
    @FXML
    private Button Hogwarts, Mordor, Essos, Dorne, Highgarden, Aranthia, Galadria, Drakoria;
    @FXML
    private Button Dragonstone, Winterfell, Helvoria, Pyke, Volantis, Pentos, Braavos, Oldtown;

    private final RemoteGame server;
    private Property<GameMap> gameMap;
    private Property<Player> self;

    public PlayGameController(RemoteGame server) throws RemoteException {
        super();
        this.server = server;
        this.gameMap = new SimpleObjectProperty<>(server.getGameMap());
        this.self = new SimpleObjectProperty<>(server.getSelfStatus(UserSession.getInstance().getUsername()));
        String response = server.tryRegisterClient(UserSession.getInstance().getUsername(), this);
        if (response != null) {
            throw new IllegalStateException(response);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPlayerInfo();
        setTerritoryColor();
    }

    @Override
    public void updateGameMap(GameMap gameMap) throws RemoteException {
        Platform.runLater(() -> {
            this.gameMap.setValue(gameMap);
            setTerritoryColor();
        });
    }

    @Override
    public void updatePlayer(Player player) throws RemoteException {
        Platform.runLater(() -> {
            this.self.setValue(player);
            setPlayerInfo();
        });
    }

    @Override
    public void showPopupWindow(String msg) throws RemoteException {
        Platform.runLater(() -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Game Server");
            dialog.setContentText(msg);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
        });
    }

    public Territory findTerritory(String terrName) {
        for (Territory t : self.getValue().getTerritories()) {
            if (t.getName().equals(terrName)) {
                return t;
            }
        }
        return gameMap.getValue().getTerritoryByName(terrName);
    }

    @FXML
    public void clickOnTerr(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            Territory selectedTerr = findTerritory(btn.getText());
            terrName.setText(selectedTerr.getName());
            ownerName.setText(selectedTerr.getOwner().getName());
            levelValue0.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(0))));
            levelValue1.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(1))));
            levelValue2.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(2))));
            levelValue3.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(3))));
            levelValue4.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(4))));
            levelValue5.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(5))));
            levelValue6.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(6))));
        } else {
            throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
        }
    }

    @FXML
    public void clickOnMove(ActionEvent event) throws IOException {
        Scene newScene = OrderMoveController.getScene(server);
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.showAndWait();
    }

    @FXML
    public void clickOnAttack(ActionEvent event) throws IOException {
        Scene newScene = OrderAttackController.getScene(server, gameMap.getValue());
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.showAndWait();
    }

    @FXML
    public void clickOnUpgrade(ActionEvent event) throws IOException {
        Scene newScene = OrderUpgradeController.getScene(server);
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.showAndWait();
    }

    @FXML
    public void clickOnResearch(ActionEvent event) throws RemoteException {
        String response = server.tryResearchOrder(UserSession.getInstance().getUsername());
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
    }

    @FXML
    public void clickOnCommit(ActionEvent event) throws RemoteException, InterruptedException {
        String response = server.doCommitOrder(UserSession.getInstance().getUsername());
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
    }

    public void setPlayerInfo() {
        playername.setText(UserSession.getInstance().getUsername());
        food.setText(String.valueOf(self.getValue().getFood().getAmount()));
        techResource.setText(String.valueOf(self.getValue().getTech().getAmount()));
        techLevel.setText(String.valueOf(self.getValue().getCurrentMaxLevel()) + " (level"
                + String.valueOf(self.getValue().getCurrentMaxLevel().label) + ")");
    }

    public Map<String, Color> initColorMap() {
        Color[] colorArr = { Color.MAGENTA, Color.GREEN, Color.BLUE, Color.ORANGE };
        Set<String> playerSet = new TreeSet<>();
        for (Territory t : gameMap.getValue().getTerritories()) {
            playerSet.add(t.getOwner().getName());
        }
        Map<String, Color> colorMap = new HashMap<String, Color>();
        int idx = 0;
        for (String name : playerSet) {
            colorMap.put(name, colorArr[idx]);
            ++idx;
        }
        return colorMap;
    }

    public void setTerritoryColor() {
        Map<String, Color> colorMap = initColorMap();
        // Midkemia, Narnia, Oz, Westeros, Gondor, Elantris, Scadrial, Roshar;
        Midkemia.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Midkemia").getOwner().getName()));
        Narnia.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Narnia").getOwner().getName()));
        Oz.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Oz").getOwner().getName()));
        Westeros.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Westeros").getOwner().getName()));
        Gondor.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Gondor").getOwner().getName()));
        Elantris.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Elantris").getOwner().getName()));
        Scadrial.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Scadrial").getOwner().getName()));
        Roshar.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Roshar").getOwner().getName()));
        // Hogwarts, Mordor, Essos, Dorne, Highgarden, Aranthia, Galadria, Drakoria;
        Hogwarts.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Hogwarts").getOwner().getName()));
        Mordor.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Mordor").getOwner().getName()));
        Essos.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Essos").getOwner().getName()));
        Dorne.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Dorne").getOwner().getName()));
        Highgarden.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Highgarden").getOwner().getName()));
        Aranthia.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Aranthia").getOwner().getName()));
        Galadria.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Galadria").getOwner().getName()));
        Drakoria.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Drakoria").getOwner().getName()));
        // Dragonstone, Winterfell, Helvoria, Pyke, Volantis, Pentos, Braavos, Oldtown;
        Dragonstone
                .setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Dragonstone").getOwner().getName()));
        Winterfell.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Winterfell").getOwner().getName()));
        Helvoria.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Helvoria").getOwner().getName()));
        Pyke.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Pyke").getOwner().getName()));
        Volantis.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Volantis").getOwner().getName()));
        Pentos.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Pentos").getOwner().getName()));
        Braavos.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Braavos").getOwner().getName()));
        Oldtown.setTextFill(colorMap.get(gameMap.getValue().getTerritoryByName("Oldtown").getOwner().getName()));
    }
}
