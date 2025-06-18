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
import javafx.scene.paint.Color;
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
    private TextField thresholdField;

    @FXML
    private CheckBox alertEnabledCheckBox;

    @FXML
    private TextField searchField;

    @FXML
    private Button backButton;

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
    private TableColumn<Product, Integer> thresholdColumn;

    @FXML
    private TableColumn<Product, String> statusColumn;

    @FXML
    private Label messageLabel;

    @FXML
    private Label lowStockCountLabel;

    private ProductDAO productDAO;
    private int currentUserId;
    private ObservableList<Product> allProducts;

    @FXML
    public void initialize() {
        productDAO = new ProductDAO();
        setupTable();
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        refreshTable();
        updateLowStockCount();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        thresholdColumn.setCellValueFactory(new PropertyValueFactory<>("lowStockThreshold"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));

        // Add row styling for low stock items
        productTable.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.isCriticalStock()) {
                        setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828; -fx-font-weight: bold;");
                    } else if (item.isLowStock()) {
                        setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00; -fx-font-weight: bold;");
                    } else if (item.isMediumStock()) {
                        setStyle("-fx-background-color: #f3e5f5; -fx-text-fill: #7b1fa2; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");
                    }
                }
            }
        });

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(newSelection.getId());
                nameField.setText(newSelection.getName());
                priceField.setText(String.valueOf(newSelection.getPrice()));
                quantityField.setText(String.valueOf(newSelection.getQuantity()));
                thresholdField.setText(String.valueOf(newSelection.getLowStockThreshold()));
                alertEnabledCheckBox.setSelected(newSelection.isAlertEnabled());
            }
        });
    }

    @FXML
    private void handleViewLowStock() {
        ObservableList<Product> lowStockProducts = FXCollections.observableArrayList(
                productDAO.getLowStockProducts(currentUserId));

        if (lowStockProducts.isEmpty()) {
            messageLabel.setText("No low stock items found");
        } else {
            productTable.setItems(lowStockProducts);
            messageLabel.setText("Showing " + lowStockProducts.size() + " low stock item(s)");
            backButton.setVisible(true);
        }
    }

    @FXML
    private void handleSearch() {
        String searchId = searchField.getText().trim();
        if (searchId.isEmpty()) {
            messageLabel.setText("Please enter a product ID to search");
            return;
        }

        ObservableList<Product> searchResults = FXCollections.observableArrayList(
                productDAO.searchProduct(searchId, currentUserId));

        if (searchResults.isEmpty()) {
            messageLabel.setText("No product found with ID: " + searchId);
            productTable.setItems(FXCollections.observableArrayList());
        } else {
            productTable.setItems(searchResults);
            messageLabel.setText("Found " + searchResults.size() + " product(s)");
            backButton.setVisible(true);
        }
    }

    @FXML
    private void handleShowAll() {
        refreshTable();
        searchField.clear();
        messageLabel.setText("Showing all products");
        backButton.setVisible(false);
    }

    @FXML
    private void handleAddProduct() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            int threshold = Integer.parseInt(thresholdField.getText().trim());
            boolean alertEnabled = alertEnabledCheckBox.isSelected();

            if (id.isEmpty() || name.isEmpty()) {
                messageLabel.setText("Please fill in all required fields");
                return;
            }

            Product product = new Product(id, name, price, quantity, currentUserId, threshold, alertEnabled);
            if (productDAO.addProduct(product)) {
                messageLabel.setText("Product added successfully");
                clearFields();
                refreshTable();
                updateLowStockCount();

                // Check for low stock alert
                if (product.isLowStock()) {
                    showLowStockAlert(product);
                }
            } else {
                messageLabel.setText("Failed to add product");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter valid numbers for price, quantity, and threshold");
        }
    }

    @FXML
    private void handleUpdateProduct() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            int threshold = Integer.parseInt(thresholdField.getText().trim());
            boolean alertEnabled = alertEnabledCheckBox.isSelected();

            if (id.isEmpty() || name.isEmpty()) {
                messageLabel.setText("Please fill in all required fields");
                return;
            }

            Product product = new Product(id, name, price, quantity, currentUserId, threshold, alertEnabled);
            if (productDAO.updateProduct(product)) {
                messageLabel.setText("Product updated successfully");
                clearFields();
                refreshTable();
                updateLowStockCount();

                // Check for low stock alert
                if (product.isLowStock()) {
                    showLowStockAlert(product);
                }
            } else {
                messageLabel.setText("Failed to update product");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter valid numbers for price, quantity, and threshold");
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
            updateLowStockCount();
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
        } catch (IOException e) {
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
        thresholdField.clear();
        alertEnabledCheckBox.setSelected(true);
        productTable.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        allProducts = FXCollections.observableArrayList(productDAO.getAllProducts(currentUserId));
        productTable.setItems(allProducts);
        backButton.setVisible(false);
    }

    private void updateLowStockCount() {
        int lowStockCount = productDAO.getLowStockCount(currentUserId);
        if (lowStockCount == 0) {
            lowStockCountLabel.setText("No low stock items");
        } else {
            lowStockCountLabel.setText(lowStockCount + " item(s) running low on stock");
        }
    }

    private void showLowStockAlert(Product product) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Low Stock Alert");
        alert.setHeaderText("Product Stock is Running Low!");

        String content = String.format(
                "Product: %s (ID: %s)\n" +
                        "Current Quantity: %d\n" +
                        "Low Stock Threshold: %d\n\n" +
                        "Please consider restocking this item.",
                product.getName(), product.getId(), product.getQuantity(), product.getLowStockThreshold());

        alert.setContentText(content);
        alert.showAndWait();
    }
}