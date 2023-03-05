package edu.duke.ece651.team7.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ImplRemotePlayer extends UnicastRemoteObject implements RemotePlayer {
    private final String msg;

    public ImplRemotePlayer(String msg) throws RemoteException {
        super();
        this.msg = msg;
    }

    @Override
    public String getMsg() throws RemoteException {
        return msg;
    }
}
