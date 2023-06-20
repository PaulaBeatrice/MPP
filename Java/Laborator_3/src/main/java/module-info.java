module com.example.laborator_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;


    opens com.example.laborator_3 to javafx.fxml;
    exports com.example.laborator_3;


    opens com.example.laborator_3.controller to javafx.fxml;
    exports com.example.laborator_3.controller;

    opens com.example.laborator_3.domain to javafx.fxml;
    exports com.example.laborator_3.domain;
}