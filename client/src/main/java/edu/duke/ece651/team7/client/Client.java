package edu.duke.ece651.team7.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    /**
   * @param socket client socket used for send and recieve data
   * @param out the datastream for sending data to server
   * @param input the datastream for recieving data from server
   */
  private final Socket socket;
  private final DataOutputStream out;
  private final BufferedReader input;

  /**
   * 
   * @param host hostname(IP address) of the server connected to
   * @param port port numner of the server connected to
   * @throws UnknownHostException
   * @throws IOException
   */
  public Client(String host, int port) throws UnknownHostException, IOException{
    socket = new Socket(host, port);
    System.out.println("Connected to host " + host);
    out = new DataOutputStream(socket.getOutputStream());
    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public void sendString(String message) throws IOException{
    out.writeBytes(message);
  }

  public void recvString() throws IOException{
    String message = input.readLine();
    if(message==null){
      throw new EOFException("Input stream is null");
    }
    System.out.println(message);
   }

  public void closeSocket() throws IOException{
    socket.close();
  }
}
