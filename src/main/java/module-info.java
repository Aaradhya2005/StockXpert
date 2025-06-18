module com.inventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.inventory to javafx.fxml;
    opens com.inventory.controller to javafx.fxml;

    exports com.inventory;
    exports com.inventory.controller;
}