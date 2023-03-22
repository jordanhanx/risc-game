package edu.duke.ece651.team7.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AppTest {
  @Test
  public void test_serverApp_main() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);

    PrintStream oldErr = System.err;
    try {
      System.setErr(out);
      App.main(new String[0]);
    } finally {
      System.setErr(oldErr);
    }
    assertEquals("Usage: server <port> <client capacity> <init units per playe>\n", bytes.toString());
  }
}
