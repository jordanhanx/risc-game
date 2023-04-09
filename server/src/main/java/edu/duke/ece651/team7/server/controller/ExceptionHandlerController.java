package edu.duke.ece651.team7.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class handles exceptions that occur in the application and returns an
 * HTTP response with a message describing the error that occurred.
 */
@ControllerAdvice
public class ExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    /**
     * Handles any Exception that occurs in the application and logs the exception
     * message before returning an HTTP response with the same message.
     * 
     * @param ex The Exception that occurred
     * @return A ResponseEntity with an HTTP status code of 500 and the exception
     *         message in the response body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error(ex.getMessage()); // only log the exception message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}