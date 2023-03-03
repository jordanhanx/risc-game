package edu.duke.ece651.team7.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
  /**
   * @param server server socket to connect clients
   * @param numClients number of possible clients
   * @param clients vector of client sockets
   */
  private final ServerSocket server;
  int numClients;
  private Vector<Socket> clients;

  /**
   * @param port port number the server is listening to
   * @param nc number of clients
   * @throws IOException
   */
  public Server(int port, int nc) throws IOException {
    server = new ServerSocket(port);
    clients = new Vector<Socket>();
    numClients = nc;
    System.out.println("Waiting for connection");
    // out = socket.getReceiveBufferSize()
  }

  public void connectClients() throws IOException{
    for(int i = 0; i < numClients; i++){
      Socket newClient = server.accept();
      clients.add(newClient);
      System.out.println("Connected");
    }
  }

  public void readData() throws IOException{
    BufferedReader input = new BufferedReader(new InputStreamReader(clients.elementAt(0).getInputStream()));
   String message = input.readLine();
   if(message == null){
     throw new EOFException("Input stream is null");
   }
   System.out.println(message);
  }

  public void sendData(String message) throws IOException{
   DataOutputStream out = new DataOutputStream(clients.elementAt(0).getOutputStream());
   out.writeBytes(message);
  }
}
