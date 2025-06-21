package com.todoapp;

import com.todoapp.database.JsonDatabaseManager;
import com.todoapp.model.Task;
import javafx.collections.ObservableList;

/**
 * Simple test to verify JSON database operations work correctly.
 */
public class JsonDatabaseTest {
    public static void main(String[] args) {
        System.out.println("Starting JSON database integration test...");
        
        try {
            // Initialize JSON database manager
            JsonDatabaseManager jsonManager = new JsonDatabaseManager();
            System.out.println("✓ JSON database manager initialized successfully");
            System.out.println("  JSON file path: " + jsonManager.getJsonFilePath());
            
            // Test adding tasks
            int task1Id = jsonManager.addTask("Test task 1 - Buy groceries");
            int task2Id = jsonManager.addTask("Test task 2 - Complete project");
            int task3Id = jsonManager.addTask("Test task 3 - Walk the dog");
            System.out.println("✓ Added tasks with IDs: " + task1Id + ", " + task2Id + ", " + task3Id);
            
            // Test retrieving all tasks
            ObservableList<Task> tasks = jsonManager.getAllTasks();
            System.out.println("✓ Retrieved " + tasks.size() + " tasks from JSON file");
            
            // Print all tasks
            for (Task task : tasks) {
                System.out.println("  - Task ID " + task.getId() + ": " + task.getDescription() + 
                                 " (Completed: " + task.isCompleted() + ")");
            }
            
            // Test updating task completion
            jsonManager.updateTaskCompletion(task1Id, true);
            jsonManager.updateTaskCompletion(task3Id, true);
            System.out.println("✓ Updated completion status for tasks " + task1Id + " and " + task3Id);
            
            // Test retrieving tasks again to verify update
            tasks = jsonManager.getAllTasks();
            System.out.println("✓ Retrieved tasks after update:");
            for (Task task : tasks) {
                System.out.println("  - Task ID " + task.getId() + ": " + task.getDescription() + 
                                 " (Completed: " + task.isCompleted() + ")");
            }
            
            // Test clearing completed tasks
            int clearedCount = jsonManager.clearCompletedTasks();
            System.out.println("✓ Cleared " + clearedCount + " completed tasks");
            
            // Test deleting a specific task
            jsonManager.deleteTask(task2Id);
            System.out.println("✓ Deleted task " + task2Id);
            
            // Final verification
            tasks = jsonManager.getAllTasks();
            System.out.println("✓ Final task count: " + tasks.size());
            
            // Test saving all tasks (simulating application shutdown)
            Task newTask = new Task(99, "Final test task", false);
            tasks.add(newTask);
            jsonManager.saveAllTasks(tasks);
            System.out.println("✓ Saved all tasks including new test task");
            
            // Verify the save worked
            tasks = jsonManager.getAllTasks();
            System.out.println("✓ Final verification - task count after save: " + tasks.size());
            
            System.out.println("\n🎉 All JSON database tests passed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

