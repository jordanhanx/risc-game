package edu.duke.ece651.team7.client.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import javafx.application.Platform;

@ExtendWith(ApplicationExtension.class)
public class ErrorReporterTest {
    @Test
    void test_uncaughtException() {
        Platform.runLater(() -> {
            ErrorReporter errorReporter = new ErrorReporter();
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setErr(new PrintStream(outContent));
            errorReporter.uncaughtException(Thread.currentThread(), new RuntimeException("Test exception"));
            String expectedOutput = "Test exception\n";
            String actualOutput = outContent.toString();
            assertTrue(actualOutput.contains(expectedOutput));
        });
    }
}
