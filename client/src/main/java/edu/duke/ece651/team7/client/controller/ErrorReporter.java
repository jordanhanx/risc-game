package edu.duke.ece651.team7.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;

public class ErrorReporter implements Thread.UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorReporter.class);

    @Override
    public void uncaughtException(Thread thr, Throwable err) {
        logger.error(err.getMessage(), err);
        while (err.getCause() != null) {
            err = err.getCause();
        }
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setHeaderText(err.getClass().getName());
        dialog.setContentText(err.getMessage());
        dialog.showAndWait();
    }

}
