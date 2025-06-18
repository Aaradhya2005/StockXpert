package com.inventory.model;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private int userId;
    private int lowStockThreshold;
    private boolean alertEnabled;

    public Product(String id, String name, double price, int quantity, int userId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
        this.lowStockThreshold = 10; // Default threshold
        this.alertEnabled = true; // Default enabled
    }

    public Product(String id, String name, double price, int quantity, int userId, int lowStockThreshold,
            boolean alertEnabled) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
        this.lowStockThreshold = lowStockThreshold;
        this.alertEnabled = alertEnabled;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUserId() {
        return userId;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public boolean isAlertEnabled() {
        return alertEnabled;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public void setAlertEnabled(boolean alertEnabled) {
        this.alertEnabled = alertEnabled;
    }

    // Helper methods for low stock alerts
    public boolean isLowStock() {
        return alertEnabled && quantity <= lowStockThreshold;
    }

    public boolean isCriticalStock() {
        return alertEnabled && quantity == 0;
    }

    public boolean isMediumStock() {
        return alertEnabled && quantity > lowStockThreshold && quantity <= lowStockThreshold * 1.5;
    }

    public String getStockStatus() {
        if (isCriticalStock())
            return "CRITICAL";
        if (isLowStock())
            return "LOW";
        if (isMediumStock())
            return "MEDIUM";
        return "GOOD";
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", lowStockThreshold=" + lowStockThreshold +
                ", alertEnabled=" + alertEnabled +
                '}';
    }
}