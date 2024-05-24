module com.tfg.parkplatesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.tfg.parkplatesystem.controller to javafx.fxml;
    opens com.tfg.parkplatesystem.model to javafx.base;
    opens com.tfg.parkplatesystem.service to javafx.base;
    opens com.tfg.parkplatesystem.util to javafx.base;
    opens com.tfg.parkplatesystem.configuration to javafx.base;


    exports com.tfg.parkplatesystem.controller;
    exports com.tfg.parkplatesystem.model;
    exports com.tfg.parkplatesystem.service;
    exports com.tfg.parkplatesystem.util;
    exports com.tfg.parkplatesystem.configuration;

    opens com.tfg.parkplatesystem to javafx.fxml;
    exports com.tfg.parkplatesystem;

}

