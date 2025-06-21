package com.todoapp.controller;

import com.todoapp.database.JsonDatabaseManager;
import com.todoapp.model.Task;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for the To-Do List application with JSON database integration.
 */
public class TodoController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(TodoController.class.getName());

    @FXML
    private TextField taskInput;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Boolean> completedColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button clearCompletedButton;

    private ObservableList<Task> taskList;
    private JsonDatabaseManager jsonDatabaseManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Initialize JSON database manager
            jsonDatabaseManager = new JsonDatabaseManager();
            LOGGER.info("JSON database manager initialized successfully");

            // Load tasks from JSON file
            taskList = jsonDatabaseManager.getAllTasks();
            taskTable.setItems(taskList);
            LOGGER.info("Loaded " + taskList.size() + " tasks from JSON file");

            // Set up table columns
            completedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
            completedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(completedColumn));
            completedColumn.setEditable(true);

            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            // Make the table editable
            taskTable.setEditable(true);

            // Set up button actions
            addButton.setOnAction(event -> addTask());
            deleteButton.setOnAction(event -> deleteSelectedTask());
            clearCompletedButton.setOnAction(event -> clearCompletedTasks());

            // Allow adding tasks by pressing Enter in the text field
            taskInput.setOnAction(event -> addTask());

            // Enable/disable delete button based on selection
            taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> deleteButton.setDisable(newValue == null)
            );

            // Initially disable delete button
            deleteButton.setDisable(true);

            // Listen for changes in task completion status and update JSON file
            setupCompletionListener();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize TodoController", e);
            showErrorAlert("Initialization Error", "Failed to initialize the application: " + e.getMessage());
        }
    }

    /**
     * Sets up a listener to detect changes in task completion status and update the JSON file.
     */
    private void setupCompletionListener() {
        for (Task task : taskList) {
            task.completedProperty().addListener((observable, oldValue, newValue) -> {
                if (task.getId() != -1) { // Only update if task has been saved
                    try {
                        jsonDatabaseManager.updateTaskCompletion(task.getId(), newValue);
                        LOGGER.info("Updated completion status for task ID " + task.getId() + " to " + newValue);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to update task completion in JSON file", e);
                        showErrorAlert("JSON Error", "Failed to update task completion: " + e.getMessage());
                        // Revert the change in the UI
                        task.setCompleted(oldValue);
                    }
                }
            });
        }
    }

    /**
     * Adds a new task to the JSON file and updates the UI.
     */
    @FXML
    private void addTask() {
        String taskDescription = taskInput.getText().trim();
        if (!taskDescription.isEmpty()) {
            try {
                // Add task to JSON file
                int taskId = jsonDatabaseManager.addTask(taskDescription);
                
                // Create task object with the generated ID
                Task newTask = new Task(taskId, taskDescription, false);
                
                // Add completion listener for the new task
                newTask.completedProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        jsonDatabaseManager.updateTaskCompletion(newTask.getId(), newValue);
                        LOGGER.info("Updated completion status for task ID " + newTask.getId() + " to " + newValue);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to update task completion in JSON file", e);
                        showErrorAlert("JSON Error", "Failed to update task completion: " + e.getMessage());
                        // Revert the change in the UI
                        newTask.setCompleted(oldValue);
                    }
                });
                
                // Add to UI list
                taskList.add(newTask);
                taskInput.clear();
                taskInput.requestFocus();
                
                LOGGER.info("Task added successfully: " + taskDescription + " with ID: " + taskId);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to add task to JSON file", e);
                showErrorAlert("JSON Error", "Failed to add task: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes the currently selected task from the JSON file and UI.
     */
    @FXML
    private void deleteSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                // Delete from JSON file
                jsonDatabaseManager.deleteTask(selectedTask.getId());
                
                // Remove from UI list
                taskList.remove(selectedTask);
                
                LOGGER.info("Task deleted successfully: " + selectedTask.getDescription() + " with ID: " + selectedTask.getId());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to delete task from JSON file", e);
                showErrorAlert("JSON Error", "Failed to delete task: " + e.getMessage());
            }
        }
    }

    /**
     * Clears all completed tasks from the JSON file and UI.
     */
    @FXML
    private void clearCompletedTasks() {
        try {
            // Clear completed tasks from JSON file
            int deletedCount = jsonDatabaseManager.clearCompletedTasks();
            
            // Remove completed tasks from UI list
            taskList.removeIf(Task::isCompleted);
            
            LOGGER.info("Cleared " + deletedCount + " completed tasks");
            
            if (deletedCount > 0) {
                showInfoAlert("Tasks Cleared", "Successfully cleared " + deletedCount + " completed tasks.");
            } else {
                showInfoAlert("No Tasks to Clear", "No completed tasks found to clear.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to clear completed tasks from JSON file", e);
            showErrorAlert("JSON Error", "Failed to clear completed tasks: " + e.getMessage());
        }
    }

    /**
     * Shows an error alert dialog.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information alert dialog.
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Cleanup method to save any pending changes when the application shuts down.
     * This should be called when the application is closing.
     */
    public void cleanup() {
        if (jsonDatabaseManager != null) {
            try {
                // Save current state to JSON file
                jsonDatabaseManager.saveAllTasks(taskList);
                LOGGER.info("Final save to JSON file completed during cleanup");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to save tasks during cleanup", e);
            }
        }
    }
}

