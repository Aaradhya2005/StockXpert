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
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;
    @FXML
    private TableColumn<Product, Integer> productUserIdColumn;

    @FXML
    private TableView<Product> lowStockTable;
    @FXML
    private TableColumn<Product, String> lowStockIdColumn;
    @FXML
    private TableColumn<Product, String> lowStockNameColumn;
    @FXML
    private TableColumn<Product, Double> lowStockPriceColumn;
    @FXML
    private TableColumn<Product, Integer> lowStockQuantityColumn;
    @FXML
    private TableColumn<Product, Integer> lowStockThresholdColumn;
    @FXML
    private TableColumn<Product, Integer> lowStockUserIdColumn;
    @FXML
    private TableColumn<Product, String> lowStockStatusColumn;

    @FXML
    private TextField userSearchField;
    @FXML
    private Button userBackButton;
    @FXML
    private TextField productSearchField;
    @FXML
    private Button productBackButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Label lowStockOverviewLabel;

    private UserDAO userDAO;
    private ProductDAO productDAO;
    private ObservableList<User> allUsers;
    private ObservableList<Product> allProducts;
    private ObservableList<Product> lowStockProducts;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        productDAO = new ProductDAO();
        setupTables();
        refreshData();
    }

    private void setupTables() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        lowStockIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lowStockNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lowStockPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        lowStockQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lowStockThresholdColumn.setCellValueFactory(new PropertyValueFactory<>("lowStockThreshold"));
        lowStockUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        lowStockStatusColumn.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));

        lowStockTable.setRowFactory(tv -> new TableRow<Product>() {
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
                    }
                }
            }
        });
    }

    private void refreshData() {
        allUsers = FXCollections.observableArrayList(userDAO.getAllUsers());
        userTable.setItems(allUsers);

        allProducts = FXCollections.observableArrayList(productDAO.getAllProducts());
        productTable.setItems(allProducts);

        lowStockProducts = FXCollections.observableArrayList(productDAO.getLowStockProducts());
        lowStockTable.setItems(lowStockProducts);

        updateLowStockOverview();
    }

    private void updateLowStockOverview() {
        int totalLowStock = lowStockProducts.size();
        int criticalStock = (int) lowStockProducts.stream().filter(Product::isCriticalStock).count();

        lowStockOverviewLabel.setText(String.format(
                "Total Low Stock Items: %d | Critical Stock (0 quantity): %d",
                totalLowStock, criticalStock));
    }

    @FXML
    private void handleSearchUsers() {
        String searchUsername = userSearchField.getText().trim();
        if (searchUsername.isEmpty()) {
            messageLabel.setText("Please enter a username to search");
            return;
        }

        ObservableList<User> searchResults = FXCollections.observableArrayList(
                userDAO.searchUser(searchUsername));

        if (searchResults.isEmpty()) {
            messageLabel.setText("No users found with username: " + searchUsername);
            userTable.setItems(FXCollections.observableArrayList());
        } else {
            userTable.setItems(searchResults);
            messageLabel.setText("Found " + searchResults.size() + " user(s)");
            userBackButton.setVisible(true);
        }
    }

    @FXML
    private void handleShowAllUsers() {
        userTable.setItems(allUsers);
        userSearchField.clear();
        messageLabel.setText("Showing all users");
        userBackButton.setVisible(false);
    }

    @FXML
    private void handleSearchProducts() {
        String searchId = productSearchField.getText().trim();
        if (searchId.isEmpty()) {
            messageLabel.setText("Please enter a product ID to search");
            return;
        }

        ObservableList<Product> searchResults = FXCollections.observableArrayList(
                productDAO.searchProductForAdmin(searchId));

        if (searchResults.isEmpty()) {
            messageLabel.setText("No products found with ID: " + searchId);
            productTable.setItems(FXCollections.observableArrayList());
        } else {
            productTable.setItems(searchResults);
            messageLabel.setText("Found " + searchResults.size() + " product(s)");
            productBackButton.setVisible(true);
        }
    }

    @FXML
    private void handleShowAllProducts() {
        productTable.setItems(allProducts);
        productSearchField.clear();
        messageLabel.setText("Showing all products");
        productBackButton.setVisible(false);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshLowStock() {
        lowStockProducts = FXCollections.observableArrayList(productDAO.getLowStockProducts());
        lowStockTable.setItems(lowStockProducts);
        updateLowStockOverview();
        messageLabel.setText("Low stock data refreshed successfully");
    }
}