package com.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.inventory.util.DatabaseUtil;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database
        DatabaseUtil.initializeDatabase();

        // Load the login page
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root, 800, 600); // Set a fixed size

        // Load CSS - Make sure the path is correct
        String css = getClass().getResource("/styles/style.css").toExternalForm();
        System.out.println("Loading CSS from: " + css); // Debug print
        scene.getStylesheets().add(css);

        primaryStage.setTitle("STOCKXPERT");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}