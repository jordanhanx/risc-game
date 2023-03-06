package edu.duke.ece651.team7.client;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    void test_getName() throws RemoteException {
        Player p = new Player("Green");
        assertEquals(p.getName(), "Green");
    }
}
