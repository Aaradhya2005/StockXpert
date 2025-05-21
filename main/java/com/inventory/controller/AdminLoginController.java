package com.inventory.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleAdminLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if ("admin123".equals(username) && "admin@123".equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Admin Panel");
            } catch (Exception e) {
                messageLabel.setText("Error loading admin panel");
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid admin credentials");
        }
    }

    @FXML
    private void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            messageLabel.setText("Error loading login page");
            e.printStackTrace();
        }
    }
}