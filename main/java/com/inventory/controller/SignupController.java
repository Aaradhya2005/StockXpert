package com.inventory.controller;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backToLoginButton;

    private UserDAO userDAO;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match");
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            messageLabel.setText("Username already exists");
            return;
        }

        // Create new user
        User newUser = new User(username, password);
        if (userDAO.createUser(newUser)) {
            messageLabel.setText("Account created successfully!");
            // Clear fields
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            messageLabel.setText("Failed to create account");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            messageLabel.setText("Error loading login page");
            e.printStackTrace();
        }
    }
}