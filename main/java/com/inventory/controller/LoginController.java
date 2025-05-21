package com.inventory.controller;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        User user = userDAO.authenticateUser(username, password);
        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inventory.fxml"));
                Parent root = loader.load();

                // Get the controller and set the user ID
                InventoryController controller = loader.getController();
                controller.setCurrentUserId(user.getId());

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Inventory Man");
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Error loading inventory page");
            }
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

    @FXML
    private void handleSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading signup page");
        }
    }

    @FXML
    private void handleAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Login");
        } catch (IOException e) {
            messageLabel.setText("Error loading admin login page");
            e.printStackTrace();
        }
    }
}