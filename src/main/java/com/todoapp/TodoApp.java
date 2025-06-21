package com.todoapp;

import com.todoapp.controller.TodoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Main application class for the JavaFX To-Do List application with JSON database integration.
 */
public class TodoApp extends Application {
    private static final Logger LOGGER = Logger.getLogger(TodoApp.class.getName());
    private TodoController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TodoApp.class.getResource("/fxml/todo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        
        // Get the controller to handle cleanup later
        controller = fxmlLoader.getController();
        
        stage.setTitle("To-Do List Application with JSON Database");
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(350);
        
        // Handle application shutdown to cleanup and save JSON data
        stage.setOnCloseRequest(event -> {
            LOGGER.info("Application is closing, performing cleanup...");
            if (controller != null) {
                controller.cleanup();
            }
        });
        
        stage.show();
        LOGGER.info("To-Do List application with JSON database started successfully");
    }

    @Override
    public void stop() throws Exception {
        // Additional cleanup when application stops
        if (controller != null) {
            controller.cleanup();
        }
        LOGGER.info("Application stopped and JSON data saved");
        super.stop();
    }

    public static void main(String[] args) {
        LOGGER.info("Starting To-Do List application with JSON database...");
        launch();
    }
}

