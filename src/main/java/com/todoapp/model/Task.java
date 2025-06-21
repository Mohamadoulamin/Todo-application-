package com.todoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class representing a single task in the to-do list.
 * Compatible with Jackson JSON serialization/deserialization.
 */
public class Task {
    private final IntegerProperty id;
    private final StringProperty description;
    private final BooleanProperty completed;

    /**
     * Default constructor for Jackson deserialization.
     */
    public Task() {
        this.id = new SimpleIntegerProperty(-1);
        this.description = new SimpleStringProperty("");
        this.completed = new SimpleBooleanProperty(false);
    }

    /**
     * Constructor for creating a new task.
     * @param description The description of the task
     */
    public Task(String description) {
        this.id = new SimpleIntegerProperty(-1); // -1 indicates not yet saved
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(false);
    }

    /**
     * Constructor for creating a task with a specific completion status.
     * @param description The description of the task
     * @param completed Whether the task is completed
     */
    public Task(String description, boolean completed) {
        this.id = new SimpleIntegerProperty(-1); // -1 indicates not yet saved
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
    }

    /**
     * Constructor for creating a task with all properties (used when loading from JSON).
     * @param id The ID of the task
     * @param description The description of the task
     * @param completed Whether the task is completed
     */
    public Task(int id, String description, boolean completed) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.completed = new SimpleBooleanProperty(completed);
    }

    // JSON serialization getters and setters
    @JsonProperty("id")
    public int getId() {
        return id.get();
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id.set(id);
    }

    @JsonProperty("description")
    public String getDescription() {
        return description.get();
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description.set(description);
    }

    @JsonProperty("completed")
    public boolean isCompleted() {
        return completed.get();
    }

    @JsonProperty("completed")
    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    // JavaFX property getters (ignored by Jackson)
    @JsonIgnore
    public IntegerProperty idProperty() {
        return id;
    }

    @JsonIgnore
    public StringProperty descriptionProperty() {
        return description;
    }

    @JsonIgnore
    public BooleanProperty completedProperty() {
        return completed;
    }

    @Override
    public String toString() {
        return getDescription() + (isCompleted() ? " (Completed)" : " (Pending)");
    }
}

