@echo off
set JAVAFX_PATH=C:\Program Files\Java\javafx-sdk-17.0.15\lib
set MYSQL_CONNECTOR=D:\mysql-connector-j-9.2.0\mysql-connector-j-9.2.0\mysql-connector-j-9.2.0.jar

javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "%MYSQL_CONNECTOR%" -d target/classes src/main/java/com/inventory/*.java src/main/java/com/inventory/controller/*.java src/main/java/com/inventory/dao/*.java src/main/java/com/inventory/model/*.java src/main/java/com/inventory/util/*.java

java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "target/classes;%MYSQL_CONNECTOR%" com.inventory.Main 