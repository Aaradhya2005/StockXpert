# Inventory Management System

A JavaFX application for managing inventory with user authentication.

## Prerequisites

- Java 17 or higher
- MySQL Server
- Maven

## Setup

1. Make sure MySQL Server is running on your machine
2. Create a MySQL user with the following credentials:
   - Username: root
   - Password: root
   (Or update the credentials in `DatabaseUtil.java` to match your MySQL setup)

## Running the Application

1. Clone the repository
2. Open a terminal in the project directory
3. Run the following command to build and run the application:
   ```bash
   mvn clean javafx:run
   ```

## Features

- User registration and login
- Secure password storage
- Input validation
- Database persistence

## Project Structure

- `src/main/java/com/inventory/`
  - `Main.java` - Application entry point
  - `controller/` - FXML controllers
  - `model/` - Data models
  - `dao/` - Data Access Objects
  - `util/` - Utility classes
- `src/main/resources/fxml/` - FXML files for UI 