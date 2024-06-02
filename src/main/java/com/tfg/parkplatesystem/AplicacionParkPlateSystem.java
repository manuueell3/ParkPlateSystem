package com.tfg.parkplatesystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

import java.util.Objects;

public class AplicacionParkPlateSystem extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/tfg/parkplatesystem/fxml/inicioSesion.fxml")));
        primaryStage.setTitle("Park Plate System - Inicio de Sesión");
        primaryStage.initStyle(StageStyle.UNDECORATED);

        // Cargar y establecer el ícono
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/tfg/parkplatesystem/images/icono_ParkPlateSystem.png")));
        primaryStage.getIcons().add(icon);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
