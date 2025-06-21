# JavaFX To-Do List Application with JSON Database Integration

A comprehensive to-do list application built with JavaFX, featuring a clean GUI and JSON file-based database integration for persistent task storage.

## Features

- **Add Tasks**: Easily add new tasks to your to-do list
- **Mark as Complete**: Check off tasks as you complete them with real-time JSON file updates
- **Delete Tasks**: Remove individual tasks from the list
- **Clear Completed**: Remove all completed tasks at once
- **JSON Database Persistence**: All tasks are automatically saved to a human-readable JSON file
- **Responsive Design**: Clean and user-friendly interface
- **Error Handling**: Comprehensive error handling with user-friendly alerts
- **Logging**: Detailed logging for debugging and monitoring

## JSON Database Integration

This application uses a JSON file for persistent storage with the following features:
- **Automatic File Creation**: JSON file and structure are created automatically on first run
- **Real-time Updates**: Changes are immediately saved to the JSON file
- **Data Persistence**: Tasks persist between application sessions
- **Human-Readable Format**: JSON data can be easily inspected and manually edited
- **CRUD Operations**: Full Create, Read, Update, Delete functionality
- **File Safety**: Proper error handling and file I/O management

## Screenshots

![To-Do List Application](screenshot.png)

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Installation and Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/javafx-todo-list.git
   cd javafx-todo-list
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Run the application**:
   ```bash
   mvn javafx:run
   ```

4. **Run JSON database tests** (optional):
   ```bash
   mvn test-compile
   java -cp "target/classes:target/test-classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" com.todoapp.JsonDatabaseTest
   ```

## Project Structure

```
javafx-todo-list/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── todoapp/
│       │           ├── TodoApp.java              # Main application class
│       │           ├── controller/
│       │           │   └── TodoController.java   # Controller with JSON integration
│       │           ├── database/
│       │           │   └── JsonDatabaseManager.java  # JSON file operations
│       │           └── model/
│       │               └── Task.java             # Enhanced Task model with JSON support
│       └── resources/
│           └── fxml/
│               └── todo-view.fxml               # FXML layout file
├── src/test/java/com/todoapp/
│   └── JsonDatabaseTest.java                   # JSON database integration tests
├── pom.xml                                      # Maven configuration with Jackson
├── tasks.json                                   # JSON database file (created at runtime)
└── README.md                                   # This file
```

## JSON Database Schema

The application uses a simple JSON file with the following structure:

```json
[
  {
    "id": 1,
    "description": "Buy groceries",
    "completed": false
  },
  {
    "id": 2,
    "description": "Complete project",
    "completed": true
  }
]
```

## Usage

1. **Adding a Task**: Type your task description in the input field and click "Add Task" or press Enter.

2. **Marking as Complete**: Click the checkbox in the "Done" column to mark a task as completed. Changes are automatically saved to the JSON file.

3. **Deleting a Task**: Select a task from the list and click "Delete Selected".

4. **Clearing Completed Tasks**: Click "Clear Completed" to remove all tasks marked as done.

5. **Data Persistence**: All changes are automatically saved to the `tasks.json` file and will persist when you restart the application.

## Building for Distribution

To create a JAR file for distribution:

```bash
mvn clean package
```

The JAR file will be created in the `target/` directory.

## Technologies Used

- **JavaFX 17**: For the graphical user interface
- **Jackson**: For JSON processing and serialization
- **Maven**: For project management and build automation
- **FXML**: For declarative UI layout
- **Java Logging API**: For comprehensive application logging

## JSON Database Operations

The application supports the following JSON file operations:
- **CREATE**: Add new tasks with auto-generated IDs
- **READ**: Retrieve all tasks on application startup
- **UPDATE**: Modify task completion status in real-time
- **DELETE**: Remove individual tasks or clear completed tasks

## Error Handling

The application includes comprehensive error handling:
- JSON file I/O failures are handled gracefully
- User-friendly error dialogs for JSON operations
- Automatic file creation if JSON file is missing
- Detailed logging for debugging purposes

## JSON File Management

- **Location**: `tasks.json` in the project root directory
- **Format**: Pretty-printed JSON for human readability
- **Backup**: Consider backing up the JSON file to preserve your tasks
- **Manual Editing**: The JSON file can be manually edited if needed

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is open source and available under the [MIT License](LICENSE).

## Future Enhancements

- [ ] Task categories and tags
- [ ] Due dates and reminders
- [ ] Task priority levels
- [ ] Search and filter functionality
- [ ] Dark mode theme
- [ ] Export tasks to different formats
- [ ] Task synchronization across devices
- [ ] JSON file backup and restore features
- [ ] Import/export from other formats

## Development Log

For detailed information about the JSON database integration process, see [json_database_integration_log.md](json_database_integration_log.md).

## Contact

If you have any questions or suggestions, feel free to open an issue or contact the maintainer.

