module client.p2_ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens client.p2_ui to javafx.fxml;
    exports client.p2_ui;
}