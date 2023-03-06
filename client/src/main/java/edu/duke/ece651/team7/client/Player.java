package edu.duke.ece651.team7.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.duke.ece651.team7.shared.*;

public class Player extends UnicastRemoteObject implements RemotePlayer {
    private final String name;

    public Player(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }
}
