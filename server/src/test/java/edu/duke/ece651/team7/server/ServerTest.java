package edu.duke.ece651.team7.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import edu.duke.ece651.team7.shared.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServerTest {
  @Test
  public void test_groupTerritory() throws RemoteException {
    MapFactory factory  = new TextMapFactory();
    Server server = new Server(2,System.out, 10, factory.createMapTest());
    for(ArrayList<Territory> a: server.getTerritoryGroups()){
      for(Territory t: a){
        System.out.print(t.getName());
        System.out.print(": "+t.getUnits());
        System.out.print(" ");
      }
      System.out.println("Group + 1");
    }
  }
}
