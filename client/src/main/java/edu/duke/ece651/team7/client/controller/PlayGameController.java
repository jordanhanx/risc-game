package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Node;

import edu.duke.ece651.team7.client.MusicFactory;
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
        return new Scene(loader.load(), 1065, 522);
    }

    @FXML
    private Label playerName, food, techResource, techLevel, ally, airPlane, bomb;
    @FXML
    private Button moveButton, attackButton, upgradeButton, researchButton, shortCutButton;
    @FXML
    private SVGPath Midkemia, Narnia, Oz, Westeros, Gondor, Elantris, Scadrial, Roshar;
    @FXML
    private SVGPath Hogwarts, Mordor, Essos, Dorne, Highgarden, Aranthia, Galadria, Drakoria;
    @FXML
    private SVGPath Dragonstone, Winterfell, Helvoria, Pyke, Volantis, Pentos, Braavos, Oldtown;

    @FXML
    private ImageView playerImage;

    @FXML
    Pane paneGroup;
    @FXML
    private TextField input;
    @FXML
    private Button sendButton;
    @FXML private ListView content;
    @FXML ChoiceBox<String> playerSelector;
    @FXML Pane playerInfoPane;
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
        setChoiceBox();
    }

    /**
     * Sets the choices for the player selector choice box.
     Adds "All" option as well.
     */
    private void setChoiceBox(){
        ObservableList<String> playerList = FXCollections.observableArrayList(
                gameMap.getValue().getTerritories().stream()
                        .map(Territory::getOwner)
                        .filter(player -> !player.getName().equals(UserSession.getInstance().getUsername()))
                        .map(Player::getName)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList()
        ));
        playerList.add("All");
        playerSelector.setItems(playerList);

    }

    /**
     * Sets up tooltips for all the territories on the game map.
     * Tooltips are styled with a custom background color and font.
     * Tooltips are installed on each territory SVGPath object and mapped to their respective territory names.
     */

    private void setToolTipMap() {
        for (Node node : paneGroup.getChildren()) {
            if (node instanceof SVGPath) {
                SVGPath svg = (SVGPath) node;
                Tooltip tooltip = new Tooltip();
                tooltip.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
                Font font = new Font("Wawati SC Regular", 15);
                tooltip.setFont(font);
                Tooltip.install(svg, tooltip);
                toolTipMap.put(svg.getId(), tooltip);
            }
        }
    }

    /**
     Sets the player image for this player
     @param playerIndex the index of the player
     @param territoryName the name of the territory (used to determine which territory's owner to check)
     */
    private void setImageForPlayer(int playerIndex, String territoryName) {
        if (gameMap.getValue().getTerritoryByName(territoryName).getOwner().getName()
                .equals(UserSession.getInstance().getUsername())) {
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

    /**
     * Display the player's image
     */
    public void DisplayImage() {
        Set<String> playerSet = new TreeSet<>();
        for (Territory t : gameMap.getValue().getTerritories()) {
            playerSet.add(t.getOwner().getName());
        }
        int capacity = playerSet.size();

        if (capacity == 2) {
            setImageForPlayer(0, "Narnia");
            setImageForPlayer(1, "Aranthia");
        } else if (capacity == 3) {
            setImageForPlayer(0, "Narnia");
            setImageForPlayer(1, "Hogwarts");
            setImageForPlayer(2, "Winterfell");
        } else {
            setImageForPlayer(0, "Narnia");
            setImageForPlayer(1, "Hogwarts");
            setImageForPlayer(2, "Aranthia");
            setImageForPlayer(3, "Dragonstone");
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
            setChoiceBox();
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
     * Handles mouse movement over a territory Text element. Shows a Tooltip with information about the territory.
     * @param me the MouseEvent triggered by the mouse movement
     * @throws IllegalArgumentException if the source of the MouseEvent is not a Text element
     */
    @FXML
    public void mouseMovedTerritory(MouseEvent me) {
        Node source = (Node) me.getSource();
        Territory selectedTerr = null;
        Tooltip tooltip = null;
        if (source instanceof Text) {
            Text text = (Text) source;
            selectedTerr = findTerritory(text.getText());
            tooltip = toolTipMap.get(text.getText());
        } else {
            throw new IllegalArgumentException("Invalid source " + source + " for MouseEvent");
        }
        showToolTip(selectedTerr, tooltip, source);

    }

    /**
     * Shows a Tooltip with information about a territory.
     * @param selectedTerr the territory for which to show the Tooltip
     * @param tooltip the Tooltip element to show
     * @param source the Node element over which the Tooltip will be shown
     */
    private void showToolTip(Territory selectedTerr, Tooltip tooltip, Node source) {
        Player terrAlly= selectedTerr.getOwner().getAlliance();
        StringBuilder tooltipBuilder = new StringBuilder();
        tooltipBuilder.append("Territory name: ").append(selectedTerr.getName()).append("\n")
                .append("Owner: ").append(selectedTerr.getOwner().getName()).append(" \n")
                .append("Food: ").append(selectedTerr.produceFood().getAmount()).append(" \n")
                .append("Tech: ").append(selectedTerr.produceTech().getAmount()).append(" \n")
                .append("Units: \n");
        String[] levelLabels = {"CIVILIAN", "INFANTRY", "CAVALRY", "TROOPER", "ARTILLERY", "AIRFORCE", "ULTRON"};
        int[] unitCounts = new int[7];
        for (int i = 0; i < 7; i++) {
            unitCounts[i] = selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(i));
            if (terrAlly != null) {
                unitCounts[i] += selectedTerr.getUnitsNumberByLevel(Level.valueOfLabel(i), terrAlly);
            }
            tooltipBuilder.append(levelLabels[i]).append(" (Level ").append(i).append("): ")
                    .append(unitCounts[i]).append(" \n");
        }
        tooltip.setText(tooltipBuilder.toString());
        tooltip.show(source, source.localToScreen(source.getBoundsInLocal()).getMinX(),
                source.localToScreen(source.getBoundsInLocal()).getMaxY());
    }

    /**
     * Handles mouse leaving a territory Text element. Hides the corresponding Tooltip.
     * @param me the MouseEvent triggered by the mouse movement
     * @throws IllegalArgumentException if the source of the MouseEvent is not a Text element
     */
    @FXML
    public void mouseLeavedTerritory(MouseEvent me) {
        Object source = me.getSource();
        Territory selectedTerr = null;
        if (source instanceof Text) {
            Text text = (Text) source;
            toolTipMap.get(text.getText()).hide();
        } else {
            throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
        }
    }

    /**
     * This method handles territory clicks. If it is the first click, the
     * territory name is stored as the source. If it is the second click, it is stored as
     * the destination.
     @param mouseEvent The MouseEvent object generated by the user clicking a territory.
     @throws IOException if there is an error handling the territory selection.
     */
    @FXML
    public void mouseClickedTerritory(MouseEvent mouseEvent) throws IOException {
        Object source = mouseEvent.getSource();
        if (!(source instanceof Text)) {
            throw new IllegalArgumentException("Invalid source " + source + " for MouseEvent");
        }

        Text territoryText = (Text) source;
        String territoryName = territoryText.getText();

        if (clickCount == 0) {
            terrSourceDest[0] = territoryName;
        } else {
            terrSourceDest[1] = territoryName;
            handleTerritorySelection();
        }

        clickCount = (clickCount + 1) % 2;
    }

    /**
     * This method handles territory selection logic based on the source and destination territories clicked by the user.
     * It opens the upgrade window for the same territory clicked twice in a row. If the source (first click) and destination (second click) territory
     * both belong to the player, it opens the move page. If the destination territory does not belong to the player, it opens
     * the attack page.
     * @throws IOException if there is an error opening the appropriate popup window.
     */
    private void handleTerritorySelection() throws IOException {
        Player self = server.getSelfStatus(UserSession.getInstance().getUsername());
        List<String> ownedTerritories = self.getTerritories().stream()
                .map(Territory::getName)
                .toList();
        List<String> unownedTerritories = gameMap.getValue().getTerritories().stream()
                .map(Territory::getName)
                .filter(name -> !ownedTerritories.contains(name))
                .toList();

        if (terrSourceDest[0].equals(terrSourceDest[1])) {
            if (ownedTerritories.contains(terrSourceDest[0])) {
                showPopupStage(OrderUpgradeController.getScene(server, terrSourceDest[0]));
            }
            return;
        }

        if (ownedTerritories.contains(terrSourceDest[0]) && ownedTerritories.contains(terrSourceDest[1])) {
            showPopupStage(OrderMoveController.getScene(server, terrSourceDest[0], terrSourceDest[1]));
        } else if (ownedTerritories.contains(terrSourceDest[0]) && unownedTerritories.contains(terrSourceDest[1])) {
            showPopupStage(OrderAttackController.getScene(server, gameMap.getValue(), terrSourceDest[0], terrSourceDest[1]));
        }
    }


    /**
     * The method set the shortcut for the move, attack, uograde, ally, manufacture order
     * Commad + M pressed for move, Command + A pressed for attack, Command + U pressed for upgrade
     * Command + S pressed for ally, Command + K pressed for manufacture
     @throws IOException if there is an error handling the keyboard shortcut.
     */
    @FXML
    public void clickOnShortCut(ActionEvent event) throws IOException {
        Scene scene = moveButton.getScene();
        scene.setOnKeyPressed(e -> {
            if (new KeyCodeCombination(KeyCode.M, KeyCombination.META_DOWN).match(e)) {
                // Command + M pressed for move
                try {
                    showPopupStage(OrderMoveController.getScene(server, null, null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (new KeyCodeCombination(KeyCode.A, KeyCombination.META_DOWN).match(e)) {
                // Command + A pressed for attack
                try {
                    showPopupStage(OrderAttackController.getScene(server, gameMap.getValue(), null, null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (new KeyCodeCombination(KeyCode.U, KeyCombination.META_DOWN).match(e)) {
                // Command + U pressed for upgrade
                try {
                    showPopupStage(OrderUpgradeController.getScene(server, null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (new KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN).match(e)) {
                // command + S pressed for ally
                try {
                    showPopupStage(OrderAllyController.getScene(server, gameMap.getValue()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (new KeyCodeCombination(KeyCode.K, KeyCombination.META_DOWN).match(e)) {
                // command + K pressed for manufacture
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
        showPopupStage(OrderAttackController.getScene(server, gameMap.getValue(), null, null));
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
        showPopupStage(OrderUpgradeController.getScene(server, null));
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
        showPopupStage(OrderMoveController.getScene(server, null, null));
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
     * Event handler for the "Ally" button.
     * It opens the order-ally-page to allow the player to make ally orders.
     *
     * @param event The ActionEvent triggered by clicking the "Ally" button.
     * @throws IOException if there is an error while opening the scene.
     */
    @FXML
    public void clickOnAlly(ActionEvent event) throws IOException {
        showPopupStage(OrderAllyController.getScene(server, gameMap.getValue()));
    }

    /**
     * Event handler for the "Manufacture" button.
     * It opens the order-manufacture-page to allow the player to make manufacture orders.
     *
     * @param event The ActionEvent triggered by clicking the "Manufacture" button.
     * @throws IOException if there is an error while opening the scene.
     */
    @FXML
    public void clickOnManufacture(ActionEvent event) throws IOException {
        showPopupStage(OrderManufactureController.getScene(server));
    }

    /**
     * Shows a popup stage with the specified scene. This method is used to display scenes for order controllers.
     * @param newScene the scene to be displayed in the popup stage.
     */
    private void showPopupStage(Scene newScene) {
        Stage popupStage = new Stage();
        popupStage.setScene(newScene);
        popupStage.initOwner(playerName.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.showAndWait();
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
            MediaPlayer actionFailedPlayer = MusicFactory.createActionFailedPlayer();
            actionFailedPlayer.play();
            throw new IllegalArgumentException(response);
        }
        MediaPlayer commitPlayer = MusicFactory.createCommitPlayer();
        commitPlayer.play();
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
     * food and tech resources, current tech level, ally's name, .
     */
    public void setPlayerInfo() {
        playerName.setText(UserSession.getInstance().getUsername());
        food.setText(String.valueOf(self.getValue().getFood().getAmount()));
        techResource.setText(String.valueOf(self.getValue().getTech().getAmount()));
        techLevel.setText(String.valueOf(self.getValue().getCurrentMaxLevel()) + " (level"
                + String.valueOf(self.getValue().getCurrentMaxLevel().label) + ")");
        if(self.getValue().getAlliance() != null) {
            ally.setText(String.valueOf(self.getValue().getAlliance().getName()));
        }else{
            ally.setText("No Ally");
        }
        airPlane.setText(String.valueOf(self.getValue().getAircraft().size()));
        bomb.setText(String.valueOf(self.getValue().getBomb()));

        Color textColor = colorMap.get(UserSession.getInstance().getUsername());
        for(Node node: playerInfoPane.getChildren()){
            if(node instanceof Label){
                ((Label) node).setTextFill(Color.BLACK);
            }
        }
    }

    /**
     * Sets the color of each territory based on the owner's color.
     * The color is retrieved from the colorMap that is initialized in the
     * constructor.
     */
    public void setTerritoryColor() {
        for (Node node : paneGroup.getChildren()) {
            if (node instanceof SVGPath) {
                ((SVGPath) node).setFill(
                        colorMap.get(gameMap.getValue().getTerritoryByName(node.getId()).getOwner().getName()));
            }
        }
    }

    /**
     * Event handler for the "Send" button.
     * Sends a chat message to either all players or a specific player, depending on the selected player in the GUI.
     *
     * @param event The ActionEvent triggered by clicking the "Send" button.
     * @throws RemoteException          If a remote method invocation error occurs
     *                                  while attempting to commit the orders.
     * @throws IllegalArgumentException If the server returns an error message.
     */
    @FXML
    public void clickOnSend(ActionEvent event) throws RemoteException {
        String response=null;
        if(playerSelector.getValue().equals("All")) {
            response = server.chatToAll(UserSession.getInstance().getUsername(),input.getText());
        }else{
            response= server.chatToPlayer(UserSession.getInstance().getUsername(), playerSelector.getSelectionModel().getSelectedItem(),input.getText());
        }
        if (response != null) {
            throw new IllegalArgumentException(response);
        }
        input.clear();
    }

    /**
     * Overrides the showChatMessage method and displays a chat message
     * in the GUI.
     * @param msg the chat message to be displayed in the GUI.
     * @throws RemoteException if there is an error communicating with the server.
     */
    @Override
    public void showChatMessage(String msg) throws RemoteException {
        Platform.runLater(() -> {
            displayContent(msg);
        });
    }

    /**
     * Displays the chat message in the GUI, by creating an HBox containing a Label with the message text, and adding
     * the HBox to the content ListView in the GUI.
     * @param text the text of the chat message to be displayed in the GUI.
     */
    private void displayContent(String text){
        HBox hBox = new HBox();
        Label msg = new Label(text);
        hBox.getChildren().addAll(msg);
        hBox.setStyle("-fx-background-color: #D2B48C; -fx-background-radius: 5px;-fx-border-color: #000000;  -fx-border-width: 2px;");
        content.getItems().add(hBox);
    }

}
