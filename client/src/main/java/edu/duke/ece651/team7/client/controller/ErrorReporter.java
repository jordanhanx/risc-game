package edu.duke.ece651.team7.client.controller;

import javafx.scene.control.Alert;

public class ErrorReporter implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thr, Throwable err) {
        // put this in for debugging: error.printStackTrace();
        while (err.getCause() != null) {
            err = err.getCause();
        }
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setHeaderText(err.getClass().getName());
        dialog.setContentText(err.getMessage());
        dialog.showAndWait();
    }

}
