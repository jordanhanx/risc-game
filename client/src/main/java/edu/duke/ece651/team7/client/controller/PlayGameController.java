package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

import edu.duke.ece651.team7.client.model.UserSession;
import edu.duke.ece651.team7.shared.*;

import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.Node;

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
        return new Scene(loader.load(), 1065, 522);
    }

    @FXML
    private Label playerName, food, techResource, techLevel, allyName;
    @FXML
    private Button moveButton, attackButton, upgradeButton, researchButton, shortCutButton;
    @FXML
    private SVGPath Midkemia, Narnia, Oz, Westeros, Gondor, Elantris, Scadrial, Roshar;
    @FXML
    private SVGPath Hogwarts, Mordor, Essos, Dorne, Highgarden, Aranthia, Galadria, Drakoria;
    @FXML
    private SVGPath Dragonstone, Winterfell, Helvoria, Pyke, Volantis, Pentos, Braavos, Oldtown;

    @FXML private ImageView playerImage;

    @FXML
    Pane paneGroup;
    private final RemoteGame server;
    private Property<GameMap> gameMap;
    private Property<Player> self;
    private Map<String, Color> colorMap;
    static HashMap<String, Tooltip> toolTipMap = new HashMap<>();
    public static String[] terrSourceDest = new String[2];
    private static int clickCount = 0;

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
        DisplayImage();
        setToolTipMap();
    }

    private void setToolTipMap(){
        for(Node node: paneGroup.getChildren()){
            if(node instanceof SVGPath){
                SVGPath svg = (SVGPath) node;
                Tooltip tooltip = new Tooltip();
                tooltip.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
                Font font = new Font("Wawati SC Regular",15);
                tooltip.setFont(font);
                Tooltip.install(svg, tooltip);
                toolTipMap.put(svg.getId(), tooltip);
            }
        }
    }

    private void setImageForPlayer(int playerIndex, String territoryName) {
        if (gameMap.getValue().getTerritoryByName(territoryName).getOwner().getName().equals(UserSession.getInstance().getUsername())) {
            Image image = new Image(getClass().getResourceAsStream(PLAYER_IMAGE_NAMES[playerIndex]));
            playerImage.setImage(image);
        }
    }

    private static final String[] PLAYER_IMAGE_NAMES = {
            "/image/player0.png",
            "/image/player1.png",
            "/image/player2.png",
            "/image/player3.png"
    };

    public void DisplayImage(){
        Set<String> playerSet = new TreeSet<>();
        for (Territory t : gameMap.getValue().getTerritories()) {
            playerSet.add(t.getOwner().getName());
        }
        int capacity = playerSet.size();

        if(capacity==2) {
            setImageForPlayer(0, "Narnia");
            setImageForPlayer(1, "Aranthia");
        }else if(capacity==3){
            setImageForPlayer(0,"Narnia" );
            setImageForPlayer(1,"Hogwarts" );
            setImageForPlayer(2,"Winterfell");
        }else{
            setImageForPlayer(0,"Narnia" );
            setImageForPlayer(1,"Hogwarts" );
            setImageForPlayer(2,"Aranthia");
            setImageForPlayer(3,"Dragonstone");
        }
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

    @FXML
    public void mouseMovedTerritory(MouseEvent me){
        Node source = (Node) me.getSource();
        Territory selectedTerr = null;
        Tooltip tooltip = null;
        if(source instanceof Text){
            Text text = (Text) source;
            selectedTerr = findTerritory(text.getText());
            tooltip = toolTipMap.get(text.getText());
        }else{
            throw new IllegalArgumentException("Invalid source " + source + " for MouseEvent");
        }
        showToolTip(selectedTerr, tooltip, source);

    }

    private void showToolTip(Territory selectedTerr, Tooltip tooltip, Node source) {
        tooltip.setText("Territory name: "+ selectedTerr.getName()+"\n"+
                "Owner: "+selectedTerr.getOwner().getName()+" \n"+
                "Food: "+ String.valueOf(selectedTerr.produceFood().getAmount())+" \n"+
                "Tech: "+ String.valueOf(selectedTerr.produceTech().getAmount())+" \n"+
                "Units: \n"+
                "CIVILIAN  (Level 0): "+String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(0)))+" \n"+
                "INFANTRY  (Level 1): "+String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(1)))+" \n"+
                "CAVALRY   (Level 2): "+String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(2)))+" \n"+
                "TROOPER   (Level 3): "+String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(3)))+" \n"+
                "ARTILLERY (Level 4): "+String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(4)))+" \n"+
                "AIRFORCE  (Level 5): "+String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(5)))+" \n"+
                "ULTRON    (Level 6): "+String.valueOf(selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(6)))+" \n"
        );
        tooltip.show(source, source.localToScreen(source.getBoundsInLocal()).getMinX(), source.localToScreen(source.getBoundsInLocal()).getMaxY());
    }



    @FXML
    public void mouseLeavedTerritory(MouseEvent me){
        Object source = me.getSource();
        Territory selectedTerr = null;
         if(source instanceof Text){
            Text text = (Text) source;
            toolTipMap.get(text.getText()).hide();
        }else{
            throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
        }
    }

    //click the same territory twice pop up the upgrade page
    @FXML
    public void mouseClickedTerritory(MouseEvent me) throws IOException {
        Object source = me.getSource();
        if(source instanceof Text){
            Text t = (Text) me.getSource();
            String terrName = t.getText();
            if(clickCount ==0){
                terrSourceDest[0]=terrName;
            }else{
                terrSourceDest[1]=terrName;
                Player self = server.getSelfStatus(UserSession.getInstance().getUsername());
                List<String> own = self.getTerritories().stream().map(a -> a.getName()).toList();
                List<String> noOwn = gameMap.getValue().getTerritories().stream().map(b -> b.getName()).filter((b) -> !own.contains(t)).toList();
                if(!terrSourceDest[0].equals(terrSourceDest[1])){
                    //move or attack
                    if(own.contains(terrSourceDest[0]) && own.contains(terrSourceDest[1])){
                        showPopupStage(OrderMoveController.getScene(server, terrSourceDest[0],terrSourceDest[1]));
                    }else if(own.contains(terrSourceDest[0]) && noOwn.contains(terrSourceDest[1])){
                        showPopupStage(OrderAttackController.getScene(server, gameMap.getValue(),terrSourceDest[0],terrSourceDest[1]));
                    }
                }else{
                    if(own.contains(terrSourceDest[0])) {
                        showPopupStage(OrderUpgradeController.getScene(server, terrName));
                    }
                }
            }
        }else{
            throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
        }
        clickCount = (clickCount + 1) % 2;
    }



    @FXML
    public void clickOnShortCut(ActionEvent event) throws IOException {
        Scene scene = moveButton.getScene();
        scene.setOnKeyPressed(e -> {
            if (new KeyCodeCombination(KeyCode.M, KeyCombination.META_DOWN).match(e)) {
                // Command + M pressed for move
                try {
                    showPopupStage(OrderMoveController.getScene(server,null,null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (new KeyCodeCombination(KeyCode.A, KeyCombination.META_DOWN).match(e)) {
                // Command + A pressed for attack
                try {
                    showPopupStage(OrderAttackController.getScene(server, gameMap.getValue(),null,null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (new KeyCodeCombination(KeyCode.U, KeyCombination.META_DOWN).match(e)) {
                // Command + U pressed for upgrade
                try {
                    showPopupStage(OrderUpgradeController.getScene(server,null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else if(new KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN).match(e)){
                //command + S pressed for ally
                try {
                    showPopupStage(OrderAllyController.getScene(server, gameMap.getValue()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else if(new KeyCodeCombination(KeyCode.K, KeyCombination.META_DOWN).match(e)){
                //command + K pressed for manufacture
                try {
                    showPopupStage(OrderManufactureController.getScene(server));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
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
        showPopupStage(OrderAttackController.getScene(server, gameMap.getValue(),null,null));
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
        showPopupStage(OrderUpgradeController.getScene(server,null));
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
        showPopupStage(OrderMoveController.getScene(server,null,null));
    }

    private void showPopupStage(Scene newScene) {
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

    @FXML
    public void clickOnAlly(ActionEvent event) throws IOException {
        showPopupStage(OrderAllyController.getScene(server, gameMap.getValue()));
    }

    @FXML
    public void clickOnManfacture(ActionEvent event) throws IOException {
        showPopupStage(OrderManufactureController.getScene(server));
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
        Color customGreen = Color.rgb(187, 204, 187);
        Color customYellow = Color.rgb(204, 204, 153);
        Color customBlue = Color.rgb(102, 136, 170);
        Color customBrown = Color.rgb(136, 136, 102);
        Color[] colorArr = { customGreen, customYellow, customBlue, customBrown };


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
        allyName.setText(String.valueOf(self.getValue().getAlliance()));

        Color textColor = colorMap.get(UserSession.getInstance().getUsername());
        playerName.setTextFill(textColor);
        food.setTextFill(textColor);
        techResource.setTextFill(textColor);
        techLevel.setTextFill(textColor);
        allyName.setTextFill(textColor);

    }

    /**
     * Sets the color of each territory based on the owner's color.
     * The color is retrieved from the colorMap that is initialized in the
     * constructor.
     */
    public void setTerritoryColor() {
        for(Node node:paneGroup.getChildren() ){
            if(node instanceof SVGPath){
                ((SVGPath) node).setFill(colorMap.get(gameMap.getValue().getTerritoryByName(node.getId()).getOwner().getName()));
            }
        }
    }

}
