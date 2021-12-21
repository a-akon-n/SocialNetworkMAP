module com.example.filter_sem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.filter_sem9 to javafx.fxml;
    exports com.example.filter_sem9;
    exports com.example.controller;
    exports com.example.service;
    exports com.example.repository;
    exports com.example.domain;
    opens com.example.service to javafx.fxml;
    opens com.example.controller to javafx.fxml;

    opens com.example.domain to javafx.graphics, javafx.fxml, javafx.base;
}