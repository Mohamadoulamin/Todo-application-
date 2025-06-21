package com.todoapp.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JsonDatabaseManager handles all JSON file operations for the To-Do List application.
 */
public class JsonDatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(JsonDatabaseManager.class.getName());
    private static final String JSON_FILE_PATH = "tasks.json";
    
    private final ObjectMapper objectMapper;
    private final AtomicInteger nextId;
    private final File jsonFile;

    /**
     * Constructor that initializes the JSON database manager.
     */
    public JsonDatabaseManager() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File(JSON_FILE_PATH);
        this.nextId = new AtomicInteger(1);
        
        initializeJsonFile();
        LOGGER.info("JSON database manager initialized successfully");
    }

    /**
     * Initializes the JSON file and determines the next available ID.
     */
    private void initializeJsonFile() {
        try {
            if (!jsonFile.exists()) {
                // Create empty JSON array if file doesn't exist
                objectMapper.writeValue(jsonFile, new ArrayList<Task>());
                LOGGER.info("Created new JSON file: " + JSON_FILE_PATH);
            } else {
                // Load existing tasks to determine next ID
                List<Task> existingTasks = loadTasksFromFile();
                int maxId = existingTasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0);
                nextId.set(maxId + 1);
                LOGGER.info("Loaded existing JSON file with " + existingTasks.size() + " tasks. Next ID: " + nextId.get());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize JSON file", e);
            throw new RuntimeException("JSON file initialization failed", e);
        }
    }

    /**
     * Loads all tasks from the JSON file.
     * @return List of tasks from the JSON file
     */
    private List<Task> loadTasksFromFile() throws IOException {
        if (!jsonFile.exists()) {
            return new ArrayList<>();
        }
        
        TypeReference<List<Task>> typeReference = new TypeReference<List<Task>>() {};
        return objectMapper.readValue(jsonFile, typeReference);
    }

    /**
     * Saves all tasks to the JSON file.
     * @param tasks List of tasks to save
     */
    private void saveTasksToFile(List<Task> tasks) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, tasks);
        LOGGER.info("Saved " + tasks.size() + " tasks to JSON file");
    }

    /**
     * Retrieves all tasks from the JSON file.
     * @return ObservableList of all tasks
     */
    public ObservableList<Task> getAllTasks() {
        try {
            List<Task> tasks = loadTasksFromFile();
            LOGGER.info("Retrieved " + tasks.size() + " tasks from JSON file");
            return FXCollections.observableArrayList(tasks);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve tasks from JSON file", e);
            throw new RuntimeException("Failed to retrieve tasks", e);
        }
    }

    /**
     * Adds a new task to the JSON file.
     * @param description The description of the task
     * @return The generated ID of the new task
     */
    public int addTask(String description) {
        try {
            List<Task> tasks = loadTasksFromFile();
            
            int taskId = nextId.getAndIncrement();
            Task newTask = new Task(taskId, description, false);
            tasks.add(newTask);
            
            saveTasksToFile(tasks);
            LOGGER.info("Task added successfully with ID: " + taskId);
            return taskId;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to add task: " + description, e);
            throw new RuntimeException("Failed to add task", e);
        }
    }

    /**
     * Updates the completion status of a task in the JSON file.
     * @param taskId The ID of the task to update
     * @param completed The new completion status
     */
    public void updateTaskCompletion(int taskId, boolean completed) {
        try {
            List<Task> tasks = loadTasksFromFile();
            
            boolean taskFound = false;
            for (Task task : tasks) {
                if (task.getId() == taskId) {
                    task.setCompleted(completed);
                    taskFound = true;
                    break;
                }
            }
            
            if (taskFound) {
                saveTasksToFile(tasks);
                LOGGER.info("Task " + taskId + " completion status updated to: " + completed);
            } else {
                LOGGER.warning("No task found with ID: " + taskId);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to update task completion: " + taskId, e);
            throw new RuntimeException("Failed to update task completion", e);
        }
    }

    /**
     * Deletes a task from the JSON file.
     * @param taskId The ID of the task to delete
     */
    public void deleteTask(int taskId) {
        try {
            List<Task> tasks = loadTasksFromFile();
            
            boolean taskRemoved = tasks.removeIf(task -> task.getId() == taskId);
            
            if (taskRemoved) {
                saveTasksToFile(tasks);
                LOGGER.info("Task " + taskId + " deleted successfully");
            } else {
                LOGGER.warning("No task found with ID: " + taskId);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete task: " + taskId, e);
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    /**
     * Deletes all completed tasks from the JSON file.
     * @return The number of tasks deleted
     */
    public int clearCompletedTasks() {
        try {
            List<Task> tasks = loadTasksFromFile();
            
            int originalSize = tasks.size();
            tasks.removeIf(Task::isCompleted);
            int deletedCount = originalSize - tasks.size();
            
            saveTasksToFile(tasks);
            LOGGER.info("Cleared " + deletedCount + " completed tasks");
            return deletedCount;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to clear completed tasks", e);
            throw new RuntimeException("Failed to clear completed tasks", e);
        }
    }

    /**
     * Saves the current state of tasks to the JSON file.
     * This method can be called to persist the current in-memory task list.
     * @param taskList The current list of tasks to save
     */
    public void saveAllTasks(ObservableList<Task> taskList) {
        try {
            List<Task> tasks = new ArrayList<>(taskList);
            saveTasksToFile(tasks);
            LOGGER.info("Saved all tasks to JSON file");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save all tasks", e);
            throw new RuntimeException("Failed to save all tasks", e);
        }
    }

    /**
     * Gets the path to the JSON file.
     * @return The path to the JSON file
     */
    public String getJsonFilePath() {
        return jsonFile.getAbsolutePath();
    }
}

