package com.inventory.controller;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.UserDAO;
import com.inventory.model.Product;
import com.inventory.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;

public class AdminController {
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, Timestamp> createdAtColumn;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;
    @FXML
    private TableColumn<Product, Integer> productUserIdColumn;

    @FXML
    private Label messageLabel;

    private UserDAO userDAO;
    private ProductDAO productDAO;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        productDAO = new ProductDAO();
        setupTables();
        refreshData();
    }

    private void setupTables() {
        // Setup User Table
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Setup Product Table
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    private void refreshData() {
        // Refresh User Table
        ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAllUsers());
        userTable.setItems(users);

        // Refresh Product Table
        ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getAllProducts());
        productTable.setItems(products);
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            messageLabel.setText("Please select a user to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete user: " + selectedUser.getUsername()
                + "?\nThis will also delete all their products.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (userDAO.deleteUser(selectedUser.getId())) {
                messageLabel.setText("User deleted successfully");
                refreshData();
            } else {
                messageLabel.setText("Failed to delete user");
            }
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            messageLabel.setText("Please select a product to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Product");
        alert.setContentText("Are you sure you want to delete product: " + selectedProduct.getName() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (productDAO.deleteProduct(selectedProduct.getId(), selectedProduct.getUserId())) {
                messageLabel.setText("Product deleted successfully");
                refreshData();
            } else {
                messageLabel.setText("Failed to delete product");
            }
        }
    }

    @FXML
    private void handleDeleteAllProducts() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete All Products");
        alert.setContentText("Are you sure you want to delete ALL products?\nThis action cannot be undone!");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (productDAO.deleteAllProducts()) {
                messageLabel.setText("All products deleted successfully");
                refreshData();
            } else {
                messageLabel.setText("Failed to delete all products");
            }
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            messageLabel.setText("Error loading login page");
            e.printStackTrace();
        }
    }
}