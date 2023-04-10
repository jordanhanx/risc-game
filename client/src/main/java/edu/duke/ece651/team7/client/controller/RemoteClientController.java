package edu.duke.ece651.team7.client.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.duke.ece651.team7.shared.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class RemoteClientController extends UnicastRemoteObject implements RemoteClient {

    public static Scene getScene(int capacity) throws IOException {
        String url = "";
        if (capacity == 2) {
            url = "/fxml/GameMapTwoPlayers.fxml";
        } else if (capacity == 3) {
            // url = "/fxml/GameMapTwoPlayers.fxml";
        } else if (capacity == 4) {
            // url = "/fxml/GameMapTwoPlayers.fxml";
        } else {
            throw new IllegalStateException("No Game Map supports capacity = " + capacity);
        }
        URL xmlResource = LoginSignupController.class.getResource(url);
        FXMLLoader loader = new FXMLLoader(xmlResource);
        return new Scene(loader.load(), 1325, 607);
    }

    private GameMap gameMap;

    @FXML
    private ListView<Territory> territoriyList;
    @FXML
    private TextField techLevel;
    @FXML
    private TextField food;
    @FXML
    private TextField techResource;

    protected RemoteClientController() throws RemoteException {
        super();
    }

    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void doDisplay(GameMap map) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doDisplay'");
    }

    @Override
    public void doDisplay(String msg) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doDisplay'");
    }

    @Override
    public void close() throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

}
