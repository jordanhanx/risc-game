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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

/**
 * The PlayGameController class is responsible for managing the game view, which
 * displays the current state of the game to the user, and allows the user to
 * interact with the game by clicking on territories and performing various
 * actions, such as moving, attacking, upgrading, researching, and committing
 * orders.
 * This class implements the RemoteClient and Initializable interfaces, and
 * extends the UnicastRemoteObject class to provide remote access to the game
 * server.
 */
public class PlayGameController extends UnicastRemoteObject implements RemoteClient, Initializable {

    /**
     * Returns the scene of the game view with the specified server.
     *
     * @param server the remote game server.
     * @return the scene of the game view.
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static Scene getScene(RemoteGame server) throws IOException {
        URL xmlResource = PlayGameController.class.getResource("/fxml/play-game-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setController(new PlayGameController(server));
        return new Scene(loader.load(), 1325, 607);
    }

    @FXML
    private Label playerName, food, techResource, techLevel;
    @FXML
    private Button moveButton, attackButton, upgradeButton, researchButton;
    @FXML
    private Label terrName, ownerName;
    @FXML
    private Label levelValue0, levelValue1, levelValue2, levelValue3, levelValue4, levelValue5, levelValue6;
    @FXML
    private Label territoryTech, territoryFood;

    @FXML
    private Button Midkemia, Narnia, Oz, Westeros, Gondor, Elantris, Scadrial, Roshar;
    @FXML
    private Button Hogwarts, Mordor, Essos, Dorne, Highgarden, Aranthia, Galadria, Drakoria;
    @FXML
    private Button Dragonstone, Winterfell, Helvoria, Pyke, Volantis, Pentos, Braavos, Oldtown;

    private final RemoteGame server;
    private Property<GameMap> gameMap;
    private Property<Player> self;
    private Map<String, Color> colorMap;

    /**
     * Constructs a new PlayGameController object with the specified remote game
     * server.
     * 
     * @param server the remote game server.
     * @throws RemoteException if there is a problem with the remote connection.
     */
    public PlayGameController(RemoteGame server) throws RemoteException {
        super();
        this.server = server;
        this.gameMap = new SimpleObjectProperty<>(server.getGameMap());
        this.self = new SimpleObjectProperty<>(server.getSelfStatus(UserSession.getInstance().getUsername()));
        this.colorMap = new HashMap<>();
        String response = server.tryRegisterClient(UserSession.getInstance().getUsername(), this);
        if (response != null) {
            throw new IllegalStateException(response);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColorMap();
        setPlayerInfo();
        setTerritoryColor();
    }

    /**
     * Updates the game map property.
     * 
     * @param gameMap the updated game map.
     * @throws RemoteException if there is a remote communication error.
     */
    @Override
    public void updateGameMap(GameMap gameMap) throws RemoteException {
        Platform.runLater(() -> {
            this.gameMap.setValue(gameMap);
            initColorMap();
            setTerritoryColor();
        });
    }

    /**
     * Updates the player property.
     * 
     * @param player the updated player.
     * @throws RemoteException if there is a remote communication error.
     */
    @Override
    public void updatePlayer(Player player) throws RemoteException {
        Platform.runLater(() -> {
            this.self.setValue(player);
            setPlayerInfo();
        });
    }

    /**
     * Displays a popup window with the specified message.
     * 
     * @param msg the message to display.
     * @throws RemoteException if there is a remote communication error.
     */
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

    /**
     * 
     * This method finds the territory object with the given name in the player's
     * owned territories, if it doesn't exist in the owned territories, returns the
     * territory object with the given name from the game map.
     * 
     * @param terrName name of the territory to be searched
     * @return Territory object with the given name
     */
    public Territory findTerritory(String terrName) {
        for (Territory t : self.getValue().getTerritories()) {
            if (t.getName().equals(terrName)) {
                return t;
            }
        }
        return gameMap.getValue().getTerritoryByName(terrName);
    }

    /**
     * This method is called when a player clicks on a territory button. It sets the
     * necessary information of the selected territory such as name, owner, unit
     * levels, produced food and tech.
     * 
     * @param event the action event triggered by the click
     */
    @FXML
    public void clickOnTerr(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            Territory selectedTerr = findTerritory(btn.getText());
            terrName.setText(selectedTerr.getName());
            ownerName.setText(selectedTerr.getOwner().getName());
            terrName.setTextFill(colorMap.get(selectedTerr.getOwner().getName()));
            ownerName.setTextFill(colorMap.get(selectedTerr.getOwner().getName()));
            levelValue0.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(0))));
            levelValue1.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(1))));
            levelValue2.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(2))));
            levelValue3.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(3))));
            levelValue4.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(4))));
            levelValue5.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(5))));
            levelValue6.setText(String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(6))));
            territoryFood.setText(String.valueOf(selectedTerr.produceFood().getAmount()));
            territoryTech.setText(String.valueOf(selectedTerr.produceTech().getAmount()));

        } else {
            throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
        }
    }

    /**
     * This method is called when a player clicks on the move button. It opens the
     * OrderMoveController scene to allow the player to make move orders.
     * 
     * @param event the action event triggered by the click
     * @throws IOException if there is an error while opening the
     *                     OrderMoveController scene
     */
    @FXML
    public void clickOnMove(ActionEvent event) throws IOException {
        Scene newScene = OrderMoveController.getScene(server);
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.initOwner(playerName.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.showAndWait();
    }

    /**
     * This method is called when a player clicks on the attack button. It opens the
     * OrderAttackController scene to allow the player to make attack orders.
     * 
     * @param event the action event triggered by the click
     * @throws IOException if there is an error while opening the
     *                     OrderAttackController scene
     */
    @FXML
    public void clickOnAttack(ActionEvent event) throws IOException {
        Scene newScene = OrderAttackController.getScene(server, gameMap.getValue());
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.initOwner(playerName.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.showAndWait();
    }

    /**
     * Event handler for the "Upgrade" button. Opens a new window to upgrade the
     * user's orders.
     * 
     * @param event The ActionEvent triggered by clicking the "Upgrade" button.
     * @throws IOException If an input/output error occurs while opening the new
     *                     window.
     */
    @FXML
    public void clickOnUpgrade(ActionEvent event) throws IOException {
        Scene newScene = OrderUpgradeController.getScene(server);
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.initOwner(playerName.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.showAndWait();
    }

    /**
     * Event handler for the "Research" button.
     * Attempts to research a new order for the current user.
     * 
     * @param event The ActionEvent triggered by clicking the "Research" button.
     * @throws RemoteException          If a remote method invocation error occurs
     *                                  while attempting to research the order.
     * @throws IllegalArgumentException If the server returns an error message.
     */
    @FXML
    public void clickOnResearch(ActionEvent event) throws RemoteException {
        String response = server.tryResearchOrder(UserSession.getInstance().getUsername());
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
    }

    /**
     * Event handler for the "Commit" button.
     * Attempts to commit the current user's orders.
     * 
     * @param event The ActionEvent triggered by clicking the "Commit" button.
     * @throws RemoteException          If a remote method invocation error occurs
     *                                  while attempting to commit the orders.
     * @throws IllegalArgumentException If the server returns an error message.
     */
    @FXML
    public void clickOnCommit(ActionEvent event) throws RemoteException {
        String response = server.doCommitOrder(UserSession.getInstance().getUsername());
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
    }

    /**
     * Initializes the color map used to display player information.
     * The map maps each player's name to a color used to display their information.
     */
    public void initColorMap() {
        colorMap.clear();
        Color[] colorArr = { Color.MAGENTA, Color.GREEN, Color.BLUE, Color.ORANGERED };
        Set<String> playerSet = new TreeSet<>();
        for (Territory t : gameMap.getValue().getTerritories()) {
            playerSet.add(t.getOwner().getName());
        }
        int idx = 0;
        for (String name : playerSet) {
            colorMap.put(name, colorArr[idx]);
            ++idx;
        }
    }

    /**
     * Sets the current user's information in the UI. This includes the user's name,
     * food and tech resources, and current tech level.
     */
    public void setPlayerInfo() {
        playerName.setText(UserSession.getInstance().getUsername());
        food.setText(String.valueOf(self.getValue().getFood().getAmount()));
        techResource.setText(String.valueOf(self.getValue().getTech().getAmount()));
        techLevel.setText(String.valueOf(self.getValue().getCurrentMaxLevel()) + " (level"
                + String.valueOf(self.getValue().getCurrentMaxLevel().label) + ")");

        playerName.setTextFill(colorMap.get(UserSession.getInstance().getUsername()));
        food.setTextFill(colorMap.get(UserSession.getInstance().getUsername()));
        techResource.setTextFill(colorMap.get(UserSession.getInstance().getUsername()));
        techLevel.setTextFill(colorMap.get(UserSession.getInstance().getUsername()));
    }

    /**
     * Sets the color of each territory based on the owner's color.
     * The color is retrieved from the colorMap that is initialized in the
     * constructor.
     */
    public void setTerritoryColor() {
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
