package edu.duke.ece651.team7.client.Controller;
import edu.duke.ece651.team7.client.Model.LoginModel;
import edu.duke.ece651.team7.client.PlayerInfoTest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class ContinueGameController implements Initializable{
    @FXML
    private TableView<PlayerInfoTest> gameInfoTable;
    @FXML
    private TableColumn<PlayerInfoTest, Integer> gameIDTable, playersNumTable;
    @FXML
    private TableColumn<PlayerInfoTest, String> buttonTable;
    private final Stage window;

    public ContinueGameController(Stage window){
        this.window = window;
    }

    /*
    private ObservableList<PlayerInfoTest> getGameData(){

        //get the data from GameModel
        //return the ObservableList
        //temporary use the PlayerInfoTest

    }
    */

    //show the game lists that belong to this player
    private void showGameTable(ObservableList<PlayerInfoTest> gameList){

        //get the real data from GameModel
        gameIDTable.setCellValueFactory(new PropertyValueFactory<>("gameID"));
        playersNumTable.setCellValueFactory(new PropertyValueFactory<>("playerNum"));
        buttonTable.setCellFactory((col) -> {
            TableCell<PlayerInfoTest, String> cell = new TableCell<PlayerInfoTest, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        Button enterBtn = new Button("Enter Game");
                        this.setGraphic(enterBtn);
                        enterBtn.setOnMouseClicked((me) -> {

                            //go to the map
                            NewGameController mp = new NewGameController(window);
                            try {
                                mp.showMap(2);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        });
                    }
                }

            };
            return cell;
        });
        gameInfoTable.setItems(gameList);
    }


    @FXML
    public void clickOnExit() throws IOException {
        //go back to the map
        LoginSignupController gm = new LoginSignupController(new LoginModel(),window);
        try {
            gm.showMainPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PlayerInfoTest player1 = new PlayerInfoTest(1,2);
        ObservableList<PlayerInfoTest> gameListTest = FXCollections.observableArrayList(player1);
        showGameTable(gameListTest);

    }


}
