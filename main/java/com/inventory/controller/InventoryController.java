package com.inventory.controller;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
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

public class InventoryController {
    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private Label messageLabel;

    private ProductDAO productDAO;
    private int currentUserId;

    @FXML
    public void initialize() {
        productDAO = new ProductDAO();
        setupTable();
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        refreshTable();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(newSelection.getId());
                nameField.setText(newSelection.getName());
                priceField.setText(String.valueOf(newSelection.getPrice()));
                quantityField.setText(String.valueOf(newSelection.getQuantity()));
            }
        });
    }

    @FXML
    private void handleAddProduct() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (id.isEmpty() || name.isEmpty()) {
                messageLabel.setText("Please fill in all fields");
                return;
            }

            Product product = new Product(id, name, price, quantity, currentUserId);
            if (productDAO.addProduct(product)) {
                messageLabel.setText("Product added successfully");
                clearFields();
                refreshTable();
            } else {
                messageLabel.setText("Failed to add product");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter valid numbers for price and quantity");
        }
    }

    @FXML
    private void handleUpdateProduct() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (id.isEmpty() || name.isEmpty()) {
                messageLabel.setText("Please fill in all fields");
                return;
            }

            Product product = new Product(id, name, price, quantity, currentUserId);
            if (productDAO.updateProduct(product)) {
                messageLabel.setText("Product updated successfully");
                clearFields();
                refreshTable();
            } else {
                messageLabel.setText("Failed to update product");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter valid numbers for price and quantity");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            messageLabel.setText("Please select a product to delete");
            return;
        }

        if (productDAO.deleteProduct(id, currentUserId)) {
            messageLabel.setText("Product deleted successfully");
            clearFields();
            refreshTable();
        } else {
            messageLabel.setText("Failed to delete product");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            messageLabel.setText("Error loading login page");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        productTable.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getAllProducts(currentUserId));
        productTable.setItems(products);
    }
}